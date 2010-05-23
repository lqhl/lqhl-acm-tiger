package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class MoveLabel extends TExp {

	public Temp dst;
	Label srcLabel;

	public MoveLabel(Temp left, Label right) {
		this.dst = left;
		this.srcLabel = right;
	}

}
