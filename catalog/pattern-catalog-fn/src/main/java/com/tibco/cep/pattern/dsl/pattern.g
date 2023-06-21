grammar pattern;

options {
//	k=2;	
	output=AST;    
}

tokens {
	DEFINE_PATTERN_TOKEN;
	USING_TOKEN;
	EVENT_LIST_TOKEN;
	EVENT_TOKEN;
	SUBSCRIPTION_LIST_TOKEN;
	SUBSCRIPTION_TOKEN;
	MATCH_TOKEN;
	WHERE_TOKEN;
	STARTS_WITH_TOKEN;
	ITEM_TOKEN;
	ANY_ONE_TOKEN;
	ALL_TOKEN;
	SUBPATTERN_ITEM_TOKEN;
	THEN_TOKEN;
	THEN_ANY_ONE_TOKEN;
	THEN_ALL_TOKEN;
	THEN_REPEAT_TOKEN;
	THEN_WITHIN_TOKEN;
	THEN_DURING_TOKEN;
	THEN_AFTER_TOKEN;
	REPEAT_TOKEN;
	DATE_TOKEN;
	DATETIME_TOKEN;
	BIND_TOKEN;
	STRING_TOKEN;
	BOOLEAN_TOKEN;
	INTEGER_TOKEN;
	LONG_TOKEN;
	FLOAT_TOKEN;
	DOUBLE_TOKEN;
	LITERAL_TOKEN;
}

/**/
@header {
	package com.tibco.cep.pattern.dsl; 

	import com.tibco.cep.pattern.dsl.*;
}

@lexer::header {
	package com.tibco.cep.pattern.dsl; 

	import com.tibco.cep.pattern.dsl.*;
}

@members {
	ExceptionHandler handler = new ExceptionHandler();
	
	public LanguageException createException(IntStream intStream) {
		CommonTokenStream cts = (CommonTokenStream) intStream;
		patternLexer pl = (patternLexer) cts.getTokenSource();
		PatternLanguageLexer pll = new PatternLanguageLexer(pl);
		ANTLRStringStream acs = (ANTLRStringStream) pll.getInputSource();
		String inputText = acs.substring(0, acs.size()-1);
		int errorline = cts.LT(1).getLine();
		int errorpos = cts.LT(1).getCharPositionInLine();
       		if (errorpos < 0) { //backtrack since token is missing or invalid
			errorline = cts.LT(-1).getLine();
    			errorpos = ((CommonToken) cts.LT(-1)).getStopIndex()+1;
			return new LanguageException("Error on line " + errorline + " at position " + errorpos + ":" + 
			inputText.substring(0, errorpos) + "^" + inputText.substring(errorpos), inputText, errorpos);
       		}
		return new LanguageException("Error on line " + errorline + " at position " + errorpos + ":" + 
			inputText.substring(0, errorpos) + "^" + inputText.substring(errorpos), inputText, errorpos);
	}
	
	public void mismatch(IntStream intStream, int ttype, BitSet follow) 
			throws RecognitionException {
		handler.addException(createException(intStream));
	}
	
	protected Object recoverFromMismatchedToken(IntStream intStream, int i, BitSet bitSet) 
			throws RecognitionException { 
		handler.addException(createException(intStream));
		return null;
	}

	public Object recoverFromMismatchedSet(IntStream intStream, RecognitionException e, BitSet follow) 
			throws RecognitionException {
		handler.addException(createException(intStream));
		return null;
	}
	
	public ExceptionHandler getExceptionHandler() {
	        return handler;
	}
}

@rulecatch {

	catch(RecognitionException e2) { 
		handler.addException(createException(input));
		throw e2;
	} catch(Exception e3) { 
		//ignore only rewrite empty stream exception
		if (! (e3 instanceof RewriteEmptyStreamException || e3 instanceof RewriteEarlyExitException ||
			e3 instanceof LanguageException) ) {
			LanguageException le = new LanguageException(e3);
			handler.addException(le);
		}
	}
}
/**/

NEWLINE   		: ( '\r\n'
	     		    | '\r'
		            | '\n'
		          ) {$channel=HIDDEN;} ;
		          
