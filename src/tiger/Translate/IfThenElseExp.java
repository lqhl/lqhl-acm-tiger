package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

public class IfThenElseExp extends Exp {
	Exp test;
	Exp e_then, e_else;
	
	IfThenElseExp(Exp t, Exp e1, Exp e2) {
		test = t;
		e_then = e1;
		e_else = e2;
	}

	Stm unCx(Label t, Label f) {
		return new CJUMP(CJUMP.NE, unEx(), new CONST(0), t, f);
	}

	Expr unEx() {
		Temp r = new Temp();
		Label join = new Label();
		Label t = new Label();
		Label f = new Label();
		return new ESEQ(new SEQ(test.unCx(t, f),
						new SEQ(new LABEL(t),
						new SEQ(new MOVE(new TEMP(r), e_then.unEx()),
						new SEQ(new JUMP(join),
						new SEQ(new LABEL(f),
						new SEQ(new MOVE(new TEMP(r), e_else.unEx()),
						new LABEL(join))))))),
						new TEMP(r));
	}

	Stm unNx() {
		Label join = new Label();
		Label t = new Label();
		Label f = new Label();
		if (e_else == null)
			return new SEQ(test.unCx(t, join),
					new SEQ(new LABEL(t),
					new SEQ(e_then.unNx(),
					new LABEL(join))));
		else
			return new SEQ(test.unCx(t, f),
					new SEQ(new LABEL(t),
					new SEQ(e_then.unNx(),
					new SEQ(new JUMP(join),
					new SEQ(new LABEL(f),
					new SEQ(e_else.unNx(),
					new LABEL(join)))))));
	}
}
