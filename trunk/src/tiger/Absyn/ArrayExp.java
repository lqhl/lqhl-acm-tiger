package tiger.Absyn;
import tiger.Symbol.Symbol;
public class ArrayExp extends Exp {
   public Symbol typ;
   public Exp size, init;
   public ArrayExp(int l, int c, Symbol t, Exp s, Exp i) {line = l; colume = c; typ=t; size=s; init=i;}
}
