package tiger.Canon;

class MoveCall extends tiger.Tree.Stm {
  tiger.Tree.TEMP dst;
  tiger.Tree.CALL src;
  MoveCall(tiger.Tree.TEMP d, tiger.Tree.CALL s) {dst=d; src=s;}
  public tiger.Tree.ExpList kids() {return src.kids();}
  public tiger.Tree.Stm build(tiger.Tree.ExpList kids) {
	return new tiger.Tree.MOVE(dst, src.build(kids));
  }
}   
  
class ExpCall extends tiger.Tree.Stm {
  tiger.Tree.CALL call;
  ExpCall(tiger.Tree.CALL c) {call=c;}
  public tiger.Tree.ExpList kids() {return call.kids();}
  public tiger.Tree.Stm build(tiger.Tree.ExpList kids) {
	return new tiger.Tree.EXP(call.build(kids));
  }
}   
  
class StmExpList {
  tiger.Tree.Stm stm;
  tiger.Tree.ExpList exps;
  StmExpList(tiger.Tree.Stm s, tiger.Tree.ExpList e) {stm=s; exps=e;}
}

public class Canon {
  
 static boolean isNop(tiger.Tree.Stm a) {
   return a instanceof tiger.Tree.EXP
          && ((tiger.Tree.EXP)a).exp instanceof tiger.Tree.CONST;
 }

 static tiger.Tree.Stm seq(tiger.Tree.Stm a, tiger.Tree.Stm b) {
    if (isNop(a)) return b;
    else if (isNop(b)) return a;
    else return new tiger.Tree.SEQ(a,b);
 }

 static boolean commute(tiger.Tree.Stm a, tiger.Tree.Expr b) {
    return isNop(a)
        || b instanceof tiger.Tree.NAME
        || b instanceof tiger.Tree.CONST;
 }

 static tiger.Tree.Stm do_stm(tiger.Tree.SEQ s) { 
	return seq(do_stm(s.left), do_stm(s.right));
 }

 static tiger.Tree.Stm do_stm(tiger.Tree.MOVE s) { 
	if (s.dst instanceof tiger.Tree.TEMP 
	     && s.src instanceof tiger.Tree.CALL) 
		return reorder_stm(new MoveCall((tiger.Tree.TEMP)s.dst,
						(tiger.Tree.CALL)s.src));
	else if (s.dst instanceof tiger.Tree.ESEQ)
	    return do_stm(new tiger.Tree.SEQ(((tiger.Tree.ESEQ)s.dst).stm,
					new tiger.Tree.MOVE(((tiger.Tree.ESEQ)s.dst).exp,
						  s.src)));
	else return reorder_stm(s);
 }

 static tiger.Tree.Stm do_stm(tiger.Tree.EXP s) { 
	if (s.exp instanceof tiger.Tree.CALL)
	       return reorder_stm(new ExpCall((tiger.Tree.CALL)s.exp));
	else return reorder_stm(s);
 }

 static tiger.Tree.Stm do_stm(tiger.Tree.Stm s) {
     if (s instanceof tiger.Tree.SEQ) return do_stm((tiger.Tree.SEQ)s);
     else if (s instanceof tiger.Tree.MOVE) return do_stm((tiger.Tree.MOVE)s);
     else if (s instanceof tiger.Tree.EXP) return do_stm((tiger.Tree.EXP)s);
     else return reorder_stm(s);
 }

 static tiger.Tree.Stm reorder_stm(tiger.Tree.Stm s) {
     StmExpList x = reorder(s.kids());
     return seq(x.stm, s.build(x.exps));
 }

 static tiger.Tree.ESEQ do_exp(tiger.Tree.ESEQ e) {
      tiger.Tree.Stm stms = do_stm(e.stm);
      tiger.Tree.ESEQ b = do_exp(e.exp);
      return new tiger.Tree.ESEQ(seq(stms,b.stm), b.exp);
  }

 static tiger.Tree.ESEQ do_exp (tiger.Tree.Expr e) {
       if (e instanceof tiger.Tree.ESEQ) return do_exp((tiger.Tree.ESEQ)e);
       else return reorder_exp(e);
 }
         
 static tiger.Tree.ESEQ reorder_exp (tiger.Tree.Expr e) {
     StmExpList x = reorder(e.kids());
     return new tiger.Tree.ESEQ(x.stm, e.build(x.exps));
 }

 static StmExpList nopNull = new StmExpList(new tiger.Tree.EXP(new tiger.Tree.CONST(0)),null);

 static StmExpList reorder(tiger.Tree.ExpList exps) {
     if (exps==null) return nopNull;
     else {
       tiger.Tree.Expr a = exps.head;
       if (a instanceof tiger.Tree.CALL) {
    	  tiger.Temp.Temp t = new tiger.Temp.Temp();
	 tiger.Tree.Expr e = new tiger.Tree.ESEQ(new tiger.Tree.MOVE(new tiger.Tree.TEMP(t), a),
				    new tiger.Tree.TEMP(t));
         return reorder(new tiger.Tree.ExpList(e, exps.tail));
       } else {
	 tiger.Tree.ESEQ aa = do_exp(a);
	 StmExpList bb = reorder(exps.tail);
	 if (commute(bb.stm, aa.exp))
	      return new StmExpList(seq(aa.stm,bb.stm), 
				    new tiger.Tree.ExpList(aa.exp,bb.exps));
	 else {
	   tiger.Temp.Temp t = new tiger.Temp.Temp();
	   return new StmExpList(
			  seq(aa.stm, 
			    seq(new tiger.Tree.MOVE(new tiger.Tree.TEMP(t),aa.exp),
				 bb.stm)),
			  new tiger.Tree.ExpList(new tiger.Tree.TEMP(t), bb.exps));
	 }
       }
     }
 }
        
 static tiger.Tree.StmList linear(tiger.Tree.SEQ s, tiger.Tree.StmList l) {
      return linear(s.left,linear(s.right,l));
 }
 static tiger.Tree.StmList linear(tiger.Tree.Stm s, tiger.Tree.StmList l) {
    if (s instanceof tiger.Tree.SEQ) return linear((tiger.Tree.SEQ)s, l);
    else return new tiger.Tree.StmList(s,l);
 }

 static public tiger.Tree.StmList linearize(tiger.Tree.Stm s) {
    return linear(do_stm(s), null);
 }
}
