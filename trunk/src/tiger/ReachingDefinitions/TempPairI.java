package tiger.ReachingDefinitions;

import tiger.Temp.Temp;

public class TempPairI {
	Temp t;
	PairI p;
	public TempPairI(Temp t, PairI p) {
		this.t = t;
		this.p = p;
	}
	public boolean equals(Object obj) {
		if (obj instanceof TempPairI)
			return t.equals(((TempPairI) obj).t) && p.equals(((TempPairI) obj).p);
		else
			return false;
	}
	public int hashCode() {
		return t.hashCode() ^ p.hashCode();
	}
}
