package tiger.FindEscape;

import tiger.Absyn.*;

public class VarEscape extends Escape {
	VarDec vd;
	VarEscape(int d, VarDec v) {
		depth = d;
		vd = v;
		vd.escape = false;
	}
	void setEscape() {
		vd.escape = true;
	}
}
