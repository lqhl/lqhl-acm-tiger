package tiger.Translate;

import tiger.Temp.Label;
import tiger.Tree.CONST;
import tiger.Tree.Expr;
import tiger.Tree.JUMP;
import tiger.Tree.Stm;

public class IntExp extends Exp {
	int value;
	
	public IntExp(int v) {
		value = v;
	}
	Stm unCx(Label t, Label f) {
		if (value != 0)
			return new JUMP(t);
		else
			return new JUMP(f);
	}

	Expr unEx() {
		return new CONST(value);
	}

	Stm unNx() {
		return new tiger.Tree.EXP(new CONST(value));
	}

}
