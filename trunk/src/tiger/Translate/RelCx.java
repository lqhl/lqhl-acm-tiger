package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

public class RelCx extends Cx {
	int oper = 0;
	Exp left = null;
	Exp right = null;
	public RelCx(int op, Exp l, Exp r) {
		oper = op;
		left = l;
		right = r;
	}
	public Stm unCx(Label t, Label f) {
		return new CJUMP(oper, left.unEx(), right.unEx(), t, f);
	}
	
}
