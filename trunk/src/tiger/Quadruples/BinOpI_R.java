package tiger.Quadruples;

import tiger.Temp.Temp;

public class BinOpI_R extends TExp {
	public int oper;
	public Temp left;
	public int right;
	public Temp dst;

	public BinOpI_R(int op, Temp dst, Temp left, int right) {
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
	}
}
