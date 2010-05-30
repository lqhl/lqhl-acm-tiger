package tiger.Codegen;

import java.io.PrintStream;
import java.util.Hashtable;
import java.util.LinkedList;
import tiger.Tree.CJUMP;

import tiger.Mips.MipsFrame;
import tiger.Quadruples.*;
import tiger.RegAlloc.Node;
import tiger.Temp.Temp;
import tiger.Tree.BINOP;

public class Codegen {
	LinkedList <TExp> instrList;
	Hashtable <Temp, Node> temp2Node;
	PrintStream out;
	private MipsFrame frame;
	public Codegen(LinkedList <TExp> instrList, Hashtable <Temp, Node> temp2Node, MipsFrame frame) {
		this.instrList = instrList;
		this.temp2Node = temp2Node;
		this.frame = frame;
	}
	
	public void codegen(PrintStream out) {
		this.out = out;
		for (TExp e : instrList)
			print(e);
		if (frame.name.name != "main")
			out.println("jr " + "$31");
		else
			out.println("j " + "exit");
	}
	
	private String getColor(Temp t) {
		String res = "$" + temp2Node.get(t).color;
		return res;
	}
	
	void print(TExp exp) {
		if (exp instanceof BinOp)
			print((BinOp)exp);
		else if (exp instanceof BinOpI_R)
			print((BinOpI_R)exp);
		else if (exp instanceof BinOpI_L)
			print((BinOpI_L)exp);
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
		else if (exp instanceof StoreI)
			print((StoreI)exp);
	}
	
	void print(BinOp exp) {
		switch (exp.oper) {
			case BINOP.PLUS: out.print("add"); break;
			case BINOP.MINUS: out.print("sub"); break;
			case BINOP.MUL: out.print("mul"); break;
			case BINOP.DIV: out.print("div"); break;
		default: out.print(exp.oper);
		}
		out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + getColor(exp.right));
	}

	void print(BinOpI_R exp) {
		switch (exp.oper) {
			case BINOP.PLUS: out.print("add"); break;
			case BINOP.MINUS: out.print("sub"); break;
			default: throw new RuntimeException("Error at BinOpI_R in Codegen");
		}
		out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + exp.right);
	}
	
	void print(BinOpI_L exp) {
		throw new RuntimeException("Error at BinOpI_L in Codegen");
	}
	
	void print(Call exp) {
		out.println("jal " + exp.name.label);
		out.println("add $30, $29, " + -(frame.offset + frame.wordSize()));
	}
	
	void print(CJump exp) {
		switch (exp.relop) {
			case CJUMP.EQ: out.print("beq"); break;
			case CJUMP.NE: out.print("bne"); break;
			case CJUMP.LT: out.print("blt"); break;
			case CJUMP.GT: out.print("bgt"); break;
			case CJUMP.LE: out.print("ble"); break;
			case CJUMP.GE: out.print("bge"); break;
			default: throw new RuntimeException("Error at CJump in Codegen " + exp.relop);
		}
		out.println(' ' + getColor(exp.left) + ", " + getColor(exp.right) + ", " + exp.label.label);
	}
	
	void print(CJumpI exp) {
		switch (exp.relop) {
			case CJUMP.EQ: out.print("beqz"); break;
			case CJUMP.NE: out.print("bnez"); break;
			case CJUMP.LT: out.print("bltz"); break;
			case CJUMP.GT: out.print("bgtz"); break;
			case CJUMP.LE: out.print("blez"); break;
			case CJUMP.GE: out.print("bgez"); break;
			default: throw new RuntimeException("Error at CJumpI in Codegen");
		}
	out.println(' ' + getColor(exp.left) + ", " + exp.label.label);
	}
	
	void print(Jump exp) {
		out.println("j " + exp.label.label);
	}
	
	void print(Label exp) {
		out.println(exp.label + ":");
	}
	
	void print(Load exp) {
		out.println("lw " + getColor(exp.dst) + ", " + exp.offset + '(' + getColor(exp.mem) + ')');
	}
	
	void print(Move exp) {
		out.println("move " + getColor(exp.dst) + ", " + getColor(exp.src));
	}
	
	void print(MoveI exp) {
		out.println("li " + getColor(exp.dst) + ", " + exp.src);
	}
	
	void print(MoveLabel exp) {
		out.println("la " + getColor(exp.dst) + ", " + exp.srcLabel.label);
	}

	void print(ReturnSink exp) {
		
	}
	
	void print(Store exp) {
		out.println("sw " + getColor(exp.src) + ", " + exp.offset + '(' + getColor(exp.mem) + ')');
	}
	
	void print(StoreI exp) {
		throw new RuntimeException("Error at StoreI in Codegen");
	}
}
