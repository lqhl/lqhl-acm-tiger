package tiger.Mips;

import tiger.Frame.Access;
import tiger.Temp.Temp;
import tiger.Tree.*;

public class InReg extends Access {
	private Temp reg;
	
	public InReg() {
		reg = new Temp();
	}

	public Expr exp(Expr framePtr) {
		return new TEMP(reg);
	}

	public Expr expFromStack(Expr stackPtr) {
		return new TEMP(reg);
	}
}
