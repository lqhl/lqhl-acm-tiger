package tiger.Semant;
import tiger.Symbol.*;
import java.util.*;

public class Env {
	Table vEnv;
	Table tEnv;
	LoopEnv loopEnv;
	public Env() {
		loopEnv = new LoopEnv();
		tiger.Symbol.Symbol s;

		vEnv = new tiger.Symbol.Table();
		tEnv = new tiger.Symbol.Table();

		tEnv.put(sym("int"), new tiger.Types.INT());
		tEnv.put(sym("string"), new tiger.Types.STRING());

		s = sym("print");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")), null), Semant.VOID));

		s = sym("printi");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.VOID));

		s = sym("flush");
		vEnv.put(s, new FunEntry(null, Semant.VOID));

		s = sym("getchar");
		vEnv.put(s, new FunEntry(null, Semant.STRING));

		s = sym("ord");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")), null), Semant.INT));

		s = sym("chr");
		vEnv.put(s, new FunEntry( new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.STRING));

		s = sym("size");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")), null), Semant.INT));

		s = sym("substring");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")),
				new tiger.Types.RECORD(sym("f"), Semant.INT, 
				new tiger.Types.RECORD(sym("n"), Semant.INT, null))), Semant.STRING));

		s = sym("concat");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("s1"), (tiger.Types.STRING)tEnv.get(sym("string")),
				new tiger.Types.RECORD(sym("s2"), (tiger.Types.STRING)tEnv.get(sym("string")), null)),
				Semant.STRING));

		s = sym("not");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.INT));

		s = sym("exit");
		vEnv.put(s, new FunEntry(new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.VOID));
	}

	private tiger.Symbol.Symbol sym(String name) {
		return tiger.Symbol.Symbol.symbol(name);
	}
}
