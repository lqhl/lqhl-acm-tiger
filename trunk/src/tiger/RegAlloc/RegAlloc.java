package tiger.RegAlloc;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Stack;
import tiger.Mips.*;
import tiger.Liveness.*;
import tiger.Quadruples.*;
import tiger.Temp.Temp;
import tiger.Tree.BINOP;

public class RegAlloc {
	static final int K = 28;

	private static final int Infinity = 1 << 28;
	
	MipsFrame frame;
	public LinkedList <TExp> instrList;
	LinkedList <LivenessNode> nodeList;
	
	class Edge {
		Node start, target;
		public Edge(Node s, Node t) {
			start = s;
			target = t;
		}
		public boolean equals(Object obj) {
			if (!(obj instanceof Edge)) return false;
			Edge o = (Edge)obj;
			return (o.start.equals(start) && o.target.equals(target));
		}
		public int hashCode() {
			return 37 * start.hashCode() + target.hashCode();
		}
	}
	
	HashSet <Edge> adjSet = new HashSet <Edge> (); 
	
	public Hashtable <Temp, Node> temp2Node = new Hashtable <Temp, Node> ();
	
	LinkedList <Node> precolored = new LinkedList <Node> ();
	LinkedList <Node> initial = new LinkedList <Node> ();
	LinkedList <Node> simplifyWorklist = new LinkedList <Node> (), freezeWorklist = new LinkedList <Node> (),
						spillWorklist = new LinkedList <Node> (), spilledNodes = new LinkedList <Node> (),
						coalescedNodes = new LinkedList <Node> (), coloredNodes = new LinkedList <Node> ();
	Stack <Node> selectStack = new Stack <Node> ();
	
	LinkedList <tiger.Quadruples.Move> coalescedMoves = new LinkedList <tiger.Quadruples.Move> (),
										constrainedMoves = new LinkedList <tiger.Quadruples.Move> (), 
										frozenMoves = new LinkedList <tiger.Quadruples.Move> (),
										worklistMoves = new LinkedList <tiger.Quadruples.Move> (),
										activeMoves = new LinkedList <tiger.Quadruples.Move> ();
	
	public RegAlloc(MipsFrame frame, ArrayList<TExp> instrList, LinkedList <Temp> precolored) {
		this.frame = frame;
		this.instrList = modify(instrList);
		for (int i = 0; i < precolored.size(); i++) {
			this.precolored.add(t2N(precolored.get(i)));
			t2N(precolored.get(i)).color = i;
			t2N(precolored.get(i)).degree = Infinity;
		}
		Liveness liveness = new Liveness(this.instrList);
		liveness.buildNodeList();
		nodeList = liveness.nodeList;
		for (LivenessNode n : nodeList) {
			for (Temp t : n.def)
				if (temp2Node.get(t) == null)
					initial.add(t2N(t));
			for (Temp t : n.use)
				if (temp2Node.get(t) == null)
					initial.add(t2N(t));
			for (Temp t : n.in)
				if (temp2Node.get(t) == null)
					initial.add(t2N(t));
			for (Temp t : n.out)
				if (temp2Node.get(t) == null)
					initial.add(t2N(t));
		}
	}
	
	private LinkedList<TExp> modify(ArrayList<TExp> instrList) {
		LinkedList<TExp> res = new LinkedList<TExp>(instrList);
		for (int i = 0; i < res.size(); i++)
			if (res.get(i) instanceof CJumpI) {
				CJumpI c = (CJumpI)res.get(i);
				Temp newTemp = new Temp();
				res.add(i, new BinOpI_R(BINOP.MINUS, newTemp, c.left, c.right));
				c.left = newTemp;
				c.right = 0;
				i++;
			}
		for (int i = 0; i < res.size(); i++)
			if (res.get(i) instanceof StoreI) {
				StoreI s = (StoreI)res.get(i);
				res.remove(i);
				Temp newTemp = new Temp();
				res.add(i, new MoveI(newTemp, s.src));
				i++;
				res.add(i, new Store(s.mem, s.offset, newTemp));
			}
		for (int i = 0; i < res.size(); i++)
			if (res.get(i) instanceof BinOpI_R) {
				BinOpI_R b = (BinOpI_R)res.get(i);
				if (b.oper == BINOP.PLUS || b.oper == BINOP.MINUS) continue;
				res.remove(i);
				Temp newTemp = new Temp();
				res.add(i, new MoveI(newTemp, b.right));
				i++;
				res.add(i, new BinOp(b.oper, b.dst, b.left, newTemp));
			}
		return res;
	}

	public void main() {
		Liveness liveness = new Liveness(instrList);
		liveness.livenessAnalysis();
		nodeList = liveness.nodeList;
		
		build();
		makeWorklist();
		do {
			if (!simplifyWorklist.isEmpty()) simplify();
			else if (!worklistMoves.isEmpty()) coalesce();
			else if (!freezeWorklist.isEmpty()) freeze();
			else if (!spillWorklist.isEmpty()) selectSpill();
		} while (!simplifyWorklist.isEmpty() || !worklistMoves.isEmpty() ||
				 !freezeWorklist.isEmpty() || !spillWorklist.isEmpty());
		assignColors();
		if (!spilledNodes.isEmpty()) {
			rewriteProgram(spilledNodes);
			main();
		}
	}

