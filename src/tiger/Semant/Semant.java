package tiger.Semant;

import tiger.Absyn.FieldList;
import tiger.Absyn.OpExp;
import tiger.Frame.Frame;
import tiger.Temp.Label;
import tiger.Translate.*;
import tiger.Types.*;
import tiger.Util.BoolList;

import java.util.*;

public class Semant {
	Env env;
	Translate translate;
	Level level;

	static Type INT = new INT();
	static Type STRING = new STRING();
	static Type VOID = new VOID();
	static Type NIL = new NIL();

	public Semant(Frame frame) {
		translate = new Translate(frame);
		level = new Level(translate.frame);
		env = new Env(level);
	}

	List<String> errorList = new ArrayList <String> ();
	
	void report_error(int line, int colume, String message) {
		StringBuffer m = new StringBuffer("Semantic Error");
		if (line >= 0) {
			m.append(" in line " + (line + 1));
			if (colume >= 0) {
				m.append(", colume " + (colume + 1));
			}
		}
		m.append(": " + message);
		errorList.add(m.toString());
	}

	void report_error(String message) {
		report_error(-1, -1, message);
	}

	public void printError() {
		if (errorList.isEmpty())
			return ;
		else {
//			System.err.println("Semantic Error:");
			for(String m : errorList)
				System.err.println(m);
			System.exit(1);
		}
	}
	
	void fatal_error(String message) {
		System.err.println(message);
		System.exit(1);
	}
	
	public Frag transProg(tiger.Absyn.Exp e) {
		level = new Level(level, new Label(tiger.Symbol.Symbol.symbol("main")), null);
		ExpTy et = transExp(e);
		translate.procEntryExit(level, et.exp, false);
		level = level.parent;
		return translate.getResult();
	}
	
	Exp checkInt(ExpTy e, int line, int colume) {
		if (!e.ty.coerceTo(INT)) {
			report_error(line, colume, "Integer required");
		}
		return e.exp;
	}
	
	void checkAssign(Type left, Type right, int line, int colume) {
		if (left.coerceTo(right) && (!right .coerceTo(NIL)))
			return ;
		if (right.coerceTo(NIL) && left instanceof RECORD)
			return ;
		report_error(line, colume, "Imcompatible types at assignment");
	}
	
	ExpTy transVar(tiger.Absyn.Var e) {
		if (e instanceof tiger.Absyn.SimpleVar)
			return transVar((tiger.Absyn.SimpleVar) e);
		if (e instanceof tiger.Absyn.SubscriptVar)
			return transVar((tiger.Absyn.SubscriptVar) e);
		if (e instanceof tiger.Absyn.FieldVar)
			return transVar((tiger.Absyn.FieldVar) e);
		fatal_error("Translating variable");
		return null;
	}

	ExpTy transVar(tiger.Absyn.SimpleVar e) {
		Entry t = (Entry) env.vEnv.get(e.name);
		if (t instanceof VarEntry) {
			VarEntry p = (VarEntry)t;
			return new ExpTy(translate.transSimpleVar(p.access, level), ((VarEntry) t).ty);
		}
		else {
			report_error(e.line, e.colume, "Undefined variable " + e.name.toString());
			return new ExpTy(null, INT);
		}
	}

	ExpTy transVar(tiger.Absyn.SubscriptVar e) {
		ExpTy base = transVar(e.var);
		ExpTy index = transExp(e.index);
		if (base.ty instanceof ARRAY) {
			checkInt(index, e.index.line, e.index.colume);
			return new ExpTy(translate.transSubscriptVar(base.exp, index.exp), ((ARRAY)base.ty).element.actual());
		}
		else {
			report_error(e.line, e.colume, "Array type required");
			return new ExpTy(null, INT);
		}
	}

	ExpTy transVar(tiger.Absyn.FieldVar e) {
		ExpTy l = transVar(e.var);
		int offset = 0;
		if (l.ty instanceof RECORD) {
			RECORD rec;
			for (rec = (RECORD)l.ty; rec != null; rec = rec.tail, offset++)
				if (rec.fieldName == e.field)
					break;
			if (rec == null) {
				report_error(e.line, e.colume, "Field " + e.field.toString() + " does not exist");
				return new ExpTy(null, INT);
			}
			else
				return new ExpTy(translate.transFieldVar(l.exp, offset), rec.fieldType.actual());
		}
		else {
			report_error(e.line, e.colume, "Record type required");
			return new ExpTy(null, INT);
		}
	}

