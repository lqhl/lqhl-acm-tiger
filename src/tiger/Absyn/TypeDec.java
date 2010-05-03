package tiger.Absyn;
import tiger.Symbol.Symbol;
public class TypeDec extends Dec {
   public Symbol name;
   public Ty ty;
   public TypeDec next;
   public TypeDec(int l, int c, Symbol n, Ty t, TypeDec x) {line = l; colume = c; name=n; ty=t; next=x;}
}
