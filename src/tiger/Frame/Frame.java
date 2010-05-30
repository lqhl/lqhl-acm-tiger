package tiger.Frame;

import java.util.ArrayList;
import java.util.LinkedList;
import tiger.Quadruples.TExp;
import tiger.Temp.*;
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
	public abstract LinkedList <Temp>  registers();
	public abstract Expr externalCall(String funcName, ExpList args);
	public abstract Stm procEntryExit1(Stm body);
	public abstract ArrayList<TExp> procEntryExit2(ArrayList<TExp> body);
	public abstract LinkedList<TExp> procEntryExit3(LinkedList<TExp> body);
	public abstract String string(Label label, String values);
	public abstract int wordSize();
}
