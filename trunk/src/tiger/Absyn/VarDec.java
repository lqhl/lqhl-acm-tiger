package tiger.Absyn;
import tiger.Symbol.Symbol;
public class VarDec extends Dec {
   public Symbol name;
   public boolean escape = true;
   public NameTy typ; /* optional */
   public Exp init;
   public VarDec(int l, int c, Symbol n, NameTy t, Exp i) {line = l; colume = c; name=n; typ=t; init=i;}
}
