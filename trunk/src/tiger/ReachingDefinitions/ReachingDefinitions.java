package tiger.ReachingDefinitions;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;
import tiger.Blocks.BasicBlock;
import tiger.Liveness.LivenessNode;
import tiger.Quadruples.*;
import tiger.Temp.Temp;

public class ReachingDefinitions {
	public LinkedList<BasicBlock> blocks;
	
	public Hashtable <TempPairI, HashSet<PairI> > DU = new Hashtable<TempPairI, HashSet<PairI>>(),
	UD = new Hashtable<TempPairI, HashSet<PairI>>();
	
	Hashtable <Temp, HashSet <PairI> > defs = new Hashtable <Temp, HashSet <PairI> >();
	
	public ReachingDefinitions(LinkedList<BasicBlock> blk) {
		blocks = blk;
		//build defs, defs(t) = the expressions that define t
		for (int i = 0; i < blocks.size(); i++) {
			BasicBlock b = blocks.get(i);
			for (int j = 0; j < b.list.size(); j++) {
				TExp inst = b.list.get(j);
				LivenessNode node = inst.livenessNode = new LivenessNode(inst);
				for (Temp t : node.def) {
					if (defs.get(t) == null)
						defs.put(t, new HashSet<PairI> ());
					defs.get(t).add(new PairI(i, j));
				}
			}
		}
		//calculate gen and kill
		for (int i = 0; i < blocks.size(); i++) {
			BasicBlock b = blocks.get(i);
			b.reach_in.clear();
			b.reach_out.clear();
			b.reach_gen.clear();
			b.reach_kill.clear();
			for (int j = 0; j < b.list.size(); j++) {
				TExp inst = b.list.get(j);
				Node node = inst.reachingDefinitionsNode = new Node(inst, i, j, defs);
				b.reach_gen.removeAll(node.kill);
				b.reach_gen.addAll(node.gen);
				b.reach_kill.addAll(node.kill);
			}
		}
	}
	
	public void reachingDefinitions() {
		//data flow: reaching definitions
		Queue <BasicBlock> queue = new LinkedList<BasicBlock>();
		for (BasicBlock b : blocks)
			queue.add(b);
		do {
			BasicBlock b = queue.poll();
			HashSet <PairI> old = new HashSet <PairI> (b.reach_out);
			b.reach_in = new HashSet<PairI>();
			for (BasicBlock p : b.pred)
				b.reach_in.addAll(p.reach_out);
			b.reach_out = new HashSet <PairI> (b.reach_in);
			b.reach_out.removeAll(b.reach_kill);
			b.reach_out.addAll(b.reach_gen);
			if (!b.reach_out.equals(old))
				for (BasicBlock s : b.succ)
					queue.add(s);
		} while (!queue.isEmpty());
		//build DU & UD chain
		for (int i = 0; i < blocks.size(); i++) {
			BasicBlock b = blocks.get(i);
			HashSet <PairI> in = new HashSet<PairI> (b.reach_in);
			for (int j = 0; j < b.list.size(); j++) {
				TExp inst = b.list.get(j);
				LivenessNode node = inst.livenessNode;
				PairI q = new PairI(i, j);
				for (Temp t : node.use)
					for (PairI p : in)
						if (blocks.get(p.i).list.get(p.j).livenessNode.def.contains(t)) {
							setOf(DU, t, p).add(q);
							setOf(UD, t, q).add(p);
						}
				in.removeAll(inst.reachingDefinitionsNode.kill);
				in.addAll(inst.reachingDefinitionsNode.gen);
			}
		}
	}

	private HashSet<PairI> setOf(Hashtable<TempPairI, HashSet<PairI>> chain, Temp t, PairI p) {
		TempPairI key = new TempPairI(t, p);
		if (chain.get(key) == null)
			chain.put(key, new HashSet<PairI>());
		return chain.get(key);
	}
}
