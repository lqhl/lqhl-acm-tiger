package tiger.Assem;

public class MOVE extends Instr {
   public tiger.Temp.Temp dst;   
   public tiger.Temp.Temp src;

   public MOVE(String a, tiger.Temp.Temp d, tiger.Temp.Temp s) {
      assem=a; dst=d; src=s;
   }
   public tiger.Temp.TempList use() {return new tiger.Temp.TempList(src,null);}
   public tiger.Temp.TempList def() {return new tiger.Temp.TempList(dst,null);}
   public Targets jumps()     {return null;}

}
