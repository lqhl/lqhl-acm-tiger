package tiger.Quadruples;

import tiger.Temp.Temp;

public class BinOpI_L extends TExp {
	public int oper;
	public int left;
	public Temp right;
	public Temp dst;

	public BinOpI_L(int op, Temp dst, int left, Temp right) {
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
		if (right == oldTemp)
			right = newTemp;
	}
}
