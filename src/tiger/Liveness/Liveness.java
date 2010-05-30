package tiger.Liveness;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.HashSet;
import tiger.Quadruples.*;

public class Liveness {
	public LinkedList <LivenessNode> nodeList = new LinkedList<LivenessNode>();
	public LinkedList <TExp> instrList;
	
	public Liveness(LinkedList<TExp> instrList) {
		this.instrList = instrList;
	}
	
	public void livenessAnalysis() {
		buildNodeList();
		
		HashMap <tiger.Temp.Label, Integer> labelMap = new HashMap <tiger.Temp.Label, Integer>();
		for (int i = 0; i < instrList.size(); i++)
			if (instrList.get(i) instanceof Label)
				labelMap.put(((Label)instrList.get(i)).label, i);
		for (int i = 0; i < instrList.size(); i++)
			if (instrList.get(i) instanceof CJump)
				((CJump)instrList.get(i)).label.number = labelMap.get(((CJump)instrList.get(i)).label.label);
			else if (instrList.get(i) instanceof CJumpI)
				((CJumpI)instrList.get(i)).label.number = labelMap.get(((CJumpI)instrList.get(i)).label.label);
			else if (instrList.get(i) instanceof Jump)
				((Jump)instrList.get(i)).label.number = labelMap.get(((Jump)instrList.get(i)).label.label);
		
		for (int i = 0; i < instrList.size(); i++) {
			if (instrList.get(i) instanceof Label) continue;
			if (instrList.get(i) instanceof Jump) {
				instrList.get(i).node.addEdge(findNext(((Jump)instrList.get(i)).label.number));
			}
			else {
				instrList.get(i).node.addEdge(findNext(i));
				if (instrList.get(i) instanceof CJump)
					instrList.get(i).node.addEdge(findNext(((CJump)instrList.get(i)).label.number));
				else if (instrList.get(i) instanceof CJumpI)
					instrList.get(i).node.addEdge(findNext(((CJumpI)instrList.get(i)).label.number));
			}
		}
		
		boolean flag;
		do {
			flag = false;
			for (int i = nodeList.size() - 1; i >= 0; i--) {
				LivenessNode n = nodeList.get(i);
				HashSet <tiger.Temp.Temp> inTmp = new HashSet <tiger.Temp.Temp> (n.in);
				HashSet <tiger.Temp.Temp> outTmp = new HashSet <tiger.Temp.Temp> (n.out);
				
				n.out = new HashSet <tiger.Temp.Temp>();
				for (int j = 0; j < n.succ.size(); j++) {
					n.out.addAll(n.succ.get(j).in);
				}

				n.in = new HashSet <tiger.Temp.Temp> (n.out);
				n.in.removeAll(n.def);
				n.in.addAll(n.use);
				
				if (!n.in.equals(inTmp) || !n.out.equals(outTmp))
					flag = true;
			}
		} while (flag);
	}

	private LivenessNode findNext(int number) {
		for (int i = number + 1; i < instrList.size(); i++) {
			if (! (instrList.get(i) instanceof Label))
				return instrList.get(i).node;
		}
		return null;
	}
	
	public void print(PrintStream out) {
		for (int i = 0; i < nodeList.size(); i++) {
			out.println("node " + i);
			out.print("use:");
			for (tiger.Temp.Temp it : nodeList.get(i).use)
				out.print(" " + it);
			out.println();
			out.print("def:");
			for (tiger.Temp.Temp it : nodeList.get(i).def)
				out.print(" " + it);
			out.println();
			out.print("in:");
			for (tiger.Temp.Temp it : nodeList.get(i).in)
				out.print(" " + it);
			out.println();
			out.print("out:");
			for (tiger.Temp.Temp it : nodeList.get(i).out)
				out.print(" " + it);
			out.println();
		}
	}

	public void buildNodeList() {
		for (int i = 0; i < instrList.size(); i++) {
			LivenessNode n = new LivenessNode(instrList.get(i), i);
			instrList.get(i).node = n;
			nodeList.add(n);
		}
	}
}
