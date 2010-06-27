package tiger.Semant;
import tiger.Translate.*;
import tiger.Types.*;

public class ExpTy {
	public Exp exp;
	public Type ty;
	public ExpTy(Exp e, Type t) {
		exp = e;
		ty = t;
	}
}
