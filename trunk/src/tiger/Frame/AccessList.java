package tiger.Frame;

public class AccessList {
	public Access head;
	public AccessList tail;
	public AccessList(Access head, AccessList tail){
		this.head = head;
		this.tail = tail;
	}
}
