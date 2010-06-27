package tiger.Optimize;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

import tiger.Quadruples.*;
import tiger.RegAlloc.Node;
import tiger.Temp.Temp;

public class ReduceBranch {
	public static void reduceBranch(LinkedList<TExp> instrList, Hashtable<Temp, Node> temp2Node) {
//		Print print = new Print(System.out);
		if (temp2Node != null)
			for (Iterator<TExp> it = instrList.iterator(); it.hasNext(); ) {
				TExp inst = it.next();
				if (inst instanceof Move)
					if (temp2Node.get(((Move) inst).dst).color == temp2Node.get(((Move) inst).src).color)
						it.remove();
			}
		for (int i = 0; i < instrList.size(); i++)
			if (instrList.get(i) instanceof Jump
				&& i + 1 < instrList.size() && instrList.get(i + 1) instanceof Label) {
				Jump j = (Jump) instrList.get(i);
				tiger.Temp.Label l = ((Label)instrList.get(i + 1)).label;
				if (j.label.label == l)
					instrList.remove(i--);
			}
		Hashtable<tiger.Temp.Label, tiger.Temp.Label> jump = new Hashtable<tiger.Temp.Label, tiger.Temp.Label>();
		for (int i = 0; i < instrList.size(); i++) {
//			print.print(instrList.get(i));
			if (instrList.get(i) instanceof Label
				&& (i + 1 < instrList.size() && instrList.get(i + 1) instanceof Jump)) {
				Label p = (Label) instrList.get(i);
				Jump q = (Jump) instrList.get(i + 1);
				jump.put(p.label, q.label.label);
			}
			if (instrList.get(i) instanceof Label
					&& (i + 1 < instrList.size() && instrList.get(i + 1) instanceof Label)) {
					Label p = (Label) instrList.get(i);
					Label q = (Label) instrList.get(i + 1);
					jump.put(p.label, q.label);
				}
		}
		HashSet<tiger.Temp.Label> used = new HashSet<tiger.Temp.Label>();
		for (TExp inst : instrList)
			if (inst instanceof CJump) {
				if (jump.containsKey(((CJump) inst).label.label))
					((CJump) inst).label.label = jump.get(((CJump) inst).label.label);
				used.add(((CJump) inst).label.label);
			}
			else if (inst instanceof CJumpI) {
				if (jump.containsKey(((CJumpI) inst).label.label))
					((CJumpI) inst).label.label = jump.get(((CJumpI) inst).label.label);
				used.add(((CJumpI) inst).label.label);
			}
			else if (inst instanceof Jump) {
				if (jump.containsKey(((Jump) inst).label.label))
					((Jump) inst).label.label = jump.get(((Jump) inst).label.label);
				used.add(((Jump) inst).label.label);
			}
		for (Iterator<TExp> it = instrList.iterator(); it.hasNext(); ) {
			TExp inst = it.next();
			if (inst instanceof Label)
				if (!used.contains(((Label) inst).label) && ((Label) inst).label.name.startsWith("L"))
					it.remove();
		}
	}
}
