package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class BinOp extends TExp {
	int oper;
	public tiger.Temp.Temp left;
	public tiger.Temp.Temp right;
	public Temp dst;
	
	public BinOp(int op, Temp dst, Temp left, Temp right) {
		oper = op;
		this.dst = dst;
		this.left = left;
		this.right = right;
	}
}
