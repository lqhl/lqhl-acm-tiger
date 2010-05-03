package tiger.Absyn;
import tiger.Symbol.Symbol;
public class FieldExpList extends Absyn {
   public Symbol name;
   public Exp init;
   public FieldExpList tail;
   public FieldExpList(int l, int c, Symbol n, Exp i, FieldExpList t) {line = l; colume = c; 
	name=n; init=i; tail=t;
   }
}
