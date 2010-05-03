package tiger.Absyn;
import tiger.Symbol.Symbol;
public class FieldVar extends Var {
   public Var var;
   public Symbol field;
   public FieldVar(int l, int c, Var v, Symbol f) {line = l; colume = c; var=v; field=f;}
}
