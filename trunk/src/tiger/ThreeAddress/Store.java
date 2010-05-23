package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class Store extends TExp {

	public Temp mem;
	int offset;
	public Temp src;

	public Store(Temp mem, int offset, Temp src) {
		this.mem = mem;
		this.offset = offset;
		this.src = src;
	}
}
