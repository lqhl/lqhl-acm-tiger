package tiger.RegAlloc;

import java.util.LinkedList;

import tiger.Quadruples.Move;

public class Node {
	tiger.Temp.Temp temp;
	public Node(tiger.Temp.Temp t) {
		temp = t;
	}
	
	LinkedList <Node> adjList = new LinkedList <Node> ();
	int degree = 0;
	LinkedList <Move> moveList = new LinkedList <Move> ();
	public Node alias;
	public Integer color = null;
	public boolean isNew = false;
}
