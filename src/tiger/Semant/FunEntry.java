package tiger.Semant;
import tiger.Types.*;

public class FunEntry extends Entry {
	RECORD formals;
	Type result;
	FunEntry (RECORD f, Type r) {
		formals = f;
		result = r;
	}
}
