package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class Load extends TExp {

	public Temp mem;
	int offset;
	public Temp dst;

	public Load(Temp mem, int offset, Temp dst) {
		this.mem = mem;
		this.offset = offset;
		this.dst = dst;
	}

}
