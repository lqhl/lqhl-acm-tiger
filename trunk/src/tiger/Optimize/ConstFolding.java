package tiger.Optimize;

import tiger.Tree.BINOP;
import tiger.Tree.CALL;
import tiger.Tree.CJUMP;
import tiger.Tree.CONST;
import tiger.Tree.EXP;
import tiger.Tree.ExpList;
import tiger.Tree.Expr;
import tiger.Tree.JUMP;
import tiger.Tree.LABEL;
import tiger.Tree.MEM;
import tiger.Tree.MOVE;
import tiger.Tree.NAME;
import tiger.Tree.Stm;
import tiger.Tree.StmList;
import tiger.Tree.TEMP;

public class ConstFolding {
	static void transStm(Stm stm) {
		if (stm instanceof CJUMP)
			transStm((CJUMP)stm);
		else if (stm instanceof EXP)
			transStm((EXP)stm);
		else if (stm instanceof JUMP)
			transStm((JUMP)stm);
		else if (stm instanceof LABEL)
			transStm((LABEL)stm);
		else if (stm instanceof MOVE)
			transStm((MOVE)stm);
		else
			throw new RuntimeException("Error at transStm in ConstFolding");
	}
	
	static void transStm(CJUMP stm) {
		stm.left = transExpr(stm.left);
		stm.right = transExpr(stm.right);
	}
	
	static void transStm(EXP stm) {
		stm.exp = transExpr(stm.exp);
	}
	
	static void transStm(JUMP stm) {
	}
	
	static void transStm(LABEL stm) {
	}
	
	static void transStm(MOVE stm) {
		stm.dst = transExpr(stm.dst);
		stm.src = transExpr(stm.src);
	}
	
	static Expr transExpr(Expr expr) {
		if (expr instanceof BINOP)
			return transExpr((BINOP)expr);
		else if (expr instanceof CALL)
			return transExpr((CALL)expr);
		else if (expr instanceof CONST)
			return transExpr((CONST)expr);
		else if (expr instanceof MEM)
			return transExpr((MEM)expr);
		else if (expr instanceof NAME)
			return transExpr((NAME)expr);
		else if (expr instanceof TEMP)
			return transExpr((TEMP)expr);
		else
			throw new RuntimeException("Error at transStm in ConstFolding");
	}
	
	static Expr transExpr(BINOP expr) {
		expr.left = transExpr(expr.left);
		expr.right = transExpr(expr.right);
		if (expr.left instanceof CONST && expr.right instanceof CONST) {
			int left = ((CONST)expr.left).value, right = ((CONST)expr.right).value;
			switch (expr.binop) {
				case BINOP.PLUS: return new CONST(left + right);
				case BINOP.MINUS: return new CONST(left - right);
				case BINOP.MUL: return new CONST(left * right);
				case BINOP.DIV:
					if (right != 0) return new CONST(left / right);
				default: return expr;
			}
		}
		else if (expr.left instanceof CONST) {
			int left = ((CONST)expr.left).value;
			if (left == 0)
				if (expr.binop == BINOP.PLUS)
					return expr.right;
				else if (expr.binop == BINOP.MUL)
					return new CONST(0);
				else {
					expr.left = new TEMP(tiger.Mips.MipsFrame.Reg[0]);
					return expr;
				}
			else if (left == 1)
				if (expr.binop == BINOP.MUL)
					return expr.right;
				else
					return expr;
		}
		else if (expr.right instanceof CONST) {
			int right = ((CONST)expr.right).value;
			if (right == 0)
				if (expr.binop == BINOP.PLUS || expr.binop == BINOP.MINUS)
					return expr.left;
				else if (expr.binop == BINOP.MUL)
					return new CONST(0);
				else {
					expr.right = new TEMP(tiger.Mips.MipsFrame.Reg[0]);
					return expr;
				}
			else if (right == 1)
				if (expr.binop == BINOP.MUL || expr.binop == BINOP.DIV)
					return expr.left;
				else
					return expr;
		}
		return expr;
	}
	
	static Expr transExpr(CONST expr) {
		return expr;
	}
	
	static Expr transExpr(MEM expr) {
		expr.exp = transExpr(expr.exp);
		return expr;
	}
	
	static Expr transExpr(NAME expr) {
		return expr;
	}
	
	static Expr transExpr(TEMP expr) {
		return expr;
	}
	
	static Expr transExpr(CALL expr) {
		for (ExpList el = expr.args; el != null; el = el.tail)
			el.head = transExpr(el.head);
		return expr;
	}

	public static void constantFolding(StmList stml) {
		for (StmList it = stml; it != null; it = it.tail)
			transStm(it.head);
	}
}
