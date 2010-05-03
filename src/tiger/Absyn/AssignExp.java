package tiger.Absyn;
public class AssignExp extends Exp {
   public Var var;
   public Exp exp;
   public AssignExp(int l, int c, Var v, Exp e) {line = l; colume = c; var=v; exp=e;}
}
