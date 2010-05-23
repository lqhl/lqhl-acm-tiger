package tiger.Translate;

import tiger.Temp.*;
import tiger.Tree.*;

public abstract class Cx extends Exp {

	abstract Stm unCx(Label t, Label f);

	tiger.Tree.Expr unEx() {
		Temp r = new Temp();
		Label t = new Label();
		Label f = new Label();
		return new ESEQ(
				new SEQ(new MOVE(new TEMP(r), new CONST(1)),
						new SEQ(unCx(t, f),
						new SEQ(new LABEL(f),
						new SEQ(new MOVE(new TEMP(r), new CONST(0)),
						new LABEL(t))))),
					new TEMP(r));
	}

	Stm unNx() {
		return new EXP(unEx());
	}

}
