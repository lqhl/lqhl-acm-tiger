package tiger.Semant;
import tiger.Translate.Access;
import tiger.Types.*;

public class VarEntry extends Entry {
	public Type ty;
	public Access access;
	public VarEntry(Access a, Type t) {
		access = a;
		ty = t;
	}
}
