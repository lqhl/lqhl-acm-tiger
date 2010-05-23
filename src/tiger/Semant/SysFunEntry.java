package tiger.Semant;

import tiger.Temp.Label;
import tiger.Translate.Level;
import tiger.Types.RECORD;
import tiger.Types.Type;

public class SysFunEntry extends FunEntry {
	SysFunEntry (Level lvl, Label lb, RECORD f, Type r) {
		super(lvl, lb, f, r);
	}
}
