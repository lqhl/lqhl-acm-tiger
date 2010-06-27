package tiger.Optimize;

import tiger.Quadruples.*;
import tiger.Temp.Temp;
import tiger.Tree.BINOP;

public class AEBTExp {
	TExp exp;
	Temp tmp = null;
	
	public AEBTExp(TExp inst) {
		exp = inst;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof AEBTExp))
			return false;
		AEBTExp o = (AEBTExp)obj;
		if (exp instanceof BinOp && o.exp instanceof BinOp) {
			BinOp a = (BinOp)exp;
			BinOp b = (BinOp)o.exp;
			if (a.oper == b.oper)
				if (a.oper == BINOP.PLUS || a.oper == BINOP.MUL) {
					if (a.left == b.left && a.right == b.right)
						return true;
					else if (a.left == b.right && a.right == b.left)
						return true;
				}
				else
					if (a.left == b.left && a.right == b.right)
						return true;
		}
		else if (exp instanceof BinOpI_R && o.exp instanceof BinOpI_R) {
			BinOpI_R a = (BinOpI_R)exp;
			BinOpI_R b = (BinOpI_R)o.exp;
			if (a.oper == b.oper && a.left == b.left && a.right == b.right)
				return true;
		}
		else if (exp instanceof Load && o.exp instanceof Load) {
			Load a = (Load)exp;
			Load b = (Load)o.exp;
			if (a.mem == b.mem && a.offset == b.offset)
				return true;
		}
		return false;
	}
	
	public int hashCode() {
		if (exp instanceof BinOp) {
			BinOp e = (BinOp)exp;
			return e.oper ^ e.left.hashCode() ^ e.right.hashCode();
		}
		else if (exp instanceof BinOpI_R) {
			BinOpI_R e = (BinOpI_R)exp;
			return e.oper ^ e.left.hashCode() ^ e.right;
		}
		else if (exp instanceof Load) {
			Load e = (Load)exp;
			return e.mem.hashCode() ^ e.offset;
		}
		else
			return exp.hashCode();
	}
}
