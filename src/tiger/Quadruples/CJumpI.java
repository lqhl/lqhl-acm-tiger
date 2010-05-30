package tiger.Quadruples;

import tiger.Temp.Temp;

public class CJumpI extends TExp {
	public int relop;
	public tiger.Temp.Temp left;
	public int right;
	public Label label;
	
	public CJumpI(int relop, Temp l, int r, Label lb) {
		this.relop = relop;
		left = l;
		right = r;
		label = lb;
	}

	public void replaceDef(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("CJumpI's replaceDef should not be used.");
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		if (left == oldTemp)
			left = newTemp;
	}
}
