package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class Move extends TExp {
	
	public Temp dst;
	public Temp src;

	public Move(Temp dst, Temp src) {
		this.dst = dst;
		this.src = src;
	}

}
