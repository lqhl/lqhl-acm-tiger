package tiger.Semant;
import tiger.Translate.Access;
import tiger.Types.*;

public class VarEntry extends Entry {
	Type ty;
	Access access;
	VarEntry(Access a, Type t) {
		access = a;
		ty = t;
	}
}
