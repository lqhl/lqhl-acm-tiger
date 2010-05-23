package tiger.Parser;
import java_cup.runtime.*;

%%

%class Lexer
%public

%unicode

%line
%column

%cup

%{
	StringBuffer string = new StringBuffer();
	int commentCount;
	/* To create a new java_cup.runtime.Symbol with information about
	   the current token, the token will have no value in this
	   case. */
	private Symbol symbol(int type) {
		return new Symbol(type, yyline, yycolumn);
	}
	
	/* Also creates a new java_cup.runtime.Symbol with information
	   about the current token, but this object has a value. */
	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline, yycolumn, value);
	}
%}

%eofval{
	{
		if(yystate() == STRING)
		{
			throw new RuntimeException("Unclosed string constant " + "at end of file");
		}
		if(yystate() == COMMENT)
		{
			throw new RuntimeException("Unclosed comment " + "at end of file");
		}
		return symbol(sym.EOF);
	}
%eofval}

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

/* identifiers */
Identifier = [_a-zA-Z] ([_a-zA-Z] | [0-9])*

/* integer literals */
DecIntegerLiteral = [0-9][0-9]*

%state STRING
%state COMMENT

%%

<YYINITIAL> {
	\"	{string.setLength(0); yybegin(STRING);}
	"/*"	{commentCount = 1; yybegin(COMMENT);}

	/* reserved words */
	"array"	{return symbol(sym.ARRAY);}
	"break"	{return symbol(sym.BREAK);}
	"do"	{return symbol(sym.DO);}
	"else"	{return symbol(sym.ELSE);}
	"end"	{return symbol(sym.END);}
	"for"	{return symbol(sym.FOR);}
	"function"	{return symbol(sym.FUNCTION);}
	"if"	{return symbol(sym.IF);}
	"in"	{return symbol(sym.IN);}
	"let"	{return symbol(sym.LET);}
	"nil"	{return symbol(sym.NIL);}
	"of"	{return symbol(sym.OF);}
	"then"	{return symbol(sym.THEN);}
	"to"	{return symbol(sym.TO);}
	"type"	{return symbol(sym.TYPE);}
	"var"	{return symbol(sym.VAR);}
	"while"	{return symbol(sym.WHILE);}
	/* punctuation symbols */
	","	{return symbol(sym.COMMA);}
	":"	{return symbol(sym.COLON);}
	";"	{return symbol(sym.SEMI);}
	"("	{return symbol(sym.LPAREN);}
	")"	{return symbol(sym.RPAREN);}
	"["	{return symbol(sym.LBRACK);}
	"]"	{return symbol(sym.RBRACK);}
	"{"	{return symbol(sym.LBRACE);}
	"}"	{return symbol(sym.RBRACE);}
	"."	{return symbol(sym.DOT);}
	"+"	{return symbol(sym.PLUS);}
	"-"	{return symbol(sym.MINUS);}
	"*"	{return symbol(sym.TIMES);}
	"/"	{return symbol(sym.DIVIDE);}
	"="	{return symbol(sym.EQ);}
	"<>"	{return symbol(sym.NEQ);}
	"<"	{return symbol(sym.LT);}
	"<="	{return symbol(sym.LE);}
	">"	{return symbol(sym.GT);}
	">="	{return symbol(sym.GE);}
	"&"	{return symbol(sym.AND);}
	"|"	{return symbol(sym.OR);}
	":="	{return symbol(sym.ASSIGN);}
	/* identifier */
	{Identifier}	{return symbol(sym.ID, yytext());}
	/* integer */
	{WhiteSpace}	{}
	{DecIntegerLiteral}	{return symbol(sym.NUM, new Integer(yytext()));}

	/* Illegal character */
	[^]	{ throw new RuntimeException(" Illegal character <" + yytext() + ">"); }
}

<STRING> {
	\"	{yybegin(YYINITIAL);return symbol(sym.STRING, string.toString());}
	[^\n\t\"\\]+	{string.append(yytext());}
	{LineTerminator} {throw new RuntimeException("Unterminated string at end of line");}
	\\n	{string.append('\n');}
	\\t	{string.append('\t');}
	\\[0-9][0-9][0-9]	{int val = Integer.parseInt(yytext().substring(1, 4));
		if(val > 255) throw new RuntimeException("Illegal string with \\ddd"); else string.append((char)val);}
	\\\"	{string.append('\"');}
	\\\\	{string.append('\\');}
	\\{WhiteSpace}+\\	{ /* nothing to do */ }
}

<COMMENT> {
	"/*"	{commentCount++;}
	"*/"	{commentCount--;if (commentCount==0) {yybegin(YYINITIAL);}}
	[^]	{}
}
