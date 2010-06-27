package tiger.Optimize;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import tiger.Blocks.BasicBlock;
import tiger.Liveness.LivenessNode;
import tiger.Quadruples.*;
import tiger.Temp.Temp;

public class LocalCSE {
	public static boolean localCSE(LinkedList <BasicBlock> blocks) {
		boolean change = false;
		for (BasicBlock b : blocks)
			if (optimize(b.list))
				change = true;
		return change;
	}
	
	static boolean optimize(LinkedList <TExp> list) {
		boolean change = false;
		LinkedHashSet <AEBTExp> AEB = new LinkedHashSet <AEBTExp>();
		for (int i = 0; i < list.size(); i++) {
			TExp ins = list.get(i);
			AEBTExp item = new AEBTExp(ins);
			boolean found = false;
			for (AEBTExp aeb : AEB)
				if (aeb.equals(item)) {
					found = true;
					if (ins instanceof BinOp) {
						BinOp inst = (BinOp)ins;
						if (aeb.tmp == null) {
							aeb.tmp = new Temp();
							Temp origin = ((BinOp)aeb.exp).dst;
							((BinOp)aeb.exp).dst = aeb.tmp;
							list.add(list.indexOf(aeb.exp) + 1, new Move(origin, aeb.tmp));
							origin = inst.dst;
							list.remove(++i);
							list.add(i, new Move(origin, aeb.tmp));
						}
						else {
							Temp origin = inst.dst;
							list.remove(i);
							list.add(i, new Move(origin, aeb.tmp));
						}
					}
					else if (ins instanceof BinOpI_R) {
						BinOpI_R inst = (BinOpI_R)ins;
						if (aeb.tmp == null) {
							aeb.tmp = new Temp();
							Temp origin = ((BinOpI_R)aeb.exp).dst;
							((BinOpI_R)aeb.exp).dst = aeb.tmp;
							list.add(list.indexOf(aeb.exp) + 1, new Move(origin, aeb.tmp));
							origin = inst.dst;
							list.remove(++i);
							list.add(i, new Move(origin, aeb.tmp));
						}
						else {
							Temp origin = inst.dst;
							list.remove(i);
							list.add(i, new Move(origin, aeb.tmp));
						}
					}
					else if (ins instanceof Load) {
						Load inst = (Load)ins;
						if (aeb.tmp == null) {
							aeb.tmp = new Temp();
							Temp origin = ((Load)aeb.exp).dst;
							((Load)aeb.exp).dst = aeb.tmp;
							list.add(list.indexOf(aeb.exp) + 1, new Move(origin, aeb.tmp));
							origin = inst.dst;
							list.remove(++i);
							list.add(i, new Move(origin, aeb.tmp));
						}
						else {
							Temp origin = inst.dst;
							list.remove(i);
							list.add(i, new Move(origin, aeb.tmp));
						}
					}
					break;
				}
			if (found)
				change = true;
			else
				AEB.add(item);
			HashSet <Temp> def = new LivenessNode(ins).def;
			for (Temp t : def)
				removeOperand(AEB, t);
			if (ins instanceof Store) {
				Store inst = (Store)ins;
				if (inst.mem == tiger.Mips.MipsFrame.Reg[30] || inst.mem == tiger.Mips.MipsFrame.Reg[29]) {
					for (Iterator<AEBTExp> it = AEB.iterator(); it.hasNext();) {
						TExp p = it.next().exp;
						if (p instanceof Load && ((Load)p).mem == inst.mem && ((Load)p).offset == inst.offset)
							it.remove();
					}
				}
				else {
					for (Iterator<AEBTExp> it = AEB.iterator(); it.hasNext();) {
						TExp p = it.next().exp;
						if (p instanceof Load && ((Load)p).mem != tiger.Mips.MipsFrame.Reg[30] && ((Load)p).mem != tiger.Mips.MipsFrame.Reg[29])
							it.remove();
					}
				}
			}
			else if (ins instanceof Call) {
				for (Iterator<AEBTExp> it = AEB.iterator(); it.hasNext();) {
					TExp p = it.next().exp;
					if (p instanceof Load)
						it.remove();
				}
			}
		}
		return change;
	}

	private static void removeOperand(LinkedHashSet<AEBTExp> AEB, Temp dst) {
		for (Iterator<AEBTExp> it = AEB.iterator(); it.hasNext();) {
			AEBTExp aeb = it.next();
			HashSet <Temp> use = new LivenessNode(aeb.exp).use;
			if (use.contains(dst))
				it.remove();
		}
	}
}
