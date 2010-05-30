package tiger.Quadruples;

import tiger.Temp.Temp;

public class MoveLabel extends TExp {

	public Temp dst;
	public Label srcLabel;

	public MoveLabel(Temp left, Label right) {
		this.dst = left;
		this.srcLabel = right;
	}

	public void replaceDef(Temp oldTemp, Temp newTemp) {
		if (dst == oldTemp)
			dst = newTemp;
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("MoveLabel's replaceUse should not be used.");
	}
}
