package tiger.Semant;

import tiger.Symbol.*;
import tiger.Temp.*;
import tiger.Translate.*;
import tiger.Util.BoolList;

public class Env {
	Table vEnv;
	Table tEnv;
	LoopEnv loopEnv;
	Level root = null;
	public Env(Level r) {
		root = r;
		loopEnv = new LoopEnv();
		tiger.Symbol.Symbol s;

		vEnv = new tiger.Symbol.Table();
		tEnv = new tiger.Symbol.Table();

		tEnv.put(sym("int"), new tiger.Types.INT());
		tEnv.put(sym("string"), new tiger.Types.STRING());

		s = sym("print");
		Label label = new Label(s);
		Level level = new Level(root, label, new BoolList(false, null));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")), null), Semant.VOID));

		s = sym("printi");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, null));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.VOID));

		s = sym("flush");
		label = new Label(s);
		level = new Level(root, label, null);
		vEnv.put(s, new StdFunEntry(level, label, null, Semant.VOID));

		s = sym("getchar");
		label = new Label(s);
		level = new Level(root, label, null);
		vEnv.put(s, new StdFunEntry(level, label, null, Semant.STRING));

		s = sym("ord");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, null));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")), null), Semant.INT));

		s = sym("chr");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, null));
		vEnv.put(s, new StdFunEntry( level, label, new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.STRING));

		s = sym("size");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, null));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")), null), Semant.INT));

		s = sym("substring");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, new BoolList(false, new BoolList(false, null))));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("s"), (tiger.Types.STRING)tEnv.get(sym("string")),
				new tiger.Types.RECORD(sym("f"), Semant.INT, 
				new tiger.Types.RECORD(sym("n"), Semant.INT, null))), Semant.STRING));

		s = sym("concat");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, new BoolList(false, null)));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("s1"), (tiger.Types.STRING)tEnv.get(sym("string")),
				new tiger.Types.RECORD(sym("s2"), (tiger.Types.STRING)tEnv.get(sym("string")), null)),
				Semant.STRING));

		s = sym("not");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, null));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.INT));

		s = sym("exit");
		label = new Label(s);
		level = new Level(root, label, new BoolList(false, null));
		vEnv.put(s, new StdFunEntry(level, label, new tiger.Types.RECORD(sym("i"), (tiger.Types.INT)tEnv.get(sym("int")), null), Semant.VOID));
	}

	private tiger.Symbol.Symbol sym(String name) {
		return tiger.Symbol.Symbol.symbol(name);
	}
}
