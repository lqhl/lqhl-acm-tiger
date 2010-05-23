package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class StoreI extends TExp {

	public Temp mem;
	int offset;
	int src;

	public StoreI(Temp mem, int offset, int value) {
		this.mem = mem;
		this.offset = offset;
		this.src = value;
	}
}
