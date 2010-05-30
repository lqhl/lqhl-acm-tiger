package tiger.Quadruples;

import tiger.Temp.Temp;

public class Load extends TExp {

	public Temp mem;
	public int offset;
	public Temp dst;

	public Load(Temp mem, int offset, Temp dst) {
		this.mem = mem;
		this.offset = offset;
		this.dst = dst;
	}

	public void replaceDef(Temp oldTemp, Temp newTemp) {
		if (dst == oldTemp)
			dst = newTemp;
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		if (mem == oldTemp)
			mem = newTemp;
	}
}
