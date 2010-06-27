package tiger.Liveness;

import java.util.LinkedList;
import java.util.HashSet;
import tiger.Quadruples.*;
import tiger.Temp.Temp;
import tiger.Blocks.*;

public class Liveness {
	public LinkedList<BasicBlock> blocks;
	
	public Liveness(LinkedList<BasicBlock> blk) {
		blocks = blk;
		for (BasicBlock b : blocks) {
			HashSet<Temp> tmp = new HashSet<Temp> ();
			for (TExp n : b.list) {
				n.livenessNode = new LivenessNode(n);
				tmp.clear();
				tmp.addAll(n.livenessNode.use);
				tmp.removeAll(b.live_kill);
				b.live_gen.addAll(tmp);
				b.live_kill.addAll(n.livenessNode.def);
			}
		}
	}
	
	public void livenessAnalysis() {
		boolean flag;
		do {
			flag = false;
			for (int i = blocks.size() - 1; i >= 0; i--) {
				BasicBlock b = blocks.get(i);
				HashSet <tiger.Temp.Temp> inTmp = new HashSet <tiger.Temp.Temp> (b.live_in);
				HashSet <tiger.Temp.Temp> outTmp = new HashSet <tiger.Temp.Temp> (b.live_out);
				
				b.live_out = new HashSet <tiger.Temp.Temp>();
				for (BasicBlock s : b.succ)
					b.live_out.addAll(s.live_in);

				b.live_in = new HashSet <tiger.Temp.Temp> (b.live_out);
				b.live_in.removeAll(b.live_kill);
				b.live_in.addAll(b.live_gen);
				
				if (!b.live_in.equals(inTmp) || !b.live_out.equals(outTmp))
					flag = true;
			}
		} while (flag);
	}
}
