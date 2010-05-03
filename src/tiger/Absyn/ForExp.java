package tiger.Absyn;
import tiger.Symbol.Symbol;
public class ForExp extends Exp {
   public VarDec var;
   public Exp hi, body;
   public ForExp(int l, int c, VarDec v, Exp h, Exp b) {line = l; colume = c; var=v; hi=h; body=b;}
}
