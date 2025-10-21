package lab2;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public final class Invoker {
    public static List<String> run(Object target) {
        List<String> logs = new ArrayList<>();
        for (Method m : target.getClass().getDeclaredMethods()) {
            Repeat rep = m.getAnnotation(Repeat.class);
            if (rep == null) continue;
            int mod = m.getModifiers();
            if (!(Modifier.isProtected(mod) || Modifier.isPrivate(mod))) continue;

            m.setAccessible(true);
            Object[] args = defaultsFor(m.getParameterTypes());

            for (int i = 0; i < rep.value(); i++) {
                try {
                    Object r = m.invoke(target, args);
                    logs.add(m.getName() + " #" + (i+1) + " -> " + String.valueOf(r));
                } catch (InvocationTargetException ite) {
                    logs.add(m.getName() + " threw: " + ite.getTargetException());
                } catch (Exception e) {
                    logs.add(m.getName() + " ERROR: " + e);
                }
            }
        }
        return logs;
    }

    /** То, что ждёт твой GUI. */
    public static List<String> runAll() {
        return run(new SampleService());
    }

    private static Object[] defaultsFor(Class<?>[] types) {
        Object[] a = new Object[types.length];
        for (int i = 0; i < types.length; i++) a[i] = defaultFor(types[i]);
        return a;
    }
    private static Object defaultFor(Class<?> t) {
        if (t.isPrimitive()) {
            if (t == int.class) return 0;
            if (t == long.class) return 0L;
            if (t == boolean.class) return false;
            if (t == double.class) return 0.0d;
            if (t == float.class) return 0.0f;
            if (t == short.class) return (short)0;
            if (t == byte.class) return (byte)0;
            if (t == char.class) return '\0';
        } else {
            if (t == String.class) return "";
            if (t == Integer.class) return 0;
            if (t == Long.class) return 0L;
            if (t == Boolean.class) return false;
            if (t == Double.class) return 0.0d;
            if (t == Float.class) return 0.0f;
            if (t == Short.class) return (short)0;
            if (t == Byte.class) return (byte)0;
            if (t == Character.class) return '\0';
            if (t.isArray()) return Array.newInstance(t.getComponentType(), 0);
        }
        return null;
    }

    private Invoker() {}
}
