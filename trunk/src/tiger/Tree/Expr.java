package tiger.Tree;
abstract public class Expr {
	abstract public ExpList kids();
	abstract public Expr build(ExpList kids);
}
