package tiger.Semant;
import tiger.Temp.Label;
import tiger.Translate.Level;
import tiger.Types.*;

public class FunEntry extends Entry {
	Level level;
	Label label;
	RECORD formals;
	Type result;
	FunEntry (Level lvl, Label lb, RECORD f, Type r) {
		level = lvl;
		label = lb;
		formals = f;
		result = r;
	}
}
