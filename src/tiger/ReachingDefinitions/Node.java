package tiger.ReachingDefinitions;

import java.util.HashSet;
import java.util.Hashtable;

import tiger.Liveness.LivenessNode;
import tiger.Quadruples.*;
import tiger.Temp.Temp;

public class Node {
	HashSet <PairI> gen = new HashSet <PairI> ();
	HashSet <PairI> kill = new HashSet <PairI> ();
	
	public Node(TExp e, int i, int j, Hashtable <Temp, HashSet <PairI> > defs) {
		LivenessNode node = new LivenessNode(e);
		for (Temp t : node.def)
			kill.addAll(defs.get(t));
		kill.remove(new PairI(i, j));
		if (!node.def.isEmpty())
			gen.add(new PairI(i, j));
	}
}
