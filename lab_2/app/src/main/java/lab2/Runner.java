package lab2;

import java.lang.reflect.*;

public class Runner {
    public static void main(String[] args) throws Exception {
        SampleService svc = new SampleService();
        for (Method m : SampleService.class.getDeclaredMethods()) {
            Repeat rep = m.getAnnotation(Repeat.class);
            if (rep == null) continue;
            int mod = m.getModifiers();
            if (!(Modifier.isProtected(mod) || Modifier.isPrivate(mod))) continue;
            m.setAccessible(true);
            Object[] invokeArgs = defaultsFor(m.getParameterTypes());
            for (int i = 0; i < rep.value(); i++) m.invoke(svc, invokeArgs);
        }
    }

    static Object[] defaultsFor(Class<?>[] types) {
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Class<?> t = types[i];
            if (!t.isPrimitive()) {
                if (t == String.class) args[i] = "";
                else if (t.isArray()) args[i] = Array.newInstance(t.getComponentType(), 0);
                else args[i] = null;
            } else if (t == int.class) args[i] = 0;
            else if (t == long.class) args[i] = 0L;
            else if (t == boolean.class) args[i] = false;
            else if (t == double.class) args[i] = 0.0d;
            else if (t == float.class) args[i] = 0.0f;
            else if (t == short.class) args[i] = (short)0;
            else if (t == byte.class) args[i] = (byte)0;
            else if (t == char.class) args[i] = '\0';
        }
        return args;
    }
}
