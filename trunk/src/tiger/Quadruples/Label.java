package tiger.Quadruples;

import tiger.Temp.Temp;

public class Label extends TExp {

	public tiger.Temp.Label label;
	public int number;

	public Label(tiger.Temp.Label label, int num) {
		this.label = label;
		number = num;
	}

	public Label(tiger.Temp.Label label) {
		this.label = label;
		number = -1;
	}
	
	public void replaceDef(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("Label's replaceDef should not be used.");
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("Label's replaceUse should not be used.");
	}
}