DEFINE			: 'define';
PATTERN			: 'pattern';
USING			: 'using';
EVENT			: 'event';
AS			: 'as';
WHERE			: 'where';
AND			: 'and';
SUCH			: 'such';
THAT			: 'that';
STARTS			: 'starts';
WITH			: 'with';
ANY			: 'any';
ONE			: 'one';
ALL			: 'all';
THEN			: 'then';
REPEAT			: 'repeat';
WITHIN			: 'within';
DURING			: 'during';
TO			: 'to';
TIMES			: 'times';
AFTER			: 'after';
DAYS			: 'day' | 'days';
HOURS			: 'hour' | 'hours';
MINUTES			: 'minute' | 'minutes';
SECONDS			: 'second' | 'seconds';
MILLISECONDS		: 'millisecond' | 'milliseconds';
TRUE			: 'true';
FALSE			: 'false';
NULL			: 'null';

ESCAPE_SPACE		: '\\ ';
ESCAPE_KEYWORD		: '#';
COMMA			: ',';
SEMI			: ';';
LPAREN			: '(';
RPAREN			: ')';
LBRACE			: '{';
RBRACE			: '}';
BIND_SYMBOL		: '$';
fragment QUOTES		: ('"');

fragment CHAR		: ('a'..'z' | 'A'..'Z' | '/' | '_' | '-' | '0'..'9');
fragment DIGIT		: '0'..'9';
fragment SIGN		:  '+' | '-';

fragment LONG_SUFFIX	: 'l' | 'L';
fragment DOUBLE_SUFFIX	: 'd' | 'D';

SIGNED			: SIGN DIGIT+;
UNSIGNED		: DIGIT+;

LONG			: (SIGNED | UNSIGNED) LONG_SUFFIX;

FLOAT			: SIGN? DIGIT+ '.' DIGIT*;
DOUBLE			: FLOAT DOUBLE_SUFFIX;

fragment
NameFirstCharacter:
/* This is the same definition as Rules.g - Letter */
           CHAR |
           '\u0041'..'\u005a' |
           '\u0061'..'\u007a' |
           '\u00c0'..'\u00d6' |
           '\u00d8'..'\u00f6' |
           '\u00f8'..'\u00ff' |
           '\u0100'..'\u1fff' |
           '\u3040'..'\u318f' |
           '\u3300'..'\u337f' |
           '\u3400'..'\u3d2d' |
           '\u4e00'..'\u9fff' |
           '\uf900'..'\ufaff' |
           '\uff61'..'\uff9f'
        ;

fragment
NameCharacter:
/* This is the same definition as Rules.g - JavaIDDigit */
           '\u0030'..'\u0039' |
           '\u0660'..'\u0669' |
           '\u06f0'..'\u06f9' |
           '\u0966'..'\u096f' |
           '\u09e6'..'\u09ef' |
           '\u0a66'..'\u0a6f' |
           '\u0ae6'..'\u0aef' |
           '\u0b66'..'\u0b6f' |
           '\u0be7'..'\u0bef' |
           '\u0c66'..'\u0c6f' |
           '\u0ce6'..'\u0cef' |
           '\u0d66'..'\u0d6f' |
           '\u0e50'..'\u0e59' |
           '\u0ed0'..'\u0ed9' |
           '\u1040'..'\u1049'
       ;

IDENTIFIER:
           NameFirstCharacter (NameFirstCharacter | NameCharacter)*
       ;

NAME			: QUOTES (options { greedy = false; } : .)* QUOTES {setText(getText().substring(1,getText().length()-1));};

digits			: SIGNED | UNSIGNED;

year			: UNSIGNED;
month			: UNSIGNED;
day			: UNSIGNED;
hour			: UNSIGNED;
minute			: UNSIGNED;
second			: UNSIGNED;
millisecond		: UNSIGNED;

keywords		: DEFINE|PATTERN|USING|EVENT|AS|WHERE|AND|SUCH|THAT|STARTS|WITH|ANY|ONE|ALL|THEN|REPEAT|WITHIN|DURING|TO|TIMES|
			  AFTER|DAYS|HOURS|MINUTES|SECONDS|MILLISECONDS|TRUE|FALSE|NULL;

escaped_keyword		: ESCAPE_KEYWORD keywords -> keywords;

bindvar			: (BIND_SYMBOL identifier);

datevar			: '$date(' d11=year COMMA d12=month COMMA d13=day (COMMA d14=stringvar)? ')' -> ^(DATE_TOKEN $d11 $d12 $d13 $d14?)
			   | '$date(' d21=year COMMA d22=month COMMA d23=day COMMA d24=NULL ')' -> ^(DATE_TOKEN $d21 $d22 $d23);

