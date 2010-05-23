package tiger.FindEscape;

import tiger.Absyn.*;

public class FormalEscape extends Escape {
	FieldList fl;
	FormalEscape(int d, FieldList f) {
		depth = d;
		fl = f;
		fl.escape = false;
	}
	void setEscape() {
		fl.escape = true;
	}
}
