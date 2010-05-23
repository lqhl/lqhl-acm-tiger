package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class BinOpI_R extends TExp {
	int oper;
	public Temp left;
	int right;
	public Temp dst;

	public BinOpI_R(int op, Temp dst, Temp left, int right) {
		oper = op;
		this.dst = dst;
		this.left = left;
		this.right = right;
	}

}
