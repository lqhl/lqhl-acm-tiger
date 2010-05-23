package tiger.Translate;

import tiger.Tree.*;
import tiger.Frame.*;

public class ProcFrag extends Frag {
	public Stm body = null;
	public Frame frame = null;
	public ProcFrag(Stm b, Frame f) {
		body = b;
		frame = f;
	}
}
