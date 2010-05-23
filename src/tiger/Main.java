package tiger;

import java.io.*;

import tiger.Absyn.*;
import tiger.Parser.*;
import tiger.Semant.*;
import tiger.Translate.DataFrag;
import tiger.Translate.Frag;
import tiger.Translate.ProcFrag;
import tiger.FindEscape.*;

public class Main {
	static public void main(String argv[]) {
		try
		{
//			FileReader FileIn = new FileReader(argv[0]);
//			if (!argv[0].matches(".+\\.tig")) throw new RuntimeException("Error filename extension");
//			java.io.PrintStream outABS = new java.io.PrintStream(argv[0].substring(0, argv[0].length() - 3) + "abs");
//			java.io.PrintStream outIR = new java.io.PrintStream(argv[0].substring(0, argv[0].length() - 3) + "ir"));
//			java.io.PrintStream outThreeAddr = new java.io.PrintStream(argv[0].substring(0, argv[0].length() - 3) + "ta"));
//			java.io.PrintStream outLiveness = new java.io.PrintStream(argv[0].substring(0, argv[0].length() - 3) + "ln"));

			FileReader FileIn = new FileReader("test.tig");
			java.io.PrintStream outABS = new java.io.PrintStream("test.abs");
			java.io.PrintStream outIR = new java.io.PrintStream("test.ir");
			java.io.PrintStream outThreeAddr = new java.io.PrintStream("test.ta");
			java.io.PrintStream outLiveness = new java.io.PrintStream("test.ln");
			
			parser p = new parser(new Lexer(FileIn));
			
			Exp result = (Exp)p.parse().value;

			FindEscape findEscape = new FindEscape(result);
			
			tiger.Mips.MipsFrame mipsFrame = new tiger.Mips.MipsFrame();
			Semant semant = new Semant(mipsFrame);
			Frag frags = semant.transProg(result);
			semant.printError();
			Print print = new Print(outABS);
			print.prExp(result, 0);

			for (Frag it = frags; it != null; it = it.next)
				if (it instanceof ProcFrag)
					emitProc(outIR, outThreeAddr, outLiveness, (ProcFrag)it);
				else
					outIR.println(((DataFrag)it).data);

			System.out.println("Semantic analysis success.");
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void emitProc(PrintStream outIR, PrintStream outThreeAddr, PrintStream outLiveness, ProcFrag f) throws FileNotFoundException {
		java.io.PrintStream debug = outIR;
//		java.io.PrintStream debug = new java.io.PrintStream(new NullOutputStream());
		tiger.Temp.TempMap tempmap= new tiger.Temp.CombineMap(f.frame,new tiger.Temp.DefaultMap());
		tiger.Tree.Print print = new tiger.Tree.Print(debug, tempmap);
		debug.println("# Before canonicalization: ");
		print.prStm(f.body);
		debug.println("# After canonicalization: ");
		tiger.Tree.StmList stms = tiger.Canon.Canon.linearize(f.body);
		prStmList(print,stms);
		debug.println("# Basic Blocks: ");
		tiger.Canon.BasicBlocks b = new tiger.Canon.BasicBlocks(stms);
		for(tiger.Canon.StmListList l = b.blocks; l!=null; l=l.tail) {
			debug.println("#");
			prStmList(print,l.head);
		}
		print.prStm(new tiger.Tree.LABEL(b.done));
		debug.println("# Trace Scheduled: ");
		tiger.Tree.StmList traced = (new tiger.Canon.TraceSchedule(b)).stms;
		prStmList(print,traced);
		tiger.ThreeAddress.ThreeAddress threeAddr = new tiger.ThreeAddress.ThreeAddress();
		threeAddr.codegen(traced);
		tiger.ThreeAddress.Print printTA = new tiger.ThreeAddress.Print(outThreeAddr);
		printTA.print(threeAddr.instrList);
		tiger.Liveness.Liveness liveness = new tiger.Liveness.Liveness(threeAddr.instrList);
		liveness.livenessAnalysis();
		liveness.print(outLiveness);
	}
	
	static void prStmList(tiger.Tree.Print print, tiger.Tree.StmList stms) {
		for(tiger.Tree.StmList l = stms; l!=null; l=l.tail)
			print.prStm(l.head);
	}
	class NullOutputStream extends java.io.OutputStream {
		public void write(int b) {}
	}
}
