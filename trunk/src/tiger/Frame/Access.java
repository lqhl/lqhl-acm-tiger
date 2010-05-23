package tiger.Frame;

import tiger.Tree.Expr;

public abstract class Access {
	public abstract Expr exp(Expr framePtr);
	public abstract Expr expFromStack(Expr stackPtr);
}
