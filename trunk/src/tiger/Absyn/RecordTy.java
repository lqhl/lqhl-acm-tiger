package tiger.Absyn;
import tiger.Symbol.Symbol;
public class RecordTy extends Ty {
   public FieldList fields;
   public RecordTy(int l, int c, FieldList f) {line = l; colume = c; fields=f;}
}   
