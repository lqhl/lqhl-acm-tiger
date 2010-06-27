package tiger.Optimize;

import java.util.Hashtable;
import java.util.LinkedList;

import tiger.Blocks.BasicBlock;
import tiger.Liveness.LivenessNode;
import tiger.Quadruples.*;
import tiger.Temp.Temp;
import tiger.Tree.BINOP;

public class LocalConstProp {
	public static void localConstProp(LinkedList <BasicBlock> blocks) {
		for (BasicBlock b : blocks)
			optimize(b);
	}

	private static void optimize(BasicBlock b) {
		for (int i = 0; i < b.list.size(); i++)
			if (b.list.get(i) instanceof MoveI) {
				Temp old = ((MoveI)b.list.get(i)).dst;
				int value = ((MoveI)b.list.get(i)).src;
				for (int j = i + 1; j < b.list.size(); j++) {
					if (livenessNode(b.list.get(j)).def.contains(old))
						break;
					if (livenessNode(b.list.get(j)).use.contains(old))
						if (b.list.get(j) instanceof Move) {
							Move e = (Move) b.list.get(j);
							b.list.remove(j);
							b.list.add(j, new MoveI(e.dst, value));
						}
						else if (b.list.get(j) instanceof BinOp) {
							BinOp e = (BinOp) b.list.get(j);
							if (e.right != old) continue;
							b.list.remove(j);
							b.list.add(j, new BinOpI_R(e.oper, e.dst, e.left, value));
						}
						else if (b.list.get(j) instanceof BinOpI_R) {
							BinOpI_R e = (BinOpI_R) b.list.get(j);
							if (e.oper == BINOP.DIV && e.right == 0)
								continue;
							b.list.remove(j);
							switch (e.oper) {
							case BINOP.PLUS:
								b.list.add(j, new MoveI(e.dst, value + e.right));
								break;
							case BINOP.MINUS:
								b.list.add(j, new MoveI(e.dst, value - e.right));
								break;
							case BINOP.MUL:
								b.list.add(j, new MoveI(e.dst, value * e.right));
								break;
							case BINOP.DIV:
								b.list.add(j, new MoveI(e.dst, value / e.right));
								break;
							}
						}
				}
			}
	}

	private static LivenessNode livenessNode(TExp e) {
		if (e.livenessNode == null)
			e.livenessNode = new LivenessNode(e);
		return e.livenessNode;
	}
}
