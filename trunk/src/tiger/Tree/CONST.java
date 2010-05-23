package tiger.Tree;
import tiger.Temp.Temp;
import tiger.Temp.Label;
public class CONST extends Expr {
  public int value;
  public CONST(int v) {value=v;}
  public ExpList kids() {return null;}
  public Expr build(ExpList kids) {return this;}
}

