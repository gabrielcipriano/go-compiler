lexer grammar GOLexer;

fragment DIGIT        : [0-9] ;
fragment ALPHA        : [A-Za-z] ;
fragment LETTER       : ALPHA | '_';
fragment ESCAPED_CHAR : [abfnrtv\\'"];

WS                  : [ \n]+ -> skip ;

// Símbolos

PRINT               : 'fmt.Printf';
PLUS                : '+' ;
MINUS               : '-' ;
STAR                : '*' ;
DIV                 : '/' ;
AND_AND             : '&&' ;
EQ_EQ               : '==' ;
NOT_EQ              : '!=' ;
L_PR                : '(' ;
R_PR                : ')' ;
OR                  : '||' ;
LESS                : '<' ;
LESS_EQ             : '<=' ;
L_SQUARE_BR         : '[' ;
R_SQUARE_BR         : ']' ;
GREATER             : '>' ;
GREATER_EQ          : '>=' ;
L_BRACES            : '{' ;
R_BRACES            : '}' ;
INCREMENT           : '++' ;
ASSIGN              : '=' ;
SHORT_VAR_DECL      : ':=' ;
COMMA               : ',' ;
SEMI                : ';' ;
DECREMENT           : '--' ;
NOT                 : '!' ;
DOT                 : '.' ;
COLON               : ':' ;

// Tipo de dados

INT                 : 'int';
FLOAT32             : 'float32';
STRING              : 'string';
BOOLEAN             : 'bool';
VAR                 : 'var';

// Palavras reservadas

IF                  : 'if' ;
WHILE               : 'while';
BREAK               : 'break';
DEFAULT             : 'default';
FUNC                : 'func';
INTERFACE           : 'interface';
SELECT              : 'select';
CASE                : 'case';
DEFER               : 'defer';
GO                  : 'go';
MAP                 : 'map';
STRUCT              : 'struct';
ELSE                : 'else';
GOTO                : 'goto';
PACKAGE             : 'package';
SWITCH              : 'switch';
CONST               : 'const';
FALLTHROUGH         : 'fallthrough';
//RANGE               : 'range';
TYPE                : 'type';
CONTINUE            : 'continue';
FOR                 : 'for';
IMPORT              : 'import';
RETURN              : 'return';

// Strings

COMMENT_1           : '//'~[\r\n]* -> skip ;
COMMENT_2           : '/*' .*? '*/' -> skip;

NIL_LIT             : 'nil';
INT_LIT             : DIGIT+ ;
FLOAT_LIT           : DIGIT+'.'DIGIT+ ;
STR_LIT             : '"'(~["]|'\\"')*'"';

ID                  : LETTER(LETTER|DIGIT)*;