	public ExpTy transExp(tiger.Absyn.Exp e) {
		if (e instanceof tiger.Absyn.ArrayExp)
			return transExp((tiger.Absyn.ArrayExp) e);
		if (e instanceof tiger.Absyn.AssignExp)
			return transExp((tiger.Absyn.AssignExp) e);
		if (e instanceof tiger.Absyn.BreakExp)
			return transExp((tiger.Absyn.BreakExp) e);
		if (e instanceof tiger.Absyn.CallExp)
			return transExp((tiger.Absyn.CallExp) e);
		if (e instanceof tiger.Absyn.ForExp)
			return transExp((tiger.Absyn.ForExp) e);
		if (e instanceof tiger.Absyn.IfExp)
			return transExp((tiger.Absyn.IfExp) e);
		if (e instanceof tiger.Absyn.IntExp)
			return transExp((tiger.Absyn.IntExp) e);
		if (e instanceof tiger.Absyn.LetExp)
			return transExp((tiger.Absyn.LetExp) e);
		if (e instanceof tiger.Absyn.NilExp)
			return transExp((tiger.Absyn.NilExp) e);
		if (e instanceof tiger.Absyn.OpExp)
			return transExp((tiger.Absyn.OpExp) e);
		if (e instanceof tiger.Absyn.RecordExp)
			return transExp((tiger.Absyn.RecordExp) e);
		if (e instanceof tiger.Absyn.SeqExp)
			return transExp((tiger.Absyn.SeqExp) e);
		if (e instanceof tiger.Absyn.StringExp)
			return transExp((tiger.Absyn.StringExp) e);
		if (e instanceof tiger.Absyn.VarExp)
			return transExp((tiger.Absyn.VarExp) e);
		if (e instanceof tiger.Absyn.WhileExp)
			return transExp((tiger.Absyn.WhileExp) e);
		fatal_error("Translating expression");
		return null;
	}

	ExpTy transExp(tiger.Absyn.ArrayExp e) {
		Type eType = (Type)env.tEnv.get(e.typ);
		if (eType == null) {
			report_error(e.line, e.colume, "Undefined array type");
			return new ExpTy(null, INT);
		}
		
		eType = eType.actual();
		if (!(eType instanceof ARRAY)) {
			report_error(e.line, e.colume, "Array type required");
			return new ExpTy(null, INT);
		}
		
		ExpTy arraySize = transExp(e.size);
		checkInt(arraySize, e.size.line, e.size.colume);
		
		ExpTy eInit = transExp(e.init);
		checkAssign(((ARRAY)eType).element.actual(), eInit.ty, e.line, e.colume);
		
		return new ExpTy(translate.transArrayExp(level, eInit.exp, arraySize.exp), eType);
	}

	ExpTy transExp(tiger.Absyn.AssignExp e) {
		ExpTy eLvalue = transVar(e.var);
		ExpTy eExp = transExp(e.exp);
		if (e.var instanceof tiger.Absyn.SimpleVar && env.vEnv.get(((tiger.Absyn.SimpleVar)e.var).name) instanceof ForVarEntry)
			report_error(e.line, e.colume, "Loop value should not be assigned");
		checkAssign(eLvalue.ty, eExp.ty, e.line, e.colume);
		return new ExpTy(translate.transAssignExp(eLvalue.exp, eExp.exp), VOID);
	}

	ExpTy transExp(tiger.Absyn.BreakExp e) {
		if (!env.loopEnv.inLoop()) {
			report_error(e.line, e.colume, "Illegal break");
			return null;
		}
		return new ExpTy(translate.transBreak(env.loopEnv.done()), VOID);
	}

