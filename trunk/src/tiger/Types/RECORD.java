package tiger.Types;

public class RECORD extends Type { //parameter
   public tiger.Symbol.Symbol fieldName;
   public Type fieldType;
   public RECORD tail;
   public RECORD(tiger.Symbol.Symbol n, Type t, RECORD x) {
       fieldName=n; fieldType=t; tail=x;
   }
   public boolean coerceTo(Type t) {
	return this==t.actual();
   }
}
   

