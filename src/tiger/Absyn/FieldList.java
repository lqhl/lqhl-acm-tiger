package tiger.Absyn;
import tiger.Symbol.Symbol;
public class FieldList extends Absyn {
   public Symbol name;
   public Symbol typ;
   public FieldList tail;
   public boolean escape = true;
   public FieldList(int l, int c, Symbol n, Symbol t, FieldList x) {line = l; colume = c; name=n; typ=t; tail=x;}
}
