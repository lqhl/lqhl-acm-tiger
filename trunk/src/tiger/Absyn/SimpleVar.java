package tiger.Absyn;
import tiger.Symbol.Symbol;
public class SimpleVar extends Var {
   public Symbol name;
   public SimpleVar (int l, int c, Symbol n) {line = l; colume = c; name=n;}
}
