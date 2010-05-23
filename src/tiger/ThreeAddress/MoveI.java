package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class MoveI extends TExp {

	public Temp dst;
	int src;
	
	public MoveI(Temp dst, int src) {
		this.dst = dst;
		this.src = src;
	}

}
