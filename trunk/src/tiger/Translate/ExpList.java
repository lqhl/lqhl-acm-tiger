package tiger.Translate;

public class ExpList {
	Exp head;
	public ExpList tail;
	public ExpList(Exp h, ExpList t) {
		head = h;
		tail = t;
	}
}