	private void rewriteProgram(LinkedList <Node> spilledNodes) {
		LinkedList <Node> newTemps = new LinkedList <Node> (); 
		for (Node v : spilledNodes) {
			InFrame a = (InFrame)frame.allocLocal(true);
			for (int i = 0; i < instrList.size(); i++) {
				if (nodeList.get(i) == null) continue;
				if (nodeList.get(i).use.contains(v.temp)) {
					Temp p = new Temp();
					newTemps.add(t2N(p));
					instrList.add(i, new Load(frame.FP(), a.offset, p));
					nodeList.add(i, null);
					instrList.get(++i).replaceUse(v.temp, p);
				}
				if (nodeList.get(i).def.contains(v.temp)) {
					Temp p = new Temp();
					newTemps.add(t2N(p));
					instrList.add(i + 1, new Store(frame.FP(), a.offset, p));
					nodeList.add(i + 1, null);
					instrList.get(i++).replaceDef(v.temp, p);
				}
			}
		}
		spilledNodes.clear();
		initial = newTemps;
		initial.addAll(coloredNodes);
		initial.addAll(coalescedNodes);
		coloredNodes.clear();
		coalescedNodes.clear();
	}

	private void assignColors() {
		//TODO assignColors
		while (!selectStack.isEmpty()) {
			Node n = selectStack.pop();
			LinkedList <Integer> okColors = new LinkedList <Integer> ();
			for (int i = 2; i <= 25; i++)
				okColors.add(i);
			for (int i = 29; i <= 31; i++)
				okColors.add(i);
			LinkedList <Node> nodes = new LinkedList <Node> (precolored);
			nodes.addAll(coloredNodes);
			for (Node w : n.adjList)
				if (nodes.contains(getAlias(w)))
					okColors.remove(getAlias(w).color);
			if (okColors.isEmpty())
				spilledNodes.add(n);
			else {
				coloredNodes.add(n);
				Integer c = okColors.peek();
				n.color = c;
			}
		}
		for (Node n : coalescedNodes)
			n.color = getAlias(n).color;
	}

	private void selectSpill() {
		// TODO RegAlloc select spill
		Node m = null;
		for (Node n : spillWorklist)
			if (!precolored.contains(n) && (m == null || n.degree < m.degree)) {
				m = n;
				break;
			}
		spillWorklist.remove(m);
		simplifyWorklist.add(m);
		freezeMoves(m);
	}

	private void freeze() {
		Node u = freezeWorklist.poll();
		simplifyWorklist.add(u);
		freezeMoves(u);
	}

	private void freezeMoves(Node u) {
		for (Move m : nodeMoves(u)) {
			Node x = t2N(m.dst);
			Node y = t2N(m.src);
			Node v;
			if (getAlias(y) == getAlias(u))
				v = getAlias(x);
			else
				v = getAlias(y);
			activeMoves.remove(m);
			frozenMoves.add(m);
			if (nodeMoves(v).isEmpty() && v.degree < K) {
				freezeWorklist.remove(v);
				simplifyWorklist.add(v);
			}
		}
	}

	private void coalesce() {
		Move m = worklistMoves.poll();
		Node x = getAlias(t2N(m.dst));
		Node y = getAlias(t2N(m.src));
		Node u, v;
		if (precolored.contains(y)) {
			u = y;
			v = x;
		}
		else {
			u = x;
			v = y;
		}
		LinkedList <Node> nodes = new LinkedList <Node> (adjacent(u));
		nodes.addAll(adjacent(v));
		if (u == v) {
			coalescedMoves.add(m);
			addWorkList(u);
		}
		else if (precolored.contains(v) || adjSet.contains(new Edge(u, v))) {
			constrainedMoves.add(m);
			addWorkList(u);
			addWorkList(v);
		}
		else if (precolored.contains(u) && checkOK(u, v)
				|| !precolored.contains(u) && conservative(nodes)) {
			coalescedMoves.add(m);
			combine(u, v);
			addWorkList(u);	
		}
		else
			activeMoves.add(m);
	}

	private void combine(Node u, Node v) {
		if (freezeWorklist.contains(v))
			freezeWorklist.remove(v);
		else
			spillWorklist.remove(v);
		coalescedNodes.add(v);
		v.alias = u;
		HashSet <Move> tmp = new HashSet <Move> (u.moveList);
		tmp.addAll(v.moveList);
		u.moveList = new LinkedList <Move> (tmp);
		LinkedList<Node> vv = new LinkedList<Node> ();
		vv.add(v);
		enableMoves(vv);
		for (Node t : adjacent(v)) {
			addEdge(t.temp, u.temp);
			decrementDegree(t);
		}
		if (u.degree >= K && freezeWorklist.contains(u)) {
			freezeWorklist.remove(u);
			spillWorklist.add(u);
		}
	}

