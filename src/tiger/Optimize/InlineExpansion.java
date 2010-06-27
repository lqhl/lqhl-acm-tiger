package tiger.Optimize;

import java.util.HashSet;

import tiger.Absyn.*;
import tiger.Symbol.Symbol;

public class InlineExpansion {
	tiger.Symbol.Table env = new tiger.Symbol.Table();
	static HashSet <tiger.Symbol.Symbol> stdFunc = new HashSet <tiger.Symbol.Symbol> ();
	static {
		tiger.Symbol.Symbol s;
		s = sym("print");
		stdFunc.add(s);
		s = sym("printi");
		stdFunc.add(s);
		s = sym("flush");
		stdFunc.add(s);
		s = sym("getchar");
		stdFunc.add(s);
		s = sym("ord");
		stdFunc.add(s);
		s = sym("chr");
		stdFunc.add(s);
		s = sym("size");
		stdFunc.add(s);
		s = sym("substring");
		stdFunc.add(s);
		s = sym("concat");
		stdFunc.add(s);
		s = sym("not");
		stdFunc.add(s);
		s = sym("exit");
		stdFunc.add(s);
	}
	static private tiger.Symbol.Symbol sym(String name) {
		return tiger.Symbol.Symbol.symbol(name);
	}
	
	public void inlineExpansion(Exp e) {
		replaceExp(e);
		checkExp(e);
		e = inlineExp(e);
	}
	
	Exp inlineExp(Exp e) {
		if (e == null)
			return e;
		else if (e instanceof VarExp) {
			
		}
		else if (e instanceof CallExp) {
			if (env.get(((CallExp) e).func) == null)
				for (ExpList el = ((CallExp) e).args; el != null; el = el.tail)
					el.head = inlineExp(el.head);
			else {
				FunctionDec fun = (FunctionDec) env.get(((CallExp) e).func);
				DecList dec = null, ptr = null;
				FieldList params = fun.params;
				for (ExpList el = ((CallExp) e).args; el != null; el = el.tail, params = params.tail) {
					el.head = inlineExp(el.head);
					VarDec var = new VarDec(e.line, e.colume, params.name, new NameTy(e.line, e.colume, params.typ), el.head);
					if (dec == null)
						ptr = dec = new DecList(var, null);
					else
						ptr = ptr.tail = new DecList(var, null);
				}
				LetExp let = new LetExp(e.line, e.colume, dec, fun.body);
				return let;
			}
		}
		else if (e instanceof OpExp) {
			((OpExp) e).left = inlineExp(((OpExp) e).left);
			((OpExp) e).right = inlineExp(((OpExp) e).right);
		}
		else if (e instanceof RecordExp) {
			for (FieldExpList el = ((RecordExp) e).fields; el != null; el = el.tail)
				el.init = inlineExp(el.init);
		}
		else if (e instanceof SeqExp) {
			for (ExpList el = ((SeqExp) e).list; el != null; el = el.tail)
				el.head = inlineExp(el.head);
		}
		else if (e instanceof AssignExp) {
			((AssignExp) e).exp = inlineExp(((AssignExp) e).exp);
		}
		else if (e instanceof IfExp) {
			((IfExp) e).test = inlineExp(((IfExp) e).test);
			((IfExp) e).thenclause = inlineExp(((IfExp) e).thenclause);
			((IfExp) e).elseclause = inlineExp(((IfExp) e).elseclause);
		}
		else if (e instanceof WhileExp) {
			((WhileExp) e).test = inlineExp(((WhileExp) e).test);
			((WhileExp) e).body = inlineExp(((WhileExp) e).body);
		}
		else if (e instanceof ForExp) {
			env.beginScope();
			inlineDec(((ForExp) e).var);
			((ForExp) e).hi = inlineExp(((ForExp) e).hi);
			((ForExp) e).body = inlineExp(((ForExp) e).body);
			env.endScope();
		}
		else if (e instanceof LetExp) {
			env.beginScope();
			for (DecList dl = ((LetExp) e).decs; dl != null; dl = dl.tail)
				inlineDec(dl.head);
			((LetExp) e).body = inlineExp(((LetExp) e).body);
			env.endScope();
		}
		else if (e instanceof ArrayExp) {
			((ArrayExp) e).size = inlineExp(((ArrayExp) e).size);
			((ArrayExp) e).init = inlineExp(((ArrayExp) e).init);
		}
		return e;
	}

	void inlineDec(Dec d) {
		if (d == null)
			return;
		else if (d instanceof VarDec) {
			((VarDec) d).init = inlineExp(((VarDec) d).init);
		}
		else if (d instanceof FunctionDec) {
			for (FunctionDec fd = (FunctionDec) d; fd != null; fd = fd.next) {
				env.beginScope();
				fd.body = inlineExp(fd.body);
				env.endScope();
				if (fd.inline) env.put(fd.name, fd);
			}
		}
	}
	
	tiger.Symbol.Table checkEnv = new tiger.Symbol.Table();
	
