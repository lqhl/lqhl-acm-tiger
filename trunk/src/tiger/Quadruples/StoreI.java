package tiger.Quadruples;

import tiger.Temp.Temp;

public class StoreI extends TExp {

	public Temp mem;
	public int offset;
	public int src;

	public StoreI(Temp mem, int offset, int value) {
		this.mem = mem;
		this.offset = offset;
		this.src = value;
	}
	public void replaceDef(Temp oldTemp, Temp newTemp) {
		throw new RuntimeException("Store's replaceDef should not be used.");
	}

	public void replaceUse(Temp oldTemp, Temp newTemp) {
		if (mem == oldTemp)
			mem = newTemp;
	}
}
