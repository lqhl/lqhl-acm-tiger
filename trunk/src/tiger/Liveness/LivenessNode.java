package tiger.Liveness;

import java.util.HashSet;
import tiger.Mips.MipsFrame;
import tiger.Quadruples.*;

public class LivenessNode {
	public HashSet <tiger.Temp.Temp> use = new HashSet <tiger.Temp.Temp> ();
	public HashSet <tiger.Temp.Temp> def = new HashSet <tiger.Temp.Temp> ();
	
	public LivenessNode(TExp k) {
		if (k instanceof BinOp) {
			use.add(((BinOp)k).left);
			use.add(((BinOp)k).right);
			def.add(((BinOp)k).dst);
		}
		else if (k instanceof BinOpI_R) {
			use.add(((BinOpI_R)k).left);
			def.add(((BinOpI_R)k).dst);
		}
		else if (k instanceof Call) {
			//use: a0-a3
			for (int i = 0; i < 4; i++)
				use.add(MipsFrame.Reg[4 + i]);
			//define: v0, v1
			def.add(MipsFrame.Reg[2]);
			def.add(MipsFrame.Reg[3]);
			//define: a0-a3
			for (int i = 0; i < 4; i++)
				def.add(MipsFrame.Reg[4 + i]);
			//define: t0-t9
			for (int i = 0; i < 8; i++)
				def.add(MipsFrame.Reg[8 + i]);
			def.add(MipsFrame.Reg[24]);
			def.add(MipsFrame.Reg[25]);
			//define: ra
			def.add(MipsFrame.Reg[31]);
			//define: gp
			def.add(MipsFrame.Reg[28]);
		}
		else if (k instanceof CJump) {
			use.add(((CJump)k).left);
			use.add(((CJump)k).right);
		}
		else if (k instanceof CJumpI) {
			use.add(((CJumpI)k).left);
		}
		else if (k instanceof Jump) {
			
		}
		else if (k instanceof Label) {
			
		}
		else if (k instanceof Load) {
			use.add(((Load)k).mem);
			def.add(((Load)k).dst);
		}
		else if (k instanceof Move) {
			use.add(((Move)k).src);
			def.add(((Move)k).dst);
		}
		else if (k instanceof MoveI) {
			def.add(((MoveI)k).dst);
		}
		else if (k instanceof MoveLabel) {
			def.add(((MoveLabel)k).dst);
		}
		else if (k instanceof ReturnSink) {
			//v0
			use.add(MipsFrame.Reg[2]);
			//sp
			use.add(MipsFrame.Reg[29]);
			//fp
			use.add(MipsFrame.Reg[30]);
			//ra
			use.add(MipsFrame.Reg[31]);
			//s0-s7
			for (int i = 0; i < 8; i++)
				use.add(MipsFrame.Reg[16 + i]);
		}
		else if (k instanceof Store) {
			use.add(((Store)k).mem);
			use.add(((Store)k).src);
		}
	}
}
