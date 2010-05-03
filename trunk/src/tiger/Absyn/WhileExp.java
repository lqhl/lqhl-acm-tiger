package tiger.Absyn;
import tiger.Symbol.Symbol;
public class WhileExp extends Exp {
   public Exp test, body;
   public WhileExp(int l, int c, Exp t, Exp b) {line = l; colume = c; test=t; body=b;}
}
