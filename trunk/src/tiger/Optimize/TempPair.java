package tiger.Optimize;

import tiger.Temp.Temp;

public class TempPair {
	Temp dst;
	Temp src;
	
	public TempPair(Temp d, Temp s) {
		dst = d;
		src = s;
	}

	public boolean equals(Object obj) {
		if (obj instanceof TempPair)
			return dst.equals(((TempPair) obj).dst) && src.equals(((TempPair) obj).src);
		else
			return false;
	}
	
	public int hashCode() {
		return dst.hashCode() ^ src.hashCode();
	}
}
