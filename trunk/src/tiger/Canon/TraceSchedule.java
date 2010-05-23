package tiger.Canon;

public class TraceSchedule {

  public tiger.Tree.StmList stms;
  BasicBlocks theBlocks;
  java.util.Dictionary table = new java.util.Hashtable();

  tiger.Tree.StmList getLast(tiger.Tree.StmList block) {
     tiger.Tree.StmList l=block;
     while (l.tail.tail!=null)  l=l.tail;
     return l;
  }

  void trace(tiger.Tree.StmList l) {
   for(;;) {
     tiger.Tree.LABEL lab = (tiger.Tree.LABEL)l.head;
     table.remove(lab.label);
     tiger.Tree.StmList last = getLast(l);
     tiger.Tree.Stm s = last.tail.head;
     if (s instanceof tiger.Tree.JUMP) {
	tiger.Tree.JUMP j = (tiger.Tree.JUMP)s;
        tiger.Tree.StmList target = (tiger.Tree.StmList)table.get(j.targets.head);
	if (j.targets.tail==null && target!=null) {
               last.tail=target;
	       l=target;
        }
	else {
	  last.tail.tail=getNext();
	  return;
        }
     }
     else if (s instanceof tiger.Tree.CJUMP) {
	tiger.Tree.CJUMP j = (tiger.Tree.CJUMP)s;
        tiger.Tree.StmList t = (tiger.Tree.StmList)table.get(j.iftrue);
        tiger.Tree.StmList f = (tiger.Tree.StmList)table.get(j.iffalse);
        if (f!=null) {
	  last.tail.tail=f; 
	  l=f;
	}
        else if (t!=null) {
	  last.tail.head=new tiger.Tree.CJUMP(tiger.Tree.CJUMP.notRel(j.relop),
					j.left,j.right,
					j.iffalse,j.iftrue);
	  last.tail.tail=t;
	  l=t;
        }
        else {
      tiger.Temp.Label ff = new tiger.Temp.Label();
	  last.tail.head=new tiger.Tree.CJUMP(j.relop,j.left,j.right,
					j.iftrue,ff);
	  last.tail.tail=new tiger.Tree.StmList(new tiger.Tree.LABEL(ff),
		           new tiger.Tree.StmList(new tiger.Tree.JUMP(j.iffalse),
					    getNext()));
	  return;
        }
     }
     else throw new Error("Bad basic block in TraceSchedule");
    }
  }

  tiger.Tree.StmList getNext() {
      if (theBlocks.blocks==null) 
	return new tiger.Tree.StmList(new tiger.Tree.LABEL(theBlocks.done), null);
      else {
	 tiger.Tree.StmList s = theBlocks.blocks.head;
	 tiger.Tree.LABEL lab = (tiger.Tree.LABEL)s.head;
	 if (table.get(lab.label) != null) {
          trace(s);
	  return s;
         }
         else {
	   theBlocks.blocks = theBlocks.blocks.tail;
           return getNext();
         }
      }
  }

  public TraceSchedule(BasicBlocks b) {
    theBlocks=b;
    for(StmListList l = b.blocks; l!=null; l=l.tail)
       table.put(((tiger.Tree.LABEL)l.head.head).label, l.head);
    stms=getNext();
    table=null;
  }        
}


