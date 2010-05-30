package tiger.Quadruples;

import tiger.Temp.Temp;
import tiger.Temp.TempList;

public class Call extends TExp {
	public Label name;
	public TempList param;
	public void replaceDef(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("Call's replaceDef should not be used.");
	}
	public void replaceUse(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("Call's replaceUse should not be used.");
	}
}
