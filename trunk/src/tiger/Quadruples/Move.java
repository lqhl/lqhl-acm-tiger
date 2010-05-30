package tiger.Quadruples;

import tiger.Temp.Temp;

public class Move extends TExp {
	
	public Temp dst;
	public Temp src;

	public Move(Temp dst, Temp src) {
		this.dst = dst;
		this.src = src;
	}
	public void replaceDef(Temp oldTemp, Temp newTemp) {
		if (dst == oldTemp)
			dst = newTemp;
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		if (src == oldTemp)
			src = newTemp;
	}
}
