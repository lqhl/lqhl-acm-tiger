package tiger.Optimize;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import tiger.Blocks.BasicBlock;
import tiger.Liveness.Liveness;
import tiger.Quadruples.*;
import tiger.ReachingDefinitions.*;
import tiger.Temp.Temp;

public class DeadCodeElimination {
	public static void deadCodeEliminiation(LinkedList<BasicBlock> blocks){
		//*
		Liveness liveness = new Liveness(blocks);
		liveness.livenessAnalysis();
		HashSet<Temp> precolored = new HashSet<Temp>();
		for (int i =  0; i < 32; i++)
			precolored.add(tiger.Mips.MipsFrame.Reg[i]);
		for (BasicBlock b : blocks) {
			HashSet <Temp> live = new HashSet<Temp>(b.live_out);
			for (int i = b.list.size() - 1; i >= 0; i--) {
				TExp inst = b.list.get(i);
				if (inst instanceof Move || inst instanceof MoveI
					|| inst instanceof BinOp || inst instanceof BinOpI_R
					|| inst instanceof Load) {
					inst.mark = false;
					for (Temp t : inst.livenessNode.def)
						if (live.contains(t)) {
							inst.mark = true;
							break;
						}
				}
				else
					inst.mark = true;
				live.removeAll(inst.livenessNode.def);
				live.addAll(inst.livenessNode.use);
			}
		}
		//*/
		
//		ReachingDefinitions reachingDefinitions = new ReachingDefinitions(blocks);
//		reachingDefinitions.reachingDefinitions();
		
		/*
		HashSet<Temp> precolored = new HashSet<Temp>();
//		for (int i =  0; i < 32; i++)
//			precolored.add(tiger.Mips.MipsFrame.Reg[i]);
		
		for (int i = 0; i < blocks.size(); i++) {
			BasicBlock b = blocks.get(i);
			for (int j = 0; j < b.list.size(); j++) {
				TExp inst = b.list.get(j);
				if (inst instanceof Move || inst instanceof MoveI
					|| inst instanceof BinOp || inst instanceof BinOpI_R
					|| inst instanceof Load) {
					inst.mark = false;
					for (Temp t : inst.livenessNode.def) {
						if (precolored.contains(t)) {
							inst.mark = true;
							break;
						}
						HashSet<PairI> du = reachingDefinitions.DU.get(new TempPairI(t, new PairI(i, j)));
						if (du == null) continue;
						if (!du.isEmpty()) {
							inst.mark = true;
							break;
						}
					}
				}
				else
					inst.mark = true;
			}
		}
		//*/
		
		/*
		Queue <PairI> worklist = new LinkedList<PairI>();

		HashSet<Temp> precolored = new HashSet<Temp>();
		for (int i =  0; i < 32; i++)
			precolored.add(tiger.Mips.MipsFrame.Reg[i]);
		
		for (int i = 0; i < blocks.size(); i++) {
			BasicBlock b = blocks.get(i);
			for (int j = 0; j < b.list.size(); j++) {
				TExp inst = b.list.get(j);
				if (inst instanceof Call || inst instanceof ReturnSink
					|| inst instanceof Label || inst instanceof Jump
					|| inst instanceof CJump || inst instanceof CJumpI
					|| inst instanceof Store
					|| (inst instanceof Move
						&& (precolored.contains(((Move)inst).src) || precolored.contains(((Move)inst).dst)))) {
					inst.mark = true;
					worklist.add(new PairI(i, j));
				}
			}
		}
		
		while (!worklist.isEmpty()) {
			PairI x = worklist.poll();
			TExp inst = blocks.get(x.i).list.get(x.j);
			for (Temp v : inst.livenessNode.use) {
				HashSet<PairI> ud = reachingDefinitions.UD.get(new TempPairI(v, x));
				if (ud == null) continue;
				for (PairI y : ud)
					if (!blocks.get(y.i).list.get(y.j).mark) {
						blocks.get(y.i).list.get(y.j).mark = true;
						worklist.add(y);
					}
			}
			for (Temp left : inst.livenessNode.def) {
				HashSet<PairI> du = reachingDefinitions.DU.get(new TempPairI(left, x));
				if (du == null) continue;
				for (PairI y : du)
					if (!blocks.get(y.i).list.get(y.j).mark
						&& (blocks.get(y.i).list.get(y.j) instanceof CJump
						|| blocks.get(y.i).list.get(y.j) instanceof CJumpI)) {
						blocks.get(y.i).list.get(y.j).mark = true;
						worklist.add(y);
					}
			}
		}
		//*/
		for (BasicBlock b : blocks)
			for (Iterator<TExp> it = b.list.iterator(); it.hasNext(); ) {
				TExp inst = it.next();
				if (!inst.mark)
					it.remove();
			}
	}
}
