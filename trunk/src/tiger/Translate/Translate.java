package tiger.Translate;

import java.util.ArrayList;

import tiger.Tree.*;
import tiger.Frame.*;
import tiger.Temp.*;

public class Translate {
	public Frame frame = null;
	private Frag frags = null;
	
	public Translate(Frame f) {
		frame = f;
	}
	
	public Frag getResult() {
		return frags;
	}
	
	public void addFrag(Frag frag) {
		frag.next = frags;
		frags = frag;
	}

	public Exp transIntExp(int value) {
		return new IntExp(value);
	}
	
	public Exp transNilExp() {
		return new Ex(new CONST(0));
	}
	
	public Exp transStringExp(String value) {
		Label l = new Label();
		addFrag(new DataFrag(l, frame.string(l, value)));
		return new Ex(new NAME(l));
	}
	
	public Exp transVarExp(Exp e) {
		return e;
	}
	
	public Exp transCalcExp(int op, Exp left, Exp right) {
		return new Ex(new BINOP(op, left.unEx(), right.unEx()));
	}
	
	public Exp transStringRelExp(int op, Exp left, Exp right) {
		Expr comp = frame.externalCall("_strcmp", new tiger.Tree.ExpList(left.unEx(), new tiger.Tree.ExpList(right.unEx(), null)));
		return new RelCx(op, new Ex(comp), new Ex(new CONST(0)));
	}
	
	public Exp transOtherRelExp(int op, Exp left, Exp right) {
		return new RelCx(op, left, right);
	}
	
	public Exp transAssignExp(Exp lvalue, Exp e) {
		if (lvalue == null)
			return null;
		return new Nx(new MOVE(lvalue.unEx(), e.unEx()));
	}
	
	public Exp transCallExp(Level home, Level dest, Label name, ArrayList <Exp> argValue) {
		tiger.Tree.ExpList args = null;
		for (int i = argValue.size() - 1; i >= 0; i--)
			args = new tiger.Tree.ExpList(argValue.get(i).unEx(), args);
		Level l = home;
		Expr static_link = new TEMP(l.frame.FP());
		while (dest.parent != l) {
			static_link = l.staticLink().access.exp(static_link);
			l = l.parent;
		}
		if (!name.name.startsWith("_"))
			args = new tiger.Tree.ExpList(static_link, args);
		return new Ex(new CALL(new NAME(name), args));
	}
	
	public Exp transRecordExp(Level home, ArrayList<Exp> field){
		Temp addr = new Temp();
		Expr alloc = home.frame.externalCall("_allocRecord",
				new tiger.Tree.ExpList(new CONST((
				field.size() == 0 ? 1 : field.size())
				* home.frame.wordSize()), null));
		Stm init = new EXP(new CONST(0));
		for(int i = field.size() - 1; i >= 0; i--){
			Expr offset = new BINOP(BINOP.PLUS, 
					new TEMP(addr), new CONST(i * home.frame.wordSize()));
			Expr v = field.get(i).unEx();
			init = new SEQ(new MOVE(new MEM(offset),v), init);
		}
		return new Ex(new ESEQ(new SEQ(new MOVE(new TEMP(addr), alloc), init), new TEMP(addr)));
	}
	
	public Exp transArrayExp(Level home, Exp init, Exp size) {
		Expr alloc = home.frame.externalCall("_initArray", new tiger.Tree.ExpList(size.unEx(), new tiger.Tree.ExpList(init.unEx(), null)));
		return new Ex(alloc);
	}
	
	public Exp transMultiArrayExp(Level home, Exp init, Exp size) {
		Expr alloc = home.frame.externalCall("_malloc",
				new tiger.Tree.ExpList(
				new BINOP(BINOP.MUL, size.unEx(), new CONST(frame.wordSize())),
				null));
		Temp addr = new Temp();
		Access var = home.allocLocal(false);
		Stm initialization = (new ForExp(home, var, new Ex(new CONST(0)),
				new Ex(new BINOP(BINOP.MINUS, size.unEx(), new CONST(1))),
				new Nx(new MOVE(
				new MEM(new BINOP(BINOP.PLUS, new TEMP(addr), new BINOP(BINOP.MUL, var.access.exp(null), new CONST(frame.wordSize())))),
				init.unEx())),
				new Label())).unNx();
		return new Ex(new ESEQ(new SEQ(new MOVE(new TEMP(addr), alloc), initialization), new TEMP(addr)));
	}
	
	public Exp transIfThenElseExp(Exp test, Exp e_then, Exp e_else) {
		return new IfThenElseExp(test, e_then, e_else);
	}
	
	public Exp transWhileExp(Exp test, Exp body, Label done) {
		return new WhileExp(test, body, done);
	}
	
	public Exp transForExp(Level home, Access var, Exp low, Exp high, Exp body, Label done) {
		return new ForExp(home, var, low, high, body, done);
	}
	
	public Exp transBreak(Label done) {
		return new Nx(new JUMP(done));
	}
	
	public Exp transSimpleVar(Access var, Level home) {
		Expr res = new TEMP(home.frame.FP());
		Level l = home;
		while (l != var.home) {
			res = l.staticLink().access.exp(res);
			l = l.parent;
		}
		return new Ex(var.access.exp(res));
	}
	
	public Exp transSubscriptVar(Exp var, Exp index) {
		Expr array_addr = var.unEx();
		Expr array_offset;
		if (index.unEx() instanceof CONST)
			array_offset = new CONST(((CONST)index.unEx()).value * frame.wordSize());
		else
			array_offset = new BINOP(BINOP.MUL, index.unEx(), new CONST(frame.wordSize()));
		return new Ex(new MEM(new BINOP(BINOP.PLUS, array_addr, array_offset)));
	}
	
	public Exp transFieldVar(Exp var, int num) {
		Expr addr = var.unEx();
		Expr offset = new CONST(num * frame.wordSize());
		return new Ex(new MEM(new BINOP(BINOP.PLUS, addr, offset)));
	}

	public Exp transSeqExp(ExpList el, boolean isVOID) {
		if (el == null) return new Ex(new CONST(0));
		if (el.tail == null) return el.head;
		if (el.tail.tail == null)
			if (isVOID)
				return new Nx(new SEQ(el.head.unNx(), el.tail.head.unNx()));
			else
				return new Ex(new ESEQ(el.head.unNx(), el.tail.head.unEx()));
		ExpList ptr = el.tail, prev = el;
		SEQ res = null;
		for (; ptr.tail != null; ptr = ptr.tail) {
			if (res == null)
				res = new SEQ(prev.head.unNx(), ptr.head.unNx());
			else
				res = new SEQ(res, ptr.head.unNx());
		}
		if (isVOID)
			return new Nx(new SEQ(res, ptr.head.unNx()));
		else
			return new Ex(new ESEQ(res, ptr.head.unEx()));
	}

	public Exp transLetExp(ExpList eDec, Exp body, boolean isVOID) {
		if (isVOID)
			return new Nx(new SEQ(transSeqExp(eDec, true).unNx(), body.unNx()));
		else
			return new Ex(new ESEQ(transSeqExp(eDec, true).unNx(), body.unEx()));
	}

	public Exp transNoOp() {
		return new Ex(new CONST(0));
	}

	public void procEntryExit(Level level, Exp body, boolean returnValue)
	{
		Stm stm = null;
		if (returnValue)
			stm = new MOVE(new TEMP(level.frame.RV()), body.unEx());
		else
			stm = body.unNx();
		stm = level.frame.procEntryExit1(stm);
		addFrag(new ProcFrag(stm, level.frame));
	}
}
