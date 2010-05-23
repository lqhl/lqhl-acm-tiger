package tiger.Translate;

public abstract class Exp {
	abstract tiger.Tree.Expr unEx();
	abstract tiger.Tree.Stm unNx();
	abstract tiger.Tree.Stm unCx(tiger.Temp.Label t, tiger.Temp.Label f);
}