	boolean checkExp(Exp e) {
		if (e == null)
			return true;
		else if (e instanceof VarExp) {
			
		}
		else if (e instanceof CallExp) {
			if (!stdFunc.contains(((CallExp) e).func)) {
				FunctionDec funcDec = (FunctionDec) checkEnv.get(((CallExp) e).func);
				if (!funcDec.inline)
					return false;
			}
			for (ExpList el = ((CallExp) e).args; el != null; el = el.tail)
				if (!checkExp(el.head))
					return false;
		}
		else if (e instanceof OpExp) {
			if (!checkExp(((OpExp) e).left))
				return false;
			if (!checkExp(((OpExp) e).right))
				return false;
		}
		else if (e instanceof RecordExp) {
			for (FieldExpList el = ((RecordExp) e).fields; el != null; el = el.tail)
				if (!checkExp(el.init))
					return false;
		}
		else if (e instanceof SeqExp) {
			for (ExpList el = ((SeqExp) e).list; el != null; el = el.tail)
				if (!checkExp(el.head))
					return false;
		}
		else if (e instanceof AssignExp) {
			if (!checkExp(((AssignExp) e).exp))
				return false;
		}
		else if (e instanceof IfExp) {
			if (!checkExp(((IfExp) e).test))
				return false;
			if (!checkExp(((IfExp) e).thenclause))
				return false;
			if (!checkExp(((IfExp) e).elseclause))
				return false;
		}
		else if (e instanceof WhileExp) {
			if (!checkExp(((WhileExp) e).test))
				return false;
			if (!checkExp(((WhileExp) e).body))
				return false;
		}
		else if (e instanceof ForExp) {
			checkEnv.beginScope();
			if (!checkDec(((ForExp) e).var))
				return false;
			if (!checkExp(((ForExp) e).hi))
				return false;
			if (!checkExp(((ForExp) e).body))
				return false;
			checkEnv.endScope();
		}
		else if (e instanceof LetExp) {
			checkEnv.beginScope();
			for (DecList dl = ((LetExp) e).decs; dl != null; dl = dl.tail)
				if (!checkDec(dl.head))
					return false;
			if (!checkExp(((LetExp) e).body))
				return false;
			checkEnv.endScope();
		}
		else if (e instanceof ArrayExp) {
			if (!checkExp(((ArrayExp) e).size))
				return false;
			if (!checkExp(((ArrayExp) e).init))
				return false;
		}
		return true;
	}

	boolean checkDec(Dec d) {
		if (d == null)
			return true;
		else if (d instanceof VarDec) {
			if (!checkExp(((VarDec) d).init))
				return false;
		}
		else if (d instanceof FunctionDec)
			for (FunctionDec fd = (FunctionDec) d; fd != null; fd = fd.next) {
				checkEnv.put(fd.name, fd);
				checkEnv.beginScope();
				fd.inline = checkExp(fd.body);
				checkEnv.endScope();
			}
		return true;
	}
	
	tiger.Symbol.Table varEnv = new tiger.Symbol.Table();
	static int countVar = 0;
	
	void replaceVar(Var v) {
		if (v == null)
			return;
		else if (v instanceof SimpleVar) {
			Symbol tmp = (tiger.Symbol.Symbol)varEnv.get(((SimpleVar) v).name);
			((SimpleVar) v).name = tmp;
		}
		else if (v instanceof FieldVar) {
			replaceVar(((FieldVar) v).var);
		}
		else if (v instanceof SubscriptVar) {
			replaceVar(((SubscriptVar) v).var);
			replaceExp(((SubscriptVar) v).index);
		}
	}

	void replaceExp(Exp e) {
		if (e == null)
			return;
		else if (e instanceof VarExp) {
			replaceVar(((VarExp) e).var);
		}
		else if (e instanceof CallExp) {
			for (ExpList el = ((CallExp) e).args; el != null; el = el.tail)
				replaceExp(el.head);
		}
		else if (e instanceof OpExp) {
			replaceExp(((OpExp) e).left);
			replaceExp(((OpExp) e).right);
		}
		else if (e instanceof RecordExp) {
			for (FieldExpList el = ((RecordExp) e).fields; el != null; el = el.tail)
				replaceExp(el.init);
		}
		else if (e instanceof SeqExp) {
			for (ExpList el = ((SeqExp) e).list; el != null; el = el.tail)
				replaceExp(el.head);
		}
		else if (e instanceof AssignExp) {
			replaceVar(((AssignExp) e).var);
			replaceExp(((AssignExp) e).exp);
		}
		else if (e instanceof IfExp) {
			replaceExp(((IfExp) e).test);
			replaceExp(((IfExp) e).thenclause);
			replaceExp(((IfExp) e).elseclause);
		}
		else if (e instanceof WhileExp) {
			replaceExp(((WhileExp) e).test);
			replaceExp(((WhileExp) e).body);
		}
		else if (e instanceof ForExp) {
			varEnv.beginScope();
			replaceDec(((ForExp) e).var);
			replaceExp(((ForExp) e).hi);
			replaceExp(((ForExp) e).body);
			varEnv.endScope();
		}
		else if (e instanceof LetExp) {
			varEnv.beginScope();
			for (DecList dl = ((LetExp) e).decs; dl != null; dl = dl.tail)
				replaceDec(dl.head);
			replaceExp(((LetExp) e).body);
			varEnv.endScope();
		}
		else if (e instanceof ArrayExp) {
			replaceExp(((ArrayExp) e).size);
			replaceExp(((ArrayExp) e).init);
		}
	}

	void replaceDec(Dec d) {
		if (d == null)
			return;
		else if (d instanceof VarDec) {
			tiger.Symbol.Symbol rep = sym(((VarDec) d).name.toString() + "_" + countVar++);
			replaceExp(((VarDec) d).init);
			varEnv.put(((VarDec) d).name, rep);
			((VarDec) d).name = rep;
		}
		else if (d instanceof FunctionDec) {
			for (FunctionDec fd = (FunctionDec) d; fd != null; fd = fd.next) {
				varEnv.beginScope();
				for (FieldList fl = (fd).params; fl != null; fl = fl.tail) {
					tiger.Symbol.Symbol rep = sym(fl.name.toString() + "_" + countVar++);
					varEnv.put(fl.name, rep);
					fl.name = rep;
				}
				replaceExp((fd).body);
				varEnv.endScope();
			}
		}
	}
}
