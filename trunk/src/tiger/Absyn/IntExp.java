package tiger.Absyn;
import tiger.Symbol.Symbol;
public class IntExp extends Exp {
   public int value;
   public IntExp(int l, int c, int v) {line = l; colume = c; value=v;}
}
