package tiger.Quadruples;

import tiger.Temp.Temp;

public class Store extends TExp {

	public Temp mem;
	public int offset;
	public Temp src;

	public Store(Temp mem, int offset, Temp src) {
		this.mem = mem;
		this.offset = offset;
		this.src = src;
	}

	public void replaceDef(Temp oldTemp, Temp newTemp) {
		throw new RuntimeException("Store's replaceDef should not be used.");
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		if (mem == oldTemp)
			mem = newTemp;
		if (src == oldTemp)
			src = newTemp;
	}
}
