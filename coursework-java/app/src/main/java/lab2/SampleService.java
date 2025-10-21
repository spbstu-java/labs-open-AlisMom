package lab2;

public class SampleService {
    public void pub1(int x) { System.out.println("pub1:"+x); }
    public String pub2(String s, boolean b) { String r=s+"|"+b; System.out.println("pub2:"+r); return r; }

    @Repeat(2)
    protected void prot1(int a, String s) { System.out.println("prot1:"+a+","+s); }

    @Repeat(3)
    protected int prot2() { System.out.println("prot2"); return 42; }

    @Repeat(4)
    private void priv1(double d) { System.out.println("priv1:"+d); }

    @Repeat(1)
    private String priv2(Integer v, Object o) { String r="priv2:"+v+","+(o==null?"null":o.toString()); System.out.println(r); return r; }
}
