package tiger.Absyn;
import tiger.Symbol.Symbol;
public class IfExp extends Exp {
   public Exp test;
   public Exp thenclause;
   public Exp elseclause; /* optional */
   public IfExp(int l, int c, Exp x, Exp y) {line = l; colume = c; test=x; thenclause=y; elseclause=null;}
   public IfExp(int l, int c, Exp x, Exp y, Exp z) {line = l; colume = c; test=x; thenclause=y; elseclause=z;}
}
