package tiger.Absyn;
import tiger.Symbol.Symbol;
public class LetExp extends Exp {
   public DecList decs;
   public Exp body;
   public LetExp(int l, int c, DecList d, Exp b) {line = l; colume = c; decs=d; body=b;}
}
