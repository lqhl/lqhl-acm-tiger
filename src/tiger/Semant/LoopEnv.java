package tiger.Semant;

import java.util.Stack;

public class LoopEnv {
	public class loop {}
	Stack <Stack <loop> > s = new Stack <Stack <loop> > ();
	LoopEnv () {
		s.add(new Stack<loop> ());
	}
	public void beginScope() {
		s.add(new Stack<loop> ());
	}
	public void endScope() {
		s.pop();
	}
	public void add() {
		s.peek().add(new loop());
	}
	public void pop() {
		s.peek().pop();
	}
	public boolean canBreak() {
		return (!s.peek().isEmpty());
	}
}
