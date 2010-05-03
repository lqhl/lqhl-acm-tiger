package tiger;

import java.io.*;
import tiger.Absyn.*;
import tiger.Parser.*;
import tiger.Semant.*;

public class Main {
	static public void main(String argv[]) {
		try
		{
			parser p = new parser(new Lexer(new FileReader(argv[0])));
			if (!argv[0].matches(".+\\.tig")) throw new RuntimeException("Error filename extension");

//			parser p = new parser(new Lexer(new FileReader("test.tig")));
//			Print print = new Print(new java.io.PrintStream("test.abs"));
			
			Exp result = (Exp)p.parse().value;

			Semant semant = new Semant();
			semant.transExp(result);
			semant.printError();

			Print print = new Print(new java.io.PrintStream(argv[0].substring(0, argv[0].length() - 3) + "abs"));
			print.prExp(result, 0);
			System.out.println("Semantic analysis success.");
			System.exit(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
