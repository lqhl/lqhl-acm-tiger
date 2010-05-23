package tiger.Tree;

public class TEMP extends Expr {
  public tiger.Temp.Temp temp;
  public TEMP(tiger.Temp.Temp t) {temp=t;}
  public ExpList kids() {return null;}
  public Expr build(ExpList kids) {return this;}
}

