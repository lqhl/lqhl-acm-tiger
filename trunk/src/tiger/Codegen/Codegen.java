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
			out.println("	jr " + "$ra");
		else
			out.println("	j " + "_exit");
	}
	
	private String getColor(Temp t) {
		final String[] map = {"zero", "at", "v0", "v1",
				"a0", "a1", "a2", "a3", "t0", "t1",
				"t2", "t3", "t4", "t5", "t6", "t7",
				"s0", "s1", "s2", "s3", "s4", "s5",
				"s6", "s7", "t8", "t9", "k0", "k1",
				"gp", "sp", "fp", "ra"};
		String res = "$" + map[temp2Node.get(t).color];
		return res;
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
		out.print('\t');
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
		out.print('\t');
		switch (exp.oper) {
			case BINOP.PLUS:
				out.print("add");
				out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + exp.right);
				break;
			case BINOP.MINUS:
				out.print("sub");
				out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + exp.right);
				break;
			case BINOP.MUL:
				if ((exp.right & (exp.right - 1)) == 0) {
					out.print("sll");
					out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + log2(exp.right));
				}
				else {
					out.print("mul");
					out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + exp.right);
				}
				break;
			case BINOP.DIV:
				if ((exp.right & (exp.right - 1)) == 0) {
					out.print("sra");
					out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + log2(exp.right));
				}
				else {
					out.print("div");
					out.println(' ' + getColor(exp.dst) + ", " + getColor(exp.left) + ", " + exp.right);
				}
				break;
			default: throw new RuntimeException("Error at BinOpI_R in Codegen");
		}
	}
	
	private int log2(int right) {
		int res = 0;
		while (1 << res < right)
			res++;
		return res;
	}
	
	void print(Call exp) {
		out.print('\t');
		out.println("jal " + exp.name.label);
		out.print('\t');
		out.println("add $fp, $sp, " + -frame.offset);
	}
	
	void print(CJump exp) {
		out.print('\t');
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
		out.print('\t');
		switch (exp.relop) {
			case CJUMP.EQ: out.print("beq"); break;
			case CJUMP.NE: out.print("bne"); break;
			case CJUMP.LT: out.print("blt"); break;
			case CJUMP.GT: out.print("bgt"); break;
			case CJUMP.LE: out.print("ble"); break;
			case CJUMP.GE: out.print("bge"); break;
			default: throw new RuntimeException("Error at CJumpI in Codegen");
		}
		out.println(' ' + getColor(exp.left) + ", " + exp.right + ", " + exp.label.label);
	}
	
	void print(Jump exp) {
		out.print('\t');
		out.println("j " + exp.label.label);
	}
	
	void print(Label exp) {
		out.println(exp.label + ":");
	}
	
	void print(Load exp) {
		out.print('\t');
		out.println("lw " + getColor(exp.dst) + ", " + exp.offset + '(' + getColor(exp.mem) + ')');
	}
	
	void print(Move exp) {
		if (!getColor(exp.dst).equals(getColor(exp.src))) {
			out.print('\t');
			out.println("move " + getColor(exp.dst) + ", " + getColor(exp.src));
		}
	}
	
	void print(MoveI exp) {
		out.print('\t');
		out.println("li " + getColor(exp.dst) + ", " + exp.src);
	}
	
	void print(MoveLabel exp) {
		out.print('\t');
		out.println("la " + getColor(exp.dst) + ", " + exp.srcLabel.label);
	}

	void print(ReturnSink exp) {
		
	}
	
	void print(Store exp) {
		out.print('\t');
		out.println("sw " + getColor(exp.src) + ", " + exp.offset + '(' + getColor(exp.mem) + ')');
	}
}
