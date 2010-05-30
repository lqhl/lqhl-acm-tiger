package tiger.Quadruples;

import tiger.Temp.Temp;

public class Jump extends TExp {

	public Label label;

	public Jump(Label label) {
		this.label = label;
	}

	public void replaceDef(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("Jump's replaceDef should not be used.");
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("Jump's replaceUse should not be used.");
	}
}
