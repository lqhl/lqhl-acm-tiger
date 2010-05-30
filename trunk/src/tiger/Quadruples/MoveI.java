package tiger.Quadruples;

import tiger.Temp.Temp;

public class MoveI extends TExp {

	public Temp dst;
	public int src;
	
	public MoveI(Temp dst, int src) {
		this.dst = dst;
		this.src = src;
	}
	
	public void replaceDef(Temp oldTemp, Temp newTemp) {
		if (dst == oldTemp)
			dst = newTemp;
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		// Nothing to do
		throw new RuntimeException("MoveI's replaceUse should not be used.");
	}
}
