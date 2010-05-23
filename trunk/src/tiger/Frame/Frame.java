package tiger.Frame;

import tiger.Temp.*;
import tiger.Assem.*;
import tiger.Tree.*;
import tiger.Util.BoolList;

public abstract class Frame implements TempMap {
	public Label name;
	public AccessList formals = null;
	public abstract Frame newFrame(Label name, BoolList formals);
	public abstract Access allocLocal(boolean escape);
	public abstract Temp FP();
	public abstract Temp SP();
	public abstract Temp RA();
	public abstract Temp RV();
	public abstract TempList registers();
	public abstract Expr externalCall(String funcName, ExpList args);
	public abstract Stm procEntryExit1(Stm body);
	public abstract InstrList procEntryExit2(InstrList body);
	public abstract InstrList procEntryExit3(InstrList body);
	public abstract String string(Label label, String values);
	public abstract InstrList codegen(Stm s);
	public abstract int wordSize();
}
