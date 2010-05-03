package tiger.Absyn;
import tiger.Symbol.Symbol;
public class ArrayTy extends Ty {
   public Symbol typ;
   public ArrayTy(int l, int c, Symbol t) {line = l; colume = c; typ=t;}
}
