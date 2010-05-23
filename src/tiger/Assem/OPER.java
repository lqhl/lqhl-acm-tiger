package tiger.Assem;

public class OPER extends Instr {
   public tiger.Temp.TempList dst;   
   public tiger.Temp.TempList src;
   public Targets jump;

   public OPER(String a, tiger.Temp.TempList d, tiger.Temp.TempList s, tiger.Temp.LabelList j) {
      assem=a; dst=d; src=s; jump=new Targets(j);
   }
   public OPER(String a, tiger.Temp.TempList d, tiger.Temp.TempList s) {
      assem=a; dst=d; src=s; jump=null;
   }

   public tiger.Temp.TempList use() {return src;}
   public tiger.Temp.TempList def() {return dst;}
   public Targets jumps() {return jump;}

}
