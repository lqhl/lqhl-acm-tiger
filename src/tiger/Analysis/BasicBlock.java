package tiger.Analysis;

import java.util.HashSet;
import java.util.LinkedList;
import tiger.Quadruples.TExp;
import tiger.Temp.Temp;

public class BasicBlock {
	public LinkedList <TExp> list = new LinkedList <TExp> ();
	public LinkedList <BasicBlock> pred = new LinkedList <BasicBlock>();
	public LinkedList <BasicBlock> succ = new LinkedList <BasicBlock>();
	
	void addEdge(BasicBlock target) {
		if (target == null) return;
		succ.add(target);
		target.pred.add(this);
	}
	
	public HashSet<Temp> live_kill = new HashSet<Temp> ();
	public HashSet<Temp> live_gen = new HashSet<Temp> ();
	public HashSet<Temp> live_in = new HashSet<Temp> ();
	public HashSet<Temp> live_out = new HashSet<Temp> ();
}
