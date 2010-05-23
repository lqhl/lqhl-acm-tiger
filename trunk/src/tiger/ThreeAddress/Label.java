package tiger.ThreeAddress;

public class Label extends TExp {

	tiger.Temp.Label label;
	public int number;

	public Label(tiger.Temp.Label label, int num) {
		this.label = label;
		number = num;
	}

	public Label(tiger.Temp.Label label) {
		this.label = label;
		number = -1;
	}
}
