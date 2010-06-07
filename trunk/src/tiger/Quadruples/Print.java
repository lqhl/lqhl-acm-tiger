package tiger.Quadruples;

import java.io.PrintStream;
import java.util.LinkedList;

import tiger.Temp.TempList;
import tiger.Tree.BINOP;

public class Print {
	PrintStream out;
	public Print(PrintStream o) {out = o;}
	public void print(LinkedList<TExp> instrList) {
		for (TExp it : instrList)
			print(it);
	}
	
	void print(TExp exp) {
		if (exp instanceof BinOp)
			print((BinOp)exp);
		else if (exp instanceof BinOpI_R)
			print((BinOpI_R)exp);
		else if (exp instanceof Call)
			print((Call)exp);
		else if (exp instanceof CJump)
			print((CJump)exp);
		else if (exp instanceof CJumpI)
			print((CJumpI)exp);
		else if (exp instanceof Jump)
			print((Jump)exp);
		else if (exp instanceof Label)
			print((Label)exp);
		else if (exp instanceof Load)
			print((Load)exp);
		else if (exp instanceof Move)
			print((Move)exp);
		else if (exp instanceof MoveI)
			print((MoveI)exp);
		else if (exp instanceof MoveLabel)
			print((MoveLabel)exp);
		else if (exp instanceof ReturnSink)
			print((ReturnSink)exp);
		else if (exp instanceof Store)
			print((Store)exp);
	}
	
	void print(BinOp exp) {
		out.print("BinOp ");
		switch (exp.oper) {
			case BINOP.PLUS: out.print('+'); break;
			case BINOP.MINUS: out.print('-'); break;
			case BINOP.MUL: out.print('*'); break;
			case BINOP.DIV: out.print('/'); break;
		default: out.print(exp.oper);
		}
		out.println(' ' + exp.dst.toString() + ' ' + exp.left + ' ' + exp.right);
	}

	void print(BinOpI_R exp) {
		out.print("BinOp ");
		switch (exp.oper) {
			case BINOP.PLUS: out.print('+'); break;
			case BINOP.MINUS: out.print('-'); break;
			case BINOP.MUL: out.print('*'); break;
			case BINOP.DIV: out.print('/'); break;
		default: out.print(exp.oper);
		}
		out.println(' ' + exp.dst.toString() + ' ' + exp.left + ' ' + exp.right);
	}
	
	void print(Call exp) {
		out.print("Call " + exp.name.label + '(');
		for (TempList it = exp.param; it != null; it = it.tail) {
			out.print(it.head);
			if (it.tail != null) out.print(", ");
		}
		out.println(')');
	}
	
	void print(CJump exp) {
		out.println("CJump " + exp.relop + ' ' + exp.left + ' ' + exp.right + ' ' + exp.label.label);
	}
	
	void print(CJumpI exp) {
		out.println("CJumpI " + exp.relop + ' ' + exp.left + ' ' + exp.right + ' ' + exp.label.label);
	}
	
	void print(Jump exp) {
		out.println("Jump " + exp.label.label);
	}
	
	void print(Label exp) {
		out.println("Label " + exp.label);
	}
	
	void print(Load exp) {
		out.println("Load " + exp.dst + ' ' + exp.mem + ' ' + exp.offset);
	}
	
	void print(Move exp) {
		out.println("Move " + exp.dst + ' ' + exp.src);
	}
	
	void print(MoveI exp) {
		out.println("MoveI " + exp.dst + ' ' + exp.src);
	}
	
	void print(MoveLabel exp) {
		out.println("MoveLabel " + exp.dst + ' ' + exp.srcLabel.label);
	}

	void print(ReturnSink exp) {
		out.println("ReturnSink");
	}
	
	void print(Store exp) {
		out.println("Store " + exp.src + ' ' + exp.mem + ' ' + exp.offset);
	}
}
