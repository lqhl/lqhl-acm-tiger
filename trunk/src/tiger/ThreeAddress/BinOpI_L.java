package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class BinOpI_L extends TExp {
	int oper;
	int left;
	public Temp right;
	public Temp dst;

	public BinOpI_L(int op, Temp dst, int left, Temp right) {
		oper = op;
		this.dst = dst;
		this.left = left;
		this.right = right;
	}

}
