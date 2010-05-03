package tiger.Absyn;
import tiger.Symbol.Symbol;
public class StringExp extends Exp {
   public String value;
   public StringExp(int l, int c, String v) {line = l; colume = c; value=v;}
}
