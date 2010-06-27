package tiger.Semant;
import tiger.Temp.Label;
import tiger.Translate.Level;
import tiger.Types.*;

public class FunEntry extends Entry {
	public Level level;
	public Label label;
	public RECORD formals;
	public Type result;
	public FunEntry (Level lvl, Label lb, RECORD f, Type r) {
		level = lvl;
		label = lb;
		formals = f;
		result = r;
	}
}
