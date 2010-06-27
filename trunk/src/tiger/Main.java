package tiger;

import java.io.*;
import java.util.LinkedList;
import tiger.Absyn.*;
import tiger.Blocks.BasicBlock;
import tiger.Mips.MipsFrame;
import tiger.Parser.*;
import tiger.Quadruples.TExp;
import tiger.Semant.*;
import tiger.Translate.Frag;
import tiger.Translate.DataFrag;
import tiger.Translate.ProcFrag;
import tiger.FindEscape.*;

public class Main {
	static boolean DoOpt = true;
	static public void main(String argv[]) {
		try
		{
			//*
			if (argv.length == 0)
				throw new RuntimeException("no file");
			FileReader FileIn = new FileReader(argv[0]);
			if (!argv[0].matches(".+\\.tig")) throw new RuntimeException("Error filename extension");
//			PrintStream outABS = new PrintStream(argv[0].substring(0, argv[0].length() - 3) + "abs");
//			PrintStream outIR = new PrintStream(argv[0].substring(0, argv[0].length() - 3) + "ir");
//			PrintStream outThreeAddr = new PrintStream(argv[0].substring(0, argv[0].length() - 3) + "ta");
//			PrintStream outLiveness = new PrintStream(argv[0].substring(0, argv[0].length() - 3) + "ln");
			PrintStream outAssem = new PrintStream(argv[0].substring(0, argv[0].length() - 3) + "s");
			//*/

			/*
			FileReader FileIn = new FileReader("test.tig");
			PrintStream outABS = new PrintStream("test.abs");
			PrintStream outIR = new PrintStream("test.ir");
			PrintStream outThreeAddr = new PrintStream("test.ta");
			PrintStream outLiveness = new PrintStream("test.ln");
			PrintStream outAssem = new PrintStream("test.s");
			//*/
			parser p = new parser(new Lexer(FileIn));
			
			Exp result = (Exp)p.parse().value;
			
			tiger.Mips.MipsFrame mipsFrame = new tiger.Mips.MipsFrame();
			Semant check = new Semant(mipsFrame);
			check.transProg(result);
			check.printError();
			if (DoOpt) {
				tiger.Optimize.InlineExpansion inlineExpansion = new tiger.Optimize.InlineExpansion();
				inlineExpansion.inlineExpansion(result);
				tiger.Optimize.ConstPropOnAbsyn c = new tiger.Optimize.ConstPropOnAbsyn();
				c.constPropOnAbsyn(result);
			}
			
//			Print print = new Print(outABS);
//			print.prExp(result, 0);

			FindEscape findEscape = new FindEscape();
			findEscape.findEscape(result);
			
			Semant semant = new Semant(mipsFrame);
			Frag frags = semant.transProg(result);

			int count = 0;
			for (Frag it = frags; it != null; it = it.next, count++) {
//				outThreeAddr.println("-----------Frag " + count + "-------------");
//				outIR.println("-----------Frag " + count + "-------------");
				outAssem.println("###############Frag " + count + " ################");
				if (it instanceof ProcFrag)
					emitProc(null, null, null, outAssem, (ProcFrag)it);
//					emitProc(outIR, outThreeAddr, outLiveness, outAssem, (ProcFrag)it);
				else {
//					outIR.println(((DataFrag)it).data);
//					outThreeAddr.println(((DataFrag)it).data);
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
//		PrintStream debug = outIR;
//		PrintStream debug = new PrintStream(new NullOutputStream());
//		tiger.Temp.TempMap tempmap= new tiger.Temp.CombineMap(f.frame,new tiger.Temp.DefaultMap());
//		tiger.Tree.Print print = new tiger.Tree.Print(debug, tempmap);
//		debug.println("# Before canonicalization: ");
//		print.prStm(f.body);
//		debug.println("# After canonicalization: ");
		tiger.Tree.StmList stms = tiger.Canon.Canon.linearize(f.body);
//		prStmList(print,stms);
//		debug.println("# Basic Blocks: ");
		tiger.Canon.BasicBlocks b = new tiger.Canon.BasicBlocks(stms);
//		for(tiger.Canon.StmListList l = b.blocks; l!=null; l=l.tail) {
//			debug.println("#");
//			prStmList(print,l.head);
//		}
//		print.prStm(new tiger.Tree.LABEL(b.done));
//		debug.println("# Trace Scheduled: ");
		tiger.Tree.StmList traced = (new tiger.Canon.TraceSchedule(b)).stms;
//		prStmList(print,traced);
		if (DoOpt)
			tiger.Optimize.ConstFolding.constantFolding(traced);
		tiger.Quadruples.Quadruples threeAddr = new tiger.Quadruples.Quadruples((MipsFrame)f.frame);
		threeAddr.codegen(traced);
		threeAddr.instrList = ((MipsFrame)f.frame).procEntryExit2(threeAddr.instrList);
		tiger.Blocks.BuildBlocks buildBlocks = new tiger.Blocks.BuildBlocks(threeAddr.instrList);
		//optimization
		if (DoOpt)
			tiger.Optimize.LocalConstProp.localConstProp(buildBlocks.blocks);
		//*
//		tiger.Quadruples.Print printTA = new tiger.Quadruples.Print(outThreeAddr);
//		printTA.print(buildBlocks.blocks);
		if (DoOpt)
			while (
				tiger.Optimize.LocalCSE.localCSE(buildBlocks.blocks)
				|| tiger.Optimize.LocalCopyProp.localCopyProp(buildBlocks.blocks)
				)
				tiger.Optimize.DeadCodeElimination.deadCodeEliminiation(buildBlocks.blocks)
				;
		//*/
		//liveness & register allocate
		tiger.RegAlloc.RegAlloc regAlloc = new tiger.RegAlloc.RegAlloc((MipsFrame)f.frame, buildBlocks.blocks, f.frame.registers());
		regAlloc.main();
		//regAlloc.print(outThreeAddr);
		LinkedList<TExp> instrList = new LinkedList<TExp> ();
		for (BasicBlock blk : regAlloc.blocks)
			instrList.addAll(blk.list);
		instrList = ((MipsFrame)f.frame).procEntryExit3(instrList);
		if (DoOpt)
			tiger.Optimize.ReduceBranch.reduceBranch(instrList, regAlloc.temp2Node);
		tiger.Codegen.Codegen codegen = new tiger.Codegen.Codegen(instrList, regAlloc.temp2Node, (MipsFrame)f.frame);
		codegen.codegen(outAssem);
	}
	
	static void prStmList(tiger.Tree.Print print, tiger.Tree.StmList stms) {
		for(tiger.Tree.StmList l = stms; l!=null; l=l.tail)
			print.prStm(l.head);
	}
	class NullOutputStream extends OutputStream {
		public void write(int b) {}
	}
}
