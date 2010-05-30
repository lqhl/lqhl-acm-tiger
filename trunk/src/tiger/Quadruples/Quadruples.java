package tiger.Quadruples;

import java.util.ArrayList;
import java.util.HashMap;

import tiger.Frame.*;
import tiger.Mips.*;
import tiger.Temp.*;
import tiger.Tree.*;

public class Quadruples {
	public ArrayList <TExp> instrList = new ArrayList <TExp> ();
	
	MipsFrame frame;
	
	public Quadruples(MipsFrame frame) {
		this.frame = frame;
	}
	
	public void codegen(StmList stml) {
		for (StmList it = stml; it != null; it = it.tail)
			transStm(it.head);
	}
	
	void transStm(Stm stm) {
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
			System.err.println("Error in IR to TA: Stm");
	}
	
	void transStm(CJUMP stm) {
		if (stm.left instanceof CONST) {
			Temp r = transExpr(stm.right);
			if (stm.relop == CJUMP.EQ || stm.relop == CJUMP.NE)
				instrList.add(new CJumpI(stm.relop, r, ((CONST)stm.left).value, new Label(stm.iftrue)));
			else
				instrList.add(new CJumpI(CJUMP.notRel(stm.relop), r, ((CONST)stm.left).value, new Label(stm.iftrue)));
		}
		else if (stm.right instanceof CONST) {
			Temp l = transExpr(stm.left);
			instrList.add(new CJumpI(stm.relop, l, ((CONST)stm.right).value, new Label(stm.iftrue)));
		}
		else {
			Temp l = transExpr(stm.left);
			Temp r = transExpr(stm.right);
			instrList.add(new CJump(stm.relop, l, r, new Label(stm.iftrue)));
		}
	}
	
	void transStm(EXP stm) {
		transExpr(stm.exp);
	}
	
	void transStm(JUMP stm) {
		instrList.add(new Jump(new Label(stm.targets.head)));
	}
	
	void transStm(LABEL stm) {
		Label l = new Label(stm.label, instrList.size());
		instrList.add(l);
	}
	
	void transStm(MOVE stm) {
		if (stm.dst instanceof TEMP) {
			if (stm.src instanceof CONST)
				instrList.add(new MoveI(((TEMP)stm.dst).temp, ((CONST)stm.src).value));
			else if (stm.src instanceof MEM) {
				Temp mem;
				int offset;
				if (((MEM)stm.src).exp instanceof BINOP &&
					((BINOP)((MEM)stm.src).exp).right instanceof CONST) {
						mem = transExpr(((BINOP)((MEM)stm.src).exp).left);
						offset = ((CONST)((BINOP)((MEM)stm.src).exp).right).value;
				}
				else {
					mem = transExpr(((MEM)stm.src).exp);
					offset = 0;
				}
				instrList.add(new Load(mem, offset, transExpr(stm.dst)));
			}
			else
				instrList.add(new Move(((TEMP)stm.dst).temp, transExpr(stm.src)));
		}
		else if (stm.dst instanceof MEM) {
			Temp mem;
			int offset;
			if (((MEM)stm.dst).exp instanceof BINOP &&
					((BINOP)((MEM)stm.dst).exp).right instanceof CONST) {
				mem = ((TEMP)((BINOP)((MEM)stm.dst).exp).left).temp;
				offset = ((CONST)((BINOP)((MEM)stm.dst).exp).right).value;
			}
			else {
				mem = transExpr(((MEM)stm.dst).exp);
				offset = 0;
			}
			if (stm.src instanceof CONST)
				instrList.add(new StoreI(mem, offset, ((CONST)stm.src).value));
			else
				instrList.add(new Store(mem, offset, transExpr(stm.src)));
		}
	}
	
	Temp transExpr(Expr expr) {
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
			System.err.println("Error in IR to TA: expr");
		return null;
	}
	
	Temp transExpr(BINOP expr) {
		Temp res = new Temp();
		if (expr.right instanceof CONST)
			instrList.add(new BinOpI_R(expr.binop, res, transExpr(expr.left), ((CONST)expr.right).value));
		else if (expr.left instanceof CONST)
			instrList.add(new BinOpI_L(expr.binop, res, ((CONST)expr.left).value, transExpr(expr.right)));
		else
			instrList.add(new BinOp(expr.binop, res, transExpr(expr.left), transExpr(expr.right)));
		return res;
	}
	
	Temp transExpr(CONST expr) {
		Temp res = new Temp();
		instrList.add(new MoveI(res, expr.value));
		return res;
	}
	
	Temp transExpr(MEM expr) {
		Temp res = new Temp();
		Temp mem;
		int offset;
		if ((expr).exp instanceof BINOP &&
			((BINOP)expr.exp).right instanceof CONST) {
				mem = transExpr(((BINOP)expr.exp).left);
				offset = ((CONST)((BINOP)expr.exp).right).value;
		}
		else {
			mem = transExpr(expr.exp);
			offset = 0;
		}
		instrList.add(new Load(mem, offset, res));
		return res;
	}
	
	Temp transExpr(NAME expr) {
		Temp res = new Temp();
		instrList.add(new MoveLabel(res, new Label(expr.label)));
		return res;
	}
	
	Temp transExpr(TEMP expr) {
		return expr.temp;
	}
	
	Temp transExpr(CALL expr) {
		Call cExp = new Call();
		cExp.name = new Label(((NAME)expr.func).label);
		TempList argList = null, ptr = null;
		for (ExpList it = expr.args; it != null; it = it.tail) {
			Temp arg = transExpr(it.head);
			if (argList == null)
				ptr = argList = new TempList(arg, null);
			else
				ptr = ptr.tail = new TempList(arg, null);
		}
		cExp.param = argList;
		MipsFrame tFrame = null;
		for (MipsFrame it : MipsFrame.allFrames)
			if (cExp.name.label == it.name) {
				tFrame = it;
				break;
			}
		
		Temp[] newTemps = new Temp[4];
		for (int i = 0; i < 4; i++)
			instrList.add(new Move(newTemps[i] = new Temp(), frame.A(i)));
		
		if (tFrame == null) {
			int count = 0;
			for (ptr = cExp.param; ptr != null; ptr = ptr.tail) {
				instrList.add(new Move(frame.A(count), ptr.head));
				count++;
			}
		}
		else {
			int count = 0;
			ptr = cExp.param;
			for (AccessList al = tFrame.formals; al != null; al = al.tail, ptr = ptr.tail)
				if (al.head instanceof InReg) {
					instrList.add(new Move(tFrame.A(count), ptr.head));
					count++;
				}
				else
					instrList.add(new Store(tFrame.SP(), ((InFrame)al.head).offset, ptr.head));
		}
		instrList.add(cExp);
		for (int i = 0; i < 4; i++)
			instrList.add(new Move(frame.A(i), newTemps[i]));
		return frame.RV();
	}
}
