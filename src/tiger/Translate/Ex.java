package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

public class Ex extends Exp {
	tiger.Tree.Expr exp;
	
	Ex(tiger.Tree.Expr e) {
		exp = e;
	}

	Stm unCx(Label t, Label f) {
		return new CJUMP(CJUMP.NE, exp, new CONST(0), t, f);
	}

	tiger.Tree.Expr unEx() {
		return exp;
	}

	Stm unNx() {
		return new tiger.Tree.EXP(exp);
	}

}
