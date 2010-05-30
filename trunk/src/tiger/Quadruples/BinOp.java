package tiger.Quadruples;

import tiger.Temp.Temp;

public class BinOp extends TExp {
	public int oper;
	public tiger.Temp.Temp left;
	public tiger.Temp.Temp right;
	public Temp dst;
	
	public BinOp(int op, Temp dst, Temp left, Temp right) {
		oper = op;
		this.dst = dst;
		this.left = left;
		this.right = right;
	}

	public void replaceDef(Temp oldTemp, Temp newTemp) {
		if (dst == oldTemp)
			dst = newTemp;
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		if (left == oldTemp)
			left = newTemp;
		if (right == oldTemp)
			right = newTemp;
	}
}
