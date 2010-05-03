package tiger.Absyn;
import tiger.Symbol.Symbol;
public class RecordExp extends Exp {
   public Symbol typ;
   public FieldExpList fields;
   public RecordExp(int l, int c, Symbol t, FieldExpList f) {line = l; colume = c; typ=t;fields=f;}
}