	private boolean conservative(LinkedList<Node> nodes) {
		int k = 0;
		for (Node n : nodes)
			if (n.degree >= K)
				k++;
		return k < K;
	}

	private boolean checkOK(Node u, Node v) {
		for (Node t : adjacent(v))
			if (!OK(t, u))
				return false;
		return true;
	}

	private boolean OK(Node t, Node r) {
		return t.degree < K || precolored.contains(t) || adjSet.contains(new Edge(t, r));
	}

	private void addWorkList(Node u) {
		if (!precolored.contains(u) && !moveRelated(u) && u.degree < K) {
			freezeWorklist.remove(u);
			simplifyWorklist.add(u);
		}
	}

	private Node getAlias(Node n) {
		if (coalescedNodes.contains(n))
			return getAlias(n.alias);
		else
			return n;
	}

	private void simplify() {
		Node n = simplifyWorklist.poll();
		selectStack.push(n);
		for (Node m : adjacent(n))
			decrementDegree(m);
	}

	private void decrementDegree(Node m) {
		int d = m.degree;
		m.degree = d - 1;
		if (d == K) {
			LinkedList <Node> nodes = new LinkedList <Node> (adjacent(m));
			nodes.add(m);
			enableMoves(nodes);
			spillWorklist.remove(m);
			if (moveRelated(m))
				freezeWorklist.add(m);
			else
				simplifyWorklist.add(m);
		}
	}

	private void enableMoves(LinkedList<Node> nodes) {
		for (Node n : nodes)
			for (Move m : nodeMoves(n))
				if (activeMoves.contains(m)) {
					activeMoves.remove(m);
					worklistMoves.add(m);
				}
	}

	private void makeWorklist() {
		while (!initial.isEmpty()) {
			Node n = initial.poll();
			if (n.degree >= K)
				spillWorklist.add(n);
			else if (moveRelated(n))
				freezeWorklist.add(n);
			else
				simplifyWorklist.add(n);
		}
	}
	
	private boolean moveRelated(Node n) {
		return !nodeMoves(n).isEmpty();
	}

	private LinkedList <Move> nodeMoves(Node n) {
		LinkedList <Move> res = new LinkedList <Move> (activeMoves);
		res.addAll(worklistMoves);
		res.retainAll(n.moveList);
		return res;
	}

	private LinkedList <Node> adjacent(Node n) {
		LinkedList <Node> res = new LinkedList <Node> (n.adjList);
		res.removeAll(selectStack);
		res.removeAll(coalescedNodes);
		return res;
	}

	private void build() {
		for (int i = 0; i < instrList.size(); i++)
			if (instrList.get(i) instanceof CJump
				|| instrList.get(i) instanceof CJumpI
				|| instrList.get(i) instanceof Jump
				|| (i + 1 < instrList.size() && instrList.get(i + 1) instanceof Label)) {
				HashSet <Temp> live = new HashSet<Temp> (nodeList.get(i).out);
				for (int j = i; !(instrList.get(j) instanceof Label); j--) {
					if (instrList.get(j) instanceof Move) {
						live.removeAll(nodeList.get(j).use);
						HashSet <Temp> nodes = new HashSet <Temp> (nodeList.get(j).def);
						nodes.addAll(nodeList.get(j).use);
						for (Temp t : nodes) {
							Node n = t2N(t);
							n.moveList.add((Move)instrList.get(j));
						}
						worklistMoves.add((Move)instrList.get(j));
					}
					live.addAll(nodeList.get(j).def);
					for (Temp d : nodeList.get(j).def)
						for (Temp l : live)
							addEdge(l, d);
					live.removeAll(nodeList.get(j).def);
					live.addAll(nodeList.get(j).use);
				}
			}
	}

	private Node t2N(Temp t) {
		Node n = temp2Node.get(t);
		if (n == null) {
			n = new Node(t);
			temp2Node.put(t, n);
		}
		return n;
	}

	private void addEdge(Temp u, Temp v) {
		Node uu = t2N(u), vv = t2N(v);
		if (u != v && !adjSet.contains(new Edge(uu, vv))) {
			adjSet.add(new Edge(uu, vv));
			adjSet.add(new Edge(vv, uu));
			if (!precolored.contains(u)) {
				uu.adjList.add(vv);
				uu.degree++;
			}
			if (!precolored.contains(v)) {
				vv.adjList.add(uu);
				vv.degree++;
			}
		}
	}
	
	public void print(PrintStream out) {
		for (Node n : coloredNodes)
			out.println(n.temp + ": $" + n.color);
	}

	public void reduceMoves() {
		for (int i = 0; i < instrList.size(); i++) {
			if (instrList.get(i) instanceof Move) {
				Move n = (Move)instrList.get(i);
				if (t2N(n.dst).color == t2N(n.src).color) {
					instrList.remove(i);
					i--;
				}
			}
		}
	}
}
