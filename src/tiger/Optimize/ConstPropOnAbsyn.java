package tiger.Optimize;

import java.util.Hashtable;

import tiger.Absyn.*;
import tiger.Symbol.*;

public class ConstPropOnAbsyn {
	Hashtable<Symbol, Integer> table = new Hashtable<Symbol, Integer>();

	void traverseVar(Var v) {
		if (v == null)
			return;
		else if (v instanceof SimpleVar) {
			
		}
		else if (v instanceof FieldVar) {
			traverseVar(((FieldVar) v).var);
		}
		else if (v instanceof SubscriptVar) {
			traverseVar(((SubscriptVar) v).var);
			traverseExp(((SubscriptVar) v).index);
		}
	}

	void traverseExp(Exp e) {
		if (e == null)
			return;
		else if (e instanceof VarExp)
			traverseVar(((VarExp) e).var);
		else if (e instanceof CallExp) {
			for (ExpList el = ((CallExp) e).args; el != null; el = el.tail)
				traverseExp(el.head);
		}
		else if (e instanceof OpExp) {
			traverseExp(((OpExp) e).left);
			traverseExp(((OpExp) e).right);
		}
		else if (e instanceof RecordExp)
			for (FieldExpList el = ((RecordExp) e).fields; el != null; el = el.tail)
				traverseExp(el.init);
		else if (e instanceof SeqExp)
			for (ExpList el = ((SeqExp) e).list; el != null; el = el.tail)
				traverseExp(el.head);
		else if (e instanceof AssignExp) {
			if (((AssignExp) e).var instanceof SimpleVar)
				if (table.containsKey(((SimpleVar)((AssignExp) e).var).name))
					table.remove(((SimpleVar)((AssignExp) e).var).name);
			traverseVar(((AssignExp) e).var);
			traverseExp(((AssignExp) e).exp);
		}
		else if (e instanceof IfExp) {
			traverseExp(((IfExp) e).test);
			traverseExp(((IfExp) e).thenclause);
			traverseExp(((IfExp) e).elseclause);
		}
		else if (e instanceof WhileExp) {
			traverseExp(((WhileExp) e).test);
			traverseExp(((WhileExp) e).body);
		}
		else if (e instanceof ForExp) {
//			traverseDec(((ForExp) e).var);
			traverseExp(((VarDec)((ForExp) e).var).init);
			traverseExp(((ForExp) e).hi);
			traverseExp(((ForExp) e).body);
		}
		else if (e instanceof LetExp) {
			for (DecList dl = ((LetExp) e).decs; dl != null; dl = dl.tail)
				traverseDec(dl.head);
			traverseExp(((LetExp) e).body);
		}
		else if (e instanceof ArrayExp) {
			traverseExp(((ArrayExp) e).size);
			traverseExp(((ArrayExp) e).init);
		}
	}

	void traverseDec(Dec d) {
		if (d == null)
			return;
		else if (d instanceof VarDec) {
			traverseExp(((VarDec) d).init);
			if (table.containsKey(((VarDec) d).name))
				table.remove(((VarDec) d).name);
			else if (((VarDec) d).init instanceof IntExp)
				table.put(((VarDec) d).name, ((IntExp)((VarDec) d).init).value);
		}
		else if (d instanceof FunctionDec) {
			for (FunctionDec fd = (FunctionDec) d; fd != null; fd = fd.next) {
				for (FieldList fl = (fd).params; fl != null; fl = fl.tail)
					if (table.containsKey(fl.name))
						table.remove(fl.name);
				traverseExp((fd).body);
			}
		}
	}
	
	public void constPropOnAbsyn(Exp e) {
		traverseExp(e);
		e = replaceExp(e);
//		for (Symbol n : table.keySet())
//			System.out.println(n + " " + table.get(n));
	}
	
	void replaceVar(Var v) {
		if (v == null)
			return;
		else if (v instanceof SimpleVar) {
			
		}
		else if (v instanceof FieldVar) {
			replaceVar(((FieldVar) v).var);
		}
		else if (v instanceof SubscriptVar) {
			replaceVar(((SubscriptVar) v).var);
			((SubscriptVar) v).index = replaceExp(((SubscriptVar) v).index);
		}
	}

	Exp replaceExp(Exp e) {
		if (e == null)
			return e;
		else if (e instanceof VarExp) {
			if (((VarExp) e).var instanceof SimpleVar) {
				SimpleVar t = (SimpleVar) ((VarExp) e).var;
				if (table.containsKey(t.name))
					return new IntExp(t.line, t.colume, table.get(t.name));
			}
			else {
				replaceVar(((VarExp) e).var);
				return e;
			}
		}
		else if (e instanceof CallExp) {
			for (ExpList el = ((CallExp) e).args; el != null; el = el.tail)
				el.head = replaceExp(el.head);
			return e;
		}
		else if (e instanceof OpExp) {
			((OpExp) e).left = replaceExp(((OpExp) e).left);
			((OpExp) e).right = replaceExp(((OpExp) e).right);
			return e;
		}
		else if (e instanceof RecordExp) {
			for (FieldExpList el = ((RecordExp) e).fields; el != null; el = el.tail)
				el.init = replaceExp(el.init);
			return e;
		}
		else if (e instanceof SeqExp) {
			for (ExpList el = ((SeqExp) e).list; el != null; el = el.tail)
				el.head = replaceExp(el.head);
			return e;
		}
		else if (e instanceof AssignExp) {
			replaceVar(((AssignExp) e).var);
			((AssignExp) e).exp = replaceExp(((AssignExp) e).exp);
			return e;
		}
		else if (e instanceof IfExp) {
			((IfExp) e).test = replaceExp(((IfExp) e).test);
			((IfExp) e).thenclause = replaceExp(((IfExp) e).thenclause);
			((IfExp) e).elseclause = replaceExp(((IfExp) e).elseclause);
			return e;
		}
		else if (e instanceof WhileExp) {
			((WhileExp) e).test = replaceExp(((WhileExp) e).test);
			((WhileExp) e).body = replaceExp(((WhileExp) e).body);
			return e;
		}
		else if (e instanceof ForExp) {
//			replaceDec(((ForExp) e).var);
			((VarDec)((ForExp) e).var).init = replaceExp(((VarDec)((ForExp) e).var).init);
			((ForExp) e).hi = replaceExp(((ForExp) e).hi);
			((ForExp) e).body = replaceExp(((ForExp) e).body);
			return e;
		}
		else if (e instanceof LetExp) {
			for (DecList dl = ((LetExp) e).decs; dl != null; dl = dl.tail)
				replaceDec(dl.head);
			((LetExp) e).body = replaceExp(((LetExp) e).body);
			return e;
		}
		else if (e instanceof ArrayExp) {
			((ArrayExp) e).size = replaceExp(((ArrayExp) e).size);
			((ArrayExp) e).init = replaceExp(((ArrayExp) e).init);
			return e;
		}
		return e;
	}

	void replaceDec(Dec d) {
		if (d == null)
			return;
		else if (d instanceof VarDec) {
			((VarDec) d).init = replaceExp(((VarDec) d).init);
		}
		else if (d instanceof FunctionDec) {
			for (FunctionDec fd = (FunctionDec) d; fd != null; fd = fd.next) {
				(fd).body = replaceExp((fd).body);
			}
		}
	}
}
