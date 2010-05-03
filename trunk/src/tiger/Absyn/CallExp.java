package tiger.Absyn;
import tiger.Symbol.Symbol;
public class CallExp extends Exp {
   public Symbol func;
   public ExpList args;
   public CallExp(int l, int c, Symbol f, ExpList a) {line = l; colume = c; func=f; args=a;}
}
