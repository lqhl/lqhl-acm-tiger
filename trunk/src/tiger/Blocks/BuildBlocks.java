package tiger.Blocks;

import java.util.HashMap;
import java.util.LinkedList;
import tiger.Quadruples.*;

public class BuildBlocks {
	public LinkedList<BasicBlock> blocks = new LinkedList<BasicBlock> ();
	
	public BuildBlocks(LinkedList<TExp> instrList) {
		HashMap <tiger.Temp.Label, BasicBlock> label2Block = new HashMap <tiger.Temp.Label, BasicBlock>();
		for (TExp e : instrList) {
			if (e instanceof Label) {
				BasicBlock b = new BasicBlock();
				blocks.add(b);
				label2Block.put(((Label)e).label, b);
			}
			blocks.peekLast().list.add(e);
		}
		
		for (int i = 0; i < blocks.size(); i++) {
			BasicBlock b = blocks.get(i);
			if (b.list.peekLast() instanceof Jump) {
				b.addEdge(label2Block.get(((Jump)b.list.peekLast()).label.label));
			}
			else {
				if (i + 1 < blocks.size())
					b.addEdge(blocks.get(i + 1));
				if (b.list.peekLast() instanceof CJump)
					b.addEdge(label2Block.get(((CJump)b.list.peekLast()).label.label));
				else if (b.list.peekLast() instanceof CJumpI)
					b.addEdge(label2Block.get(((CJumpI)b.list.peekLast()).label.label));
			}
		}
	}
}
