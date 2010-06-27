package tiger.ReachingDefinitions;

public class PairI {
	public int i;
	public int j;
	public PairI(int a, int b) {
		i = a;
		j = b;
	}
	public boolean equals(Object obj) {
		if (obj instanceof PairI)
			return i == ((PairI)obj).i && j == ((PairI)obj).j;
		else
			return false;
	}
	public int hashCode() {
		return (i << 10) + j;
	}
}
