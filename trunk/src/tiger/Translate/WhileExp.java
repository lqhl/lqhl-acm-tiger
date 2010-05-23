package tiger.Translate;

import tiger.Tree.*;
import tiger.Temp.*;

public class WhileExp extends Exp {
	Exp test;
	Exp body;
	Label done;

	WhileExp(Exp test, Exp body, Label done){
		this.test = test;
		this.body = body;
		this.done = done;
	}

	Stm unCx(Label t, Label f) {
		return null;
	}

	Expr unEx() {
		return null;
	}

	Stm unNx() {
		Label begin = new Label();
		Label t = new Label();
		return new SEQ( new LABEL(begin),
				new SEQ(test.unCx(t, done),
				new SEQ(new LABEL(t),
				new SEQ(body.unNx(),
				new SEQ(new JUMP(begin),
				new LABEL(done))))));
	}
}
