package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class CJumpI extends TExp {
	int relop;
	int left;
	public tiger.Temp.Temp right;
	public Label label;
	
	public CJumpI(int relop, int l, Temp r, Label lb) {
		this.relop = relop;
		left = l;
		right = r;
		label = lb;
	}
}
