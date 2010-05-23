package tiger.Canon;

public class BasicBlocks {
  public StmListList blocks;
  public tiger.Temp.Label done;

  private StmListList lastBlock;
  private tiger.Tree.StmList lastStm;

  private void addStm(tiger.Tree.Stm s) {
	lastStm = lastStm.tail = new tiger.Tree.StmList(s,null);
  }

  private void doStms(tiger.Tree.StmList l) {
      if (l==null) 
	doStms(new tiger.Tree.StmList(new tiger.Tree.JUMP(done), null));
      else if (l.head instanceof tiger.Tree.JUMP 
	      || l.head instanceof tiger.Tree.CJUMP) {
	addStm(l.head);
	mkBlocks(l.tail);
      } 
      else if (l.head instanceof tiger.Tree.LABEL)
           doStms(new tiger.Tree.StmList(new tiger.Tree.JUMP(((tiger.Tree.LABEL)l.head).label), 
	  			   l));
      else {
	addStm(l.head);
	doStms(l.tail);
      }
  }

  void mkBlocks(tiger.Tree.StmList l) {
     if (l==null) return;
     else if (l.head instanceof tiger.Tree.LABEL) {
	lastStm = new tiger.Tree.StmList(l.head,null);
        if (lastBlock==null)
  	   lastBlock= blocks= new StmListList(lastStm,null);
        else
  	   lastBlock = lastBlock.tail = new StmListList(lastStm,null);
	doStms(l.tail);
     }
     else mkBlocks(new tiger.Tree.StmList(new tiger.Tree.LABEL(new tiger.Temp.Label()), l));
  }
   

  public BasicBlocks(tiger.Tree.StmList stms) {
    done = new tiger.Temp.Label();
    mkBlocks(stms);
  }
}