datetimevar		: '$datetime(' d11=year COMMA d12=month COMMA d13=day COMMA d14=hour COMMA d15=minute COMMA d16=second  COMMA 
				d17=millisecond (COMMA d18=stringvar)? ')' -> ^(DATETIME_TOKEN $d11 $d12 $d13 $d14 $d15 $d16 $d17 $d18?)
			  | '$datetime(' d21=year COMMA d22=month COMMA d23=day COMMA d24=hour COMMA d25=minute COMMA d26=second  COMMA 
			  	d27=millisecond COMMA d28=NULL ')' -> ^(DATETIME_TOKEN $d21 $d22 $d23 $d24 $d25 $d26 $d27);

stringvar		: NAME;

booleanvar		: TRUE | FALSE;

integervar		: SIGNED | UNSIGNED;

unsignedintegervar	: UNSIGNED;

longvar			: LONG;

floatvar		: FLOAT;

doublevar 		: DOUBLE;

identifier		: escaped_keyword | digits | IDENTIFIER;

main			: query EOF -> query
			  | EOF -> EOF;

query			: define_pattern -> ^(define_pattern);

define_pattern		: DEFINE PATTERN id=patternname u=using -> ^(DEFINE_PATTERN_TOKEN $id $u);
			  
patternname		: NAME | identifier;

using			: USING events=event_list w=with -> ^(USING ^(EVENT_LIST_TOKEN $events) $w);

event_list		: (event AND?)+ -> ^(event)+;

event			: id=patternname AS alias=identifier -> ^(EVENT $id $alias);

with			: WITH subs=subscription_list s=starts_with -> ^(WHERE ^(SUBSCRIPTION_LIST_TOKEN $subs) $s);

subscription_list	: (subscription AND?)+ -> ^(subscription)+;

subscription		: f=field ('=' s=subscriptionvar)? -> ^(SUBSCRIPTION_TOKEN $f $s?);

subscriptionvar		: (d=datevar -> $d
			   | dt=datetimevar -> $dt
			   | s=stringvar -> ^(STRING_TOKEN $s)
			   | b=booleanvar -> ^(BOOLEAN_TOKEN $b)
			   | iv=integervar -> ^(INTEGER_TOKEN $iv)
			   | l=longvar -> ^(LONG_TOKEN $l)
			   | f=floatvar -> ^(FLOAT_TOKEN $f)
			   | dv=doublevar -> ^(DOUBLE_TOKEN $dv)
			   | bv=bindvar -> ^(BIND_TOKEN $bv)
			   );

field			: id=identifier '.' fieldname=identifier -> ^($id $fieldname);

starts_with		: STARTS WITH p=subpattern -> ^(STARTS_WITH_TOKEN $p);

subpattern		: (i=items then_list) -> ^($i then_list?);
			   
item_list		: LPAREN! i1=items (COMMA! i2=items)+ RPAREN!;// -> $i1 $i2+;

item			: id=identifier -> ^($id);

then_list		: (then)* -> ^(then)*;

then			: (THEN i=items -> ^(THEN_TOKEN $i)
			   | THEN WITHIN timeunit=time i=items -> ^(THEN_WITHIN_TOKEN $timeunit $i)
			   | THEN DURING timeunit=time i=items -> ^(THEN_DURING_TOKEN $timeunit $i)
			   | THEN AFTER timeunit=time -> ^(THEN_AFTER_TOKEN $timeunit)
			   );

repeat			: minval=value TO maxval=value TIMES items -> ^(REPEAT_TOKEN $minval $maxval items)
			   | times=value TIMES items -> ^(REPEAT_TOKEN $times items);

items			: (item -> ^(item)
			   | ANY ONE item_list -> ^(ANY_ONE_TOKEN item_list)
			   | ALL item_list -> ^(ALL_TOKEN item_list)
			   | REPEAT repeat -> ^(THEN_REPEAT_TOKEN repeat)
			   | LPAREN subpattern RPAREN -> ^(subpattern)
			   );
			   
value			: (bindvar -> ^(BIND_TOKEN bindvar)
			   | unsignedintegervar -> ^(INTEGER_TOKEN unsignedintegervar)
			  );
			   
time			: (bindvar | unsignedintegervar) timeunittype;

timeunittype 		: DAYS | HOURS | MINUTES | SECONDS | MILLISECONDS;

WHITESPACE		: (' '
			    | '\t'
			    | '\f'
			  ) {$channel=HIDDEN;} ;

ALLCHARS		: .;
