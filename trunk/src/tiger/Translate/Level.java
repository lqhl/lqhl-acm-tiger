package tiger.Translate;

import tiger.Temp.*;
import tiger.Util.*;

public class Level {
	public tiger.Frame.Frame frame;
	public Level parent;
	public AccessList formals;
	
	public Access staticLink() {
		return formals.head;
	}
	
	public Level(Level p, Label n, BoolList f) {
		parent = p;
		frame = p.frame.newFrame(n, new BoolList(true, f));
		formals = null;
		AccessList ptr = null;
		for(tiger.Frame.AccessList al = frame.formals; al !=null; al = al.tail)
			if (formals == null)
				ptr = formals = new AccessList(new Access(this, al.head), null);
			else
				ptr = ptr.tail = new AccessList(new Access(this, al.head), null);
	}
	
	public Level(Level p, Label n, BoolList f, boolean stdFunc) {
		parent = p;
		frame = p.frame.newFrame(n, f);
		formals = null;
		AccessList ptr = null;
		for(tiger.Frame.AccessList al = frame.formals; al !=null; al = al.tail)
			if (formals == null)
				ptr = formals = new AccessList(new Access(this, al.head), null);
			else
				ptr = ptr.tail = new AccessList(new Access(this, al.head), null);
	}
	
	public Level (tiger.Frame.Frame f) {
		frame = f;
	}
	
	public Access allocLocal(boolean escape) {
		return new Access(this, frame.allocLocal(escape));
	}
}
