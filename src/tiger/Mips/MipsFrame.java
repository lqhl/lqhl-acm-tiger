package tiger.Mips;

import java.util.ArrayList;

import tiger.Temp.Temp;
import tiger.Temp.TempList;

import tiger.Assem.InstrList;
import tiger.Frame.*;
import tiger.Temp.*;
import tiger.Tree.*;
import tiger.Util.BoolList;

public class MipsFrame extends Frame {
	int frameLength = 0;
	
	public static final int wordSize = 4;
	public static final Temp Reg [] = new Temp[32];
	static {
		for (int i = 0; i < 32; i++)
			Reg[i] = new Temp();
	}
	
	public Temp FP() {
		return Reg[30];
		//TODO
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
			Access ret = new InFrame(this, frameLength);
			frameLength -= wordSize;
			return ret;
		}
		else
			return new InReg();
	}
 
	public InstrList codegen(Stm s) {
		// TODO Auto-generated method stub
		return null;
	}
 
	public Expr externalCall(String funcName, ExpList args) {
		return new CALL(new NAME(new Label(funcName)), args);
	}
 
	public Frame newFrame(Label name, BoolList formals) {
		MipsFrame ret = new MipsFrame();
		ret.name = name;
		AccessList ptr = null;
		ret.formals = null;
		for (BoolList f = formals; f != null; f = f.tail) {//, argReg = argReg.tail) {
			Access a = ret.allocLocal(f.head);
			if (ret.formals == null)
				ptr = ret.formals = new AccessList(a, null);
			else
				ptr = ptr.tail = new AccessList(a, null);
		}
		return ret;
	}
 
	public Stm procEntryExit1(Stm body) {
		return body;
	}
 
	public InstrList procEntryExit2(InstrList body) {
		// TODO Auto-generated method stub
		return null;
	}
 
	public InstrList procEntryExit3(InstrList body) {
		// TODO Auto-generated method stub
		return null;
	}
 
	public TempList registers() {
		// TODO Auto-generated method stub
		return null;
	}
 
	public String string(Label label, String value) {
		// TODO
		return value;
	}
 
	public int wordSize() {
		return wordSize;
	}
 
	public String tempMap(Temp t) {
		// TODO Auto-generated method stub
		return new String();
	}
}
