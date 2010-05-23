package tiger.Tree;
import tiger.Temp.Temp;
import tiger.Temp.Label;
public class ESEQ extends Expr {
  public Stm stm;
  public Expr exp;
  public ESEQ(Stm s, Expr e) {stm=s; exp=e;}
  public ExpList kids() {throw new Error("kids() not applicable to ESEQ");}
  public Expr build(ExpList kids) {throw new Error("build() not applicable to ESEQ");}
}

