package tiger.Assem;

public class LABEL extends Instr {
   public tiger.Temp.Label label;

   public LABEL(String a, tiger.Temp.Label l) {
      assem=a; label=l;
   }

   public tiger.Temp.TempList use() {return null;}
   public tiger.Temp.TempList def() {return null;}
   public Targets jumps()     {return null;}

}
