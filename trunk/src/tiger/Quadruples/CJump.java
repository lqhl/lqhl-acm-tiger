package tiger.Quadruples;

import tiger.Temp.Temp;

public class CJump extends TExp {
	public int relop;
	public tiger.Temp.Temp left;
	public tiger.Temp.Temp right;
	public Label label;
	
	public CJump(int relop, Temp l, Temp r, Label lb) {
		this.relop = relop;
		left = l;
		right = r;
		label = lb;
	}

	public void replaceDef(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("CJump's replaceDef should not be used.");
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		if (left == oldTemp)
			left = newTemp;
		if (right == oldTemp)
			right = newTemp;
	}
}