	ExpTy transExp(tiger.Absyn.CallExp e) {
		Entry eFunc = (Entry)env.vEnv.get(e.func);
		if (eFunc == null || !(eFunc instanceof FunEntry)) {
			report_error(e.line, e.colume, "Function " + e.func.toString() + " required");
			return new ExpTy(null, INT);
		}
		tiger.Absyn.ExpList eArgument = e.args;
		RECORD eFormals = ((FunEntry)eFunc).formals;
		ExpTy tmp = null;
		ArrayList <Exp> argValue = new ArrayList <Exp> ();
		for (; eArgument != null; eArgument = eArgument.tail, eFormals = eFormals.tail) {
			if (eFormals == null) {
				report_error(e.line, e.colume, "Function " + e.func.toString() + " has too many arguments");
				break;
			}
			tmp = transExp(eArgument.head);
			checkAssign(eFormals.fieldType.actual(), tmp.ty, eArgument.head.line, eArgument.head.colume);
			argValue.add(tmp.exp);
		}
		if (eFormals != null)
			report_error(e.line, e.colume, "Function " + e.func.toString() + "'s arguments are lesser then expected");
		return new ExpTy(translate.transCallExp(level, ((FunEntry)eFunc).level, ((FunEntry)eFunc).label, argValue), ((FunEntry)eFunc).result.actual());
	}

	ExpTy transExp(tiger.Absyn.ForExp e) {
		env.vEnv.beginScope();
		ExpTy dInit = transExp(e.var.init);
		if (dInit.ty != INT)
			report_error(e.var.line, e.var.colume, "Loop variable should be integer");
		Access access = level.allocLocal(e.var.escape);
		env.vEnv.put(e.var.name, new ForVarEntry(access, dInit.ty));
		ExpTy hi = transExp(e.hi);
		checkInt(hi, e.hi.line, e.hi.colume);
		env.loopEnv.newLoop();
		ExpTy body = transExp(e.body);
		if (!body.ty.coerceTo(VOID))
			report_error(e.body.line, e.body.colume, "For expression should return void");
		Exp forExp = translate.transForExp(level, access, dInit.exp, hi.exp, body.exp, env.loopEnv.done());
		env.loopEnv.exitLoop();
		env.vEnv.endScope();
		return new ExpTy(forExp, VOID);
	}

	Type checkIfType(ExpTy et1, ExpTy et2, int line, int colume) {
		if (et1.ty.coerceTo(NIL) && et2.ty instanceof RECORD)
			return et2.ty;
		if (et2.ty.coerceTo(NIL) && et1.ty instanceof RECORD)
			return et1.ty;
		if (et1.ty.coerceTo(et2.ty))
			return et1.ty;
		report_error(line, colume, "Incompatible types at if expression");
		return null;
	}

	ExpTy transExp(tiger.Absyn.IfExp e) {
		Type temp;
		
		ExpTy eTest = transExp(e.test);
		
		ExpTy eThen = transExp(e.thenclause);
		ExpTy eElse;
		if (e.elseclause == null)
			eElse = null;
		else
			eElse = transExp(e.elseclause);

		checkInt(eTest, e.test.line, e.test.colume);

		if (eElse != null) {
			if ((temp = checkIfType(eThen, eElse, e.elseclause.line, e.elseclause.colume)) != null)
				return new ExpTy(translate.transIfThenElseExp(eTest.exp, eThen.exp, eElse.exp), temp);
			else {
				report_error(e.elseclause.line, e.elseclause.colume, "if-then-else should of same type");
				return new ExpTy(null, VOID);
			}
		}
		else {
			if (eThen.ty.coerceTo(VOID))
				return new ExpTy(translate.transIfThenElseExp(eTest.exp, eThen.exp, null), VOID);
			else {
				report_error(e.thenclause.line, e.thenclause.colume, "when no else, then clause must return void");
				return new ExpTy(null, VOID);
			}
		}
	}
	
	ExpTy transExp(tiger.Absyn.IntExp e) {
		return new ExpTy(translate.transIntExp(e.value), INT);
	}
	
	ExpTy transExp(tiger.Absyn.LetExp e) {
		env.vEnv.beginScope();
		env.tEnv.beginScope();
		ExpList eDec = null, p = null;
		for (tiger.Absyn.DecList ptr = e.decs; ptr != null; ptr = ptr.tail) {
			if (eDec == null)
				p = eDec = new ExpList(transDec(ptr.head), null);
			else
				p = p.tail = new ExpList(transDec(ptr.head), null);
		}
		ExpTy eBody = transExp(e.body);
		env.vEnv.endScope();
		env.tEnv.endScope();
		return new ExpTy(translate.transLetExp(eDec, eBody.exp, eBody.ty.coerceTo(VOID)), eBody.ty);
	}

