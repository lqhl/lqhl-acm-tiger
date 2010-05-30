package tiger.Quadruples;

import tiger.Temp.Temp;

public abstract class TExp {
	public tiger.Liveness.LivenessNode node = null;

	public abstract void replaceUse(Temp oldTemp, Temp newTemp);
	public abstract void replaceDef(Temp oldTemp, Temp newTemp);
}
