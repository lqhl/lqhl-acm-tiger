package tiger.Quadruples;

import tiger.ReachingDefinitions.Node;
import tiger.Temp.Temp;

public abstract class TExp {
	public tiger.Liveness.LivenessNode livenessNode = null;
	public Node reachingDefinitionsNode = null;
	public boolean mark;

	public abstract void replaceUse(Temp oldTemp, Temp newTemp);
	public abstract void replaceDef(Temp oldTemp, Temp newTemp);
}