	ExpTy transExp(tiger.Absyn.NilExp e) {
		return new ExpTy(translate.transNilExp(), NIL);
	}
	

	Type checkEqualType(ExpTy et1, ExpTy et2, int line, int colume) {
		if (et1.ty.coerceTo(et2.ty)
			&& (et1.ty.coerceTo(INT) || et1.ty.coerceTo(STRING) 
				|| et1.ty instanceof RECORD || et1.ty instanceof ARRAY))
			return et1.ty;
		if (et1.ty.coerceTo(NIL) && et2.ty instanceof RECORD)
			return et2.ty;
		if (et2.ty.coerceTo(NIL) && et1.ty instanceof RECORD)
			return et1.ty;
		report_error(line, colume, "Incompatible operand types");
		return INT;
	}

	Type checkCmpType(ExpTy et1, ExpTy et2, int line, int colume) {
		if (et1.ty.coerceTo(INT) && et2.ty.coerceTo(INT))
			return et1.ty;
		if (et1.ty.coerceTo(STRING) && et2.ty.coerceTo(STRING))
			return et1.ty;
		report_error(line, colume, "Incompatible operand types");
		return INT;
	}

	ExpTy transExp(tiger.Absyn.OpExp e) {
		ExpTy eLeft = transExp(e.left);
		ExpTy eRight = transExp(e.right);

		if (e.oper == tiger.Absyn.OpExp.PLUS || e.oper == tiger.Absyn.OpExp.MINUS
				|| e.oper == tiger.Absyn.OpExp.MUL || e.oper == tiger.Absyn.OpExp.DIV) {
			checkInt(eLeft, e.left.line, e.left.colume);
			checkInt(eRight, e.right.line, e.right.colume);
			return new ExpTy(translate.transCalcExp(e.oper, eLeft.exp, eRight.exp), INT);
		} else if (e.oper == tiger.Absyn.OpExp.EQ || e.oper == tiger.Absyn.OpExp.NE){
			Type ty = checkEqualType(eLeft, eRight, e.line, e.colume);
			if (ty.coerceTo(STRING))
				return new ExpTy(translate.transStringRelExp(transOp(e.oper), eLeft.exp, eRight.exp), INT);
			else
				return new ExpTy(translate.transOtherRelExp(transOp(e.oper), eLeft.exp, eRight.exp), INT);
		}
		else {
			Type ty = checkCmpType(eLeft, eRight, e.line, e.colume);
			if (ty.coerceTo(STRING))
				return new ExpTy(translate.transStringRelExp(transOp(e.oper), eLeft.exp, eRight.exp), INT);
			else
				return new ExpTy(translate.transOtherRelExp(transOp(e.oper), eLeft.exp, eRight.exp), INT);
		}
	}
	

	private int transOp(int oper) {
		switch (oper) {
			case OpExp.EQ: return 0;
			case OpExp.NE: return 1;
			case OpExp.LT: return 2;
			case OpExp.LE: return 4;
			case OpExp.GT: return 3;
			case OpExp.GE: return 5;
			default: throw new RuntimeException("Error at transOp in Semant.");
		}
	}

	ExpTy transExp(tiger.Absyn.RecordExp e) {
		Type eType = (Type)env.tEnv.get(e.typ);
		if (eType == null) {
			report_error(e.line, e.colume, "Undefined record type " + e.typ.toString());
			return new ExpTy(null, INT);
		}
		
		eType = eType.actual();
		if (!(eType instanceof RECORD)) {
			report_error(e.line, e.colume, "Record type required");
			return new ExpTy(null, INT);
		}

		tiger.Absyn.FieldExpList eFields = e.fields;
		RECORD eRecord = (RECORD)eType;
		ExpTy et;

		ArrayList <Exp> fieldList = new ArrayList<Exp> ();
		for (; eFields != null; eFields = eFields.tail, eRecord = eRecord.tail) {
			if (eRecord == null) {
				report_error(eFields.line, eFields.colume,
						"Field " + eFields.name.toString() + " has not been declared");
				break;
			}
			if (eRecord.fieldName != eFields.name) {
				report_error(eFields.line, eFields.colume, 
						eRecord.fieldName.toString() + " field dismatch");
				break;
			}
			et = transExp(eFields.init);
			fieldList.add(et.exp);
			checkAssign(eRecord.fieldType.actual(), et.ty, eFields.line, eFields.colume);
		}

		if (eRecord != null)
			report_error(eFields.line, eFields.colume, "Missing record fields");
		return new ExpTy(translate.transRecordExp(level, fieldList), eType);
	}
	
