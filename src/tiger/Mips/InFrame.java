package tiger.Mips;

import tiger.Frame.Access;
import tiger.Tree.*;


public class InFrame extends Access {
	private MipsFrame frame;
	public int offset;
	
	InFrame(MipsFrame frame, int offset){
		this.frame = frame;
		this.offset = offset;
	}

	public Expr exp(Expr framePtr) {
		return new MEM(new BINOP(BINOP.PLUS, framePtr, new CONST(offset)));
	}

	public Expr expFromStack(Expr stackPtr) {
		return new MEM(new BINOP(BINOP.PLUS, stackPtr, new CONST(-(frame.offset + frame.wordSize()) + offset)));
	}
}
