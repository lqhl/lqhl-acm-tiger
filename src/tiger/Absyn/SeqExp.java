package tiger.Absyn;
import tiger.Symbol.Symbol;
public class SeqExp extends Exp {
   public ExpList list;
   public SeqExp(int line_t, int c, ExpList l) {line = line_t; colume = c; list=l;}
}