	ExpTy transExp(tiger.Absyn.SeqExp e) {
		Type type = VOID;
		ExpTy et;
		ExpList el = null, ptr = null;
		for (tiger.Absyn.ExpList h = e.list; h != null; h = h.tail) {
			et = transExp(h.head);
			type = et.ty;
			if (el == null)
				el = ptr = new ExpList(et.exp, null);
			else
				ptr = ptr.tail = new ExpList(et.exp, null);
		}
		return new ExpTy(translate.transSeqExp(el, type.coerceTo(VOID)), type);

	}
	
	ExpTy transExp(tiger.Absyn.StringExp e) {
		return new ExpTy(translate.transStringExp(e.value), STRING);
	}
	

	ExpTy transExp(tiger.Absyn.VarExp e) {
		return transVar(e.var);
	}
	

	ExpTy transExp(tiger.Absyn.WhileExp e) {
		ExpTy eTest = transExp(e.test);
		checkInt(eTest, e.test.line, e.test.colume);
		env.loopEnv.newLoop();
		ExpTy body = transExp(e.body);
		if (body.ty != VOID)
			report_error(e.body.line, e.body.colume, "While expression should return void");
		Exp eWhile = translate.transWhileExp(eTest.exp, body.exp, env.loopEnv.done());
		env.loopEnv.exitLoop();
		return new ExpTy(eWhile, VOID);
	}
	
	Exp transDec(tiger.Absyn.Dec d) {
		if (d instanceof tiger.Absyn.VarDec)
			return transDec((tiger.Absyn.VarDec) d);
		if (d instanceof tiger.Absyn.TypeDec)
			return transDec((tiger.Absyn.TypeDec) d);
		if (d instanceof tiger.Absyn.FunctionDec)
			return transDec((tiger.Absyn.FunctionDec) d);
		fatal_error("Translating declaration");
		return null;
	}
	
	Exp transDec(tiger.Absyn.VarDec d) {
		ExpTy dInit = transExp(d.init);
		Type ty = null;
		if (d.typ == null)
			if (dInit.ty == NIL) {
				report_error(d.init.line, d.init.colume, "Illegal varible initialization");
				return null;
			} else
				ty =  dInit.ty;
		else {
			ty = transTy(d.typ).actual();
			checkAssign(ty, dInit.ty, d.init.line, d.init.colume);
		}
		Access access = level.allocLocal(d.escape);
		env.vEnv.put(d.name, new VarEntry(access, ty));
		return translate.transAssignExp(translate.transSimpleVar(access, level), dInit.exp);
	}
	
	Exp transDec(tiger.Absyn.TypeDec d) {
		List <tiger.Symbol.Symbol> list = new ArrayList <tiger.Symbol.Symbol> ();
		for (tiger.Absyn.TypeDec it = d; it != null; it = it.next)
			if (list.contains(it.name))
				report_error(it.line, it.colume, "This type has been defined in this type declaration sequence");
			else {
				list.add(it.name);
				env.tEnv.put(it.name, new NAME(it.name));
			}
		for (tiger.Absyn.TypeDec it = d; it != null; it = it.next)
			((NAME)env.tEnv.get(it.name)).bind(transTy(it.ty));
		for (tiger.Absyn.TypeDec it = d; it != null; it = it.next)
			if (((NAME)env.tEnv.get(it.name)).isLoop())
				report_error(it.line, it.colume, "This is a loop type declaration");
		return translate.transNoOp();
	}
	
