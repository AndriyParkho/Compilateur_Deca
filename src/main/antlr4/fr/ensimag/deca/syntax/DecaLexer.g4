lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// Deca lexer rules.

// Mots réservés
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
ELSE: 'else';
FALSE: 'false';
IF: 'if';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
TRUE: 'true';
WHILE: 'while';

// Identificateurs
fragment LETTER : ('a' .. 'z' | 'A' .. 'Z');
fragment DIGIT : '0' .. '9';
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;

// Symboles spéciaux
LT: '<';
GT: '>';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
EXCLAM: '!';
SEMI: ';' ;
EQEQ: '==';
NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
AND: '&&';
OR: '||';

// Littéraux entiers
fragment POSITIVE_DIGIT : '1' .. '9';
INT : '0' | (POSITIVE_DIGIT DIGIT*) 
		{try {
    		Integer.parseInt(getText());
    	} catch(IllegalArgumentException e) {
    		throw new IllegalArgumentException("L'entier entré n'est pas codable sur 32 bits");
    	}
		};

// Littéraux flottants
fragment NUM : DIGIT+;
fragment SIGN : '+' | '-' | ;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP)('F' | 'f' | );
fragment DIGITHEX : ('0' .. '9' | 'A' .. 'F' | 'a' .. 'f');
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );
FLOAT : FLOATDEC | FLOATHEX;


// Chaînes de caractères
fragment STRING_CAR : ~('"' | '\\' | '\n');
fragment EOL : '\n';
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';

// Commentaires 
fragment COMM_CLSQ : '/*' .*? '*/';
fragment COMM_LIGNE : '//' (~'\n')*;
fragment COMM : COMM_CLSQ | COMM_LIGNE;

// Séparateurs
SEP : ( ' ' | '\t' | EOL | '\r' | COMM) { skip(); };

// Inclusion de fichier
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"' 
		{ doInclude(getText()); 
		  skip(); };