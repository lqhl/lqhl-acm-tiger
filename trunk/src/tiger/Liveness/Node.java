package tiger.Liveness;

import java.util.HashSet;
import java.util.LinkedList;

import tiger.ThreeAddress.*;

public class Node {
	TExp key;
	int number;
	
	HashSet <tiger.Temp.Temp> use = new HashSet <tiger.Temp.Temp> ();
	HashSet <tiger.Temp.Temp> def = new HashSet <tiger.Temp.Temp> ();
	HashSet <tiger.Temp.Temp> in = new HashSet <tiger.Temp.Temp> ();
	HashSet <tiger.Temp.Temp> out = new HashSet <tiger.Temp.Temp> ();
	LinkedList <Node> pred = new LinkedList <Node>();
	LinkedList <Node> succ = new LinkedList <Node>();
	
	public Node(TExp k, int n) {
		key = k;
		number = n;
		if (k instanceof BinOp) {
			use.add(((BinOp)k).left);
			use.add(((BinOp)k).right);
			def.add(((BinOp)k).dst);
		}
		else if (k instanceof BinOpI_L) {
			use.add(((BinOpI_L)k).right);
			def.add(((BinOpI_L)k).dst);
		}
		else if (k instanceof BinOpI_R) {
			use.add(((BinOpI_R)k).left);
			def.add(((BinOpI_R)k).dst);
		}
		else if (k instanceof Call) {
			//TODO
		}
		else if (k instanceof CJump) {
			use.add(((CJump)k).left);
			use.add(((CJump)k).right);
		}
		else if (k instanceof CJumpI) {
			use.add(((CJumpI)k).right);
		}
		else if (k instanceof Jump) {
			
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
		else if (k instanceof Store) {
			use.add(((Store)k).mem);
			use.add(((Store)k).src);
		}
		else if (k instanceof StoreI) {
			use.add(((StoreI)k).mem);
		}
	}
	
	void addEdge(Node target) {
		if (target == null) return;
		succ.add(target);
		target.pred.add(this);
	}
}