	Exp transDec(tiger.Absyn.FunctionDec d) {
		List<tiger.Symbol.Symbol> list = new ArrayList <tiger.Symbol.Symbol> ();
		for (tiger.Absyn.FunctionDec it = d; it != null; it = it.next)
			if (env.vEnv.get(it.name) != null && env.vEnv.get(it.name) instanceof StdFunEntry)
				report_error(it.line, it.colume, "This funtion has been defined in standard library");
			else if (list.contains(it.name))
				report_error(it.line, it.colume, "This funtion has been defined in this function declaration sequence");
			else {
				list.add(it.name);
				Type result = (it.result == null) ? VOID : transTy(it.result).actual();
				Label label = new Label(it.name);
				Level new_level = new Level(level, label, makeBoolList(it.params));
				env.vEnv.put(it.name, new FunEntry(new_level, label, transTypeFields(it.params), result));
			}
		for (tiger.Absyn.FunctionDec it = d; it != null; it = it.next) {
			FunEntry f = (FunEntry)env.vEnv.get(it.name);
			env.vEnv.beginScope();
			env.loopEnv.beginScope();
			Level pLevel = level;
			level = f.level;
			AccessList al = level.formals.tail;
			for (tiger.Absyn.FieldList p = it.params; p != null; p = p.tail, al = al.tail) {
				Type ty = (Type)env.tEnv.get(p.typ);
				if (ty == null) {
					report_error(p.line, p.colume, "Undefined type" + p.typ.toString());
					env.vEnv.endScope();
					return null;
				}
				else {
					Access acc = new Access(level, al.head.access);
					env.vEnv.put(p.name, new VarEntry(acc, ty.actual()));
				}
			}
			ExpTy et = transExp(it.body);
			translate.procEntryExit(level, et.exp, et.ty != VOID);
			env.loopEnv.endScope();
			env.vEnv.endScope();
			level = pLevel;
			checkAssign(f.result.actual(), et.ty, it.body.line, it.body.colume);
		}
		return translate.transNoOp();
	}

	private BoolList makeBoolList(FieldList params) {
		if (params == null)
			return null;
		else
			return new BoolList(params.escape, makeBoolList(params.tail));
	}

	RECORD transTypeFields(tiger.Absyn.FieldList p) {
		RECORD result = null, ptr = null;
		for (; p != null; p = p.tail) {
			Type t = ((Type)(env.tEnv.get(p.typ))).actual();
			if (t == null)
				report_error(p.line, p.colume, "Undefined type " + p.typ.toString());
			else if (ptr == null)
				result = ptr = new RECORD(p.name, t, null);
			else {
				ptr.tail = new RECORD(p.name, t, null);
				ptr = ptr.tail;
			}
		}

		return result;
	}
	
	tiger.Types.Type transTy(tiger.Absyn.Ty t) {
		if (t instanceof tiger.Absyn.NameTy)
			return transTy((tiger.Absyn.NameTy)t);
		if (t instanceof tiger.Absyn.RecordTy)
			return transTy((tiger.Absyn.RecordTy)t);
		if (t instanceof tiger.Absyn.ArrayTy)
			return transTy((tiger.Absyn.ArrayTy)t);
		fatal_error("Translating type");
		return null;
	}
	
	Type transTy(tiger.Absyn.NameTy t) {
		Type Result = (Type) (env.tEnv.get(t.name));
		if (Result != null)
			return Result;
		else {
			report_error(t.line, t.colume, "Undefined type " + t.name.toString());
			return INT;
		}
	}

	
	Type transTy(tiger.Absyn.RecordTy t) {
		RECORD Result = null, p = null;
		Type temp;
		List <tiger.Symbol.Symbol> dict = new ArrayList <tiger.Symbol.Symbol> ();
		for (tiger.Absyn.FieldList tField = t.fields; tField != null; tField = tField.tail) {
			temp = (tiger.Types.Type) (env.tEnv.get(tField.typ));
			if (temp == null) {
				report_error(tField.line, tField.colume, "Undefined type " + tField.typ.toString());
				return INT;
			}
			if (dict.contains(tField.name))
				report_error(tField.line, tField.colume, "Redefined field name " + tField.name.toString());
			else
				dict.add(tField.name);
			if (p == null)
				Result = p = new RECORD(tField.name, temp, null);
			else {
				p.tail = new RECORD(tField.name, temp, null);
				p = p.tail;
			}
				
		}
		return Result;
	}
	
	Type transTy(tiger.Absyn.ArrayTy t) {
		Type tType = (Type) (env.tEnv.get(t.typ));
		if (tType == null) {
			report_error(t.line, t.colume, "Undefined type " + t.typ.toString());
			return INT;
		}
		return new ARRAY(tType);
	}
}
