package tiger;

import java.io.*;
import java.util.ArrayList;

import tiger.Absyn.*;
import tiger.Mips.MipsFrame;
import tiger.Parser.*;
import tiger.Quadruples.TExp;
import tiger.Semant.*;
import tiger.Translate.Frag;
import tiger.Translate.DataFrag;
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
			java.io.PrintStream outAssem = new java.io.PrintStream("test.s");
			
			parser p = new parser(new Lexer(FileIn));
			
			Exp result = (Exp)p.parse().value;

			FindEscape findEscape = new FindEscape(result);
			
			tiger.Mips.MipsFrame mipsFrame = new tiger.Mips.MipsFrame();
			Semant semant = new Semant(mipsFrame);
			Frag frags = semant.transProg(result);
			semant.printError();
			Print print = new Print(outABS);
			print.prExp(result, 0);

			int count = 0;
			for (Frag it = frags; it != null; it = it.next, count++) {
				outThreeAddr.println("-----------Frag " + count + "-------------");
				outIR.println("-----------Frag " + count + "-------------");
				outAssem.println("###############Frag " + count + "################");
				if (it instanceof ProcFrag)
					emitProc(outIR, outThreeAddr, outLiveness, outAssem, (ProcFrag)it);
				else {
					outIR.println(((DataFrag)it).data);
					outThreeAddr.println(((DataFrag)it).data);
					outAssem.println(((DataFrag)it).data);
				}
			}
			outAssem.println();
			BufferedReader runtime = new BufferedReader(new FileReader("runtime.s"));
			while (runtime.ready())
				outAssem.println(runtime.readLine());
			System.out.println("Compile Success!");
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static void emitProc(PrintStream outIR, PrintStream outThreeAddr, PrintStream outLiveness, PrintStream outAssem, ProcFrag f) throws FileNotFoundException {
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
		tiger.Quadruples.Quadruples threeAddr = new tiger.Quadruples.Quadruples((MipsFrame)f.frame);
		threeAddr.codegen(traced);
		tiger.Quadruples.Print printTA = new tiger.Quadruples.Print(outThreeAddr);
		printTA.print(threeAddr.instrList);
		threeAddr.instrList = ((MipsFrame)f.frame).procEntryExit2(threeAddr.instrList);
		//liveness & register allocate
		tiger.RegAlloc.RegAlloc regAlloc = new tiger.RegAlloc.RegAlloc((MipsFrame)f.frame, threeAddr.instrList, f.frame.registers());
		regAlloc.main();
		regAlloc.reduceMoves();
		//regAlloc.print(outThreeAddr);
		regAlloc.instrList = ((MipsFrame)f.frame).procEntryExit3(regAlloc.instrList);
		tiger.Codegen.Codegen codegen = new tiger.Codegen.Codegen(regAlloc.instrList, regAlloc.temp2Node, (MipsFrame)f.frame);
		codegen.codegen(outAssem);
	}
	
	static void prStmList(tiger.Tree.Print print, tiger.Tree.StmList stms) {
		for(tiger.Tree.StmList l = stms; l!=null; l=l.tail)
			print.prStm(l.head);
	}
	class NullOutputStream extends java.io.OutputStream {
		public void write(int b) {}
	}
}
