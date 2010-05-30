package tiger.Mips;

import java.util.ArrayList;
import java.util.LinkedList;
import tiger.Frame.*;
import tiger.Quadruples.BinOpI_R;
import tiger.Quadruples.EnterFunc;
import tiger.Quadruples.Move;
import tiger.Quadruples.ReturnSink;
import tiger.Quadruples.TExp;
import tiger.Temp.*;
import tiger.Tree.*;
import tiger.Util.BoolList;

public class MipsFrame extends Frame {
	public int offset = 0;
	public static LinkedList <MipsFrame> allFrames = new LinkedList <MipsFrame> ();
	public static final int wordSize = 4;
	public static final Temp Reg [] = new Temp[32];
	static {
		for (int i = 0; i < 32; i++)
			Reg[i] = new Temp();
	}

	public Temp A(int k) {
		if (k < 0 || k > 3) throw new RuntimeException("Register A0-A3: out of range");
		return Reg[4 + k];
	}
	
	public Temp FP() {
		return Reg[30];
	}
 
	public Temp RA() {
		return Reg[31];
	}
 
	public Temp RV() {
		return Reg[2];
	}
 
	public Temp SP() {
		return Reg[29];
	}
 
	public Access allocLocal(boolean escape) {
		if (escape) {
			Access ret = new InFrame(this, offset);
			offset -= wordSize;
			return ret;
		}
		else
			return new InReg();
	}

	public Expr externalCall(String funcName, ExpList args) {
		return new CALL(new NAME(new Label(funcName)), args);
	}
 
	public Frame newFrame(Label name, BoolList formals) {
		MipsFrame ret = new MipsFrame();
		ret.name = name;
		AccessList ptr = null;
		ret.formals = null;
		int count = 0;
		for (BoolList f = formals; f != null; f = f.tail) {
			Access a;
			if (count < 4 && !f.head) {
				a = ret.allocLocal(false);
				count++;
			}
			else
				a = ret.allocLocal(true);
			if (ret.formals == null)
				ptr = ret.formals = new AccessList(a, null);
			else
				ptr = ptr.tail = new AccessList(a, null);
		}
		allFrames.add(ret);
		return ret;
	}
 
	public Stm procEntryExit1(Stm body) {
		Temp newTemp;
		//save s0-s7
		for (int i = 0; i < 8; i++) {
			newTemp = new Temp();
			body = new SEQ(new MOVE(new TEMP(newTemp), new TEMP(Reg[16 + i])), body);
			body = new SEQ(body, new MOVE(new TEMP(Reg[16 + i]), new TEMP(newTemp)));
		}
		//save ra
		newTemp = new Temp();
		body = new SEQ(new MOVE(new TEMP(newTemp), new TEMP(Reg[31])), body);
		body = new SEQ(body, new MOVE(new TEMP(Reg[31]), new TEMP(newTemp)));
		//move a0-a3 to new temps
		int count = 0;
		for (AccessList ptr = formals; ptr != null; ptr = ptr.tail)
			if (ptr.head instanceof InReg) {
				body = new SEQ(new MOVE(ptr.head.exp(null), new TEMP(A(count))), body);
				count++;
			}
		return body;
	}
 
	public ArrayList<TExp> procEntryExit2(ArrayList<TExp> body) {
		body.add(new EnterFunc());
		body.add(new ReturnSink());
		return body;
	}
 
	public LinkedList<TExp> procEntryExit3(LinkedList<TExp> instrList) {
		ArrayList<TExp> pre = new ArrayList <TExp> ();
		pre.add(new tiger.Quadruples.Label(name));
		pre.add(new Move(FP(), SP()));
		pre.add(new BinOpI_R(BINOP.MINUS, SP(), SP(), -(offset + wordSize)));
		instrList.addAll(0, pre);
		instrList.add(new Move(SP(), FP()));
		return instrList;
	}
 
	public LinkedList <Temp> registers() {
		LinkedList <Temp> res = new LinkedList <Temp> ();
		for (int i = 0; i < 32; i++)
			res.add(Reg[i]);
		return res;
	}
 
	public String string(Label label, String value) {
		// TODO MipsFrame string
        String ret = ".data\r\n"; 
        ret = ret + label.toString() + ":";
        ret = ret + ".asciiz \"";
		char c;
		for (int i = 0; i < value.length(); i++) {
			c = value.charAt(i);
			if (c == '\n')
				ret = ret + "\\n";
			else if (c == '\r')
				ret = ret + "\\r";
			else if (c == '\b')
				ret = ret + "\\b";
			else if (c == '\\')
				ret = ret + "\\\\";
			else if (c == '\t')
				ret = ret + "\\t";
			else if (c == '\"')
				ret = ret + "\"";
			else if (c == '\f')
				ret = ret + "\\f";
			else
				ret = ret + c;
		}
        ret = ret + "\"\r\n"; 
        ret = ret + ".text";
        return ret; 
	}
 
	public int wordSize() {
		return wordSize;
	}
 
	public String tempMap(Temp t) {
		// TODO MipsFrame tempMap
		return new String();
	}
}
