package tiger.Temp;

import tiger.Temp.TempList;

public class TempList {
	public Temp head;
	public TempList tail;
	public TempList(Temp h, TempList t) {head=h; tail=t;}
	public TempList(TempList h, TempList t)
	{
		TempList temp = null;
		while ( h != null )
		{
			temp = new TempList(h.head, temp);
			h = h.tail;
		}
		tail = t;
		while ( temp != null )
		{
			tail = new TempList(temp.head, tail);
			temp = temp.tail;
		}
		head = tail.head;
		tail = tail.tail;
	}
}

