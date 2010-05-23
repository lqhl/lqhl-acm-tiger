package tiger.Tree;
import tiger.Temp.Temp;
import tiger.Temp.Label;
public class MEM extends Expr {
  public Expr exp;
  public MEM(Expr e) {exp=e;}
  public ExpList kids() {return new ExpList(exp,null);}
  public Expr build(ExpList kids) {
    return new MEM(kids.head);
  }
}

