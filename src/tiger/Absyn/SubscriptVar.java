package tiger.Absyn;
import tiger.Symbol.Symbol;
public class SubscriptVar extends Var {
   public Var var;
   public Exp index;
   public SubscriptVar(int l, int c, Var v, Exp i) {line = l; colume = c; var=v; index=i;}
}
