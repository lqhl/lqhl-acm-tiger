package tiger.Quadruples;

import tiger.Temp.Temp;

public class ReturnSink extends TExp {
	public void replaceDef(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("ReturnSink's replaceDef should not be used.");
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("ReturnSink's replaceDef should not be used.");
	}
}
