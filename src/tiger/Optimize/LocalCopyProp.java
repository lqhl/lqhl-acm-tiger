package tiger.Optimize;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import tiger.Blocks.BasicBlock;
import tiger.Liveness.LivenessNode;
import tiger.Quadruples.*;
import tiger.Temp.Temp;

public class LocalCopyProp {
	public static boolean localCopyProp(LinkedList <BasicBlock> blocks) {
		boolean change = false;
		for (BasicBlock b : blocks)
			if (optimize(b.list))
				change = true;
		return change;
	}
	
	static boolean change;

	private static boolean optimize(LinkedList<TExp> list) {
		change = false;
		HashSet <TempPair> ACP = new HashSet <TempPair> ();
		for (TExp inst : list) {
			if (inst instanceof BinOp) {
				BinOp ins = (BinOp)inst;
				ins.left = copyValue(ACP, ins.left);
				ins.right = copyValue(ACP, ins.right);
			}
			else if (inst instanceof BinOpI_R) {
				BinOpI_R ins = (BinOpI_R)inst;
				ins.left = copyValue(ACP, ins.left);
			}
			else if (inst instanceof Load) {
				Load ins = (Load)inst;
				ins.mem = copyValue(ACP, ins.mem);
			}
			else if (inst instanceof Store) {
				Store ins = (Store)inst;
				ins.mem = copyValue(ACP, ins.mem);
				ins.src = copyValue(ACP, ins.src);
			}
			else if (inst instanceof CJump) {
				CJump ins = (CJump)inst;
				ins.left = copyValue(ACP, ins.left);
				ins.right = copyValue(ACP, ins.right);
			}
			else if (inst instanceof CJumpI) {
				CJumpI ins = (CJumpI)inst;
				ins.left = copyValue(ACP, ins.left);
			}
			else if (inst instanceof Move) {
				Move ins = (Move)inst;
				ins.src = copyValue(ACP, ins.src);
			}
			HashSet <Temp> def = new LivenessNode(inst).def;
			for (Temp t : def)
				removeACP(ACP, t);
			if (inst instanceof Move)
				ACP.add(new TempPair(((Move) inst).dst, ((Move) inst).src));
		}
		return change;
	}

	private static void removeACP(HashSet<TempPair> ACP, Temp t) {
		for (Iterator<TempPair> it = ACP.iterator(); it.hasNext(); ) {
			TempPair p = it.next();
			if (p.dst == t || p.src == t)
				it.remove();
		}
	}

	private static Temp copyValue(HashSet<TempPair> ACP, Temp t) {
		for (TempPair p : ACP)
			if (p.dst == t) {
				change = true;
				return p.src;
			}
		return t;
	}
}
