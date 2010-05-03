package tiger.Absyn;
import tiger.Symbol.Symbol;
public class VarExp extends Exp {
   public Var var;
   public VarExp(int l, int c, Var v) {line = l; colume = c; var=v;}
}   
