package tiger.ThreeAddress;

import tiger.Temp.Temp;

public class CJump extends TExp {
	int relop;
	public tiger.Temp.Temp left;
	public tiger.Temp.Temp right;
	public Label label;
	
	public CJump(int relop, Temp l, Temp r, Label lb) {
		this.relop = relop;
		left = l;
		right = r;
		label = lb;
	}
}
