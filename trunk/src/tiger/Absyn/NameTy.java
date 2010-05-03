package tiger.Absyn;
import tiger.Symbol.Symbol;
public class NameTy extends Ty {
   public Symbol name;
   public NameTy(int l, int c, Symbol n) {line = l; colume = c; name=n;}
}
