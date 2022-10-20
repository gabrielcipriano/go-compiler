lexer grammar GOLexer;

fragment DIGIT        : [0-9] ;
fragment ALPHA        : [A-Za-z] ;
fragment LETTER       : ALPHA | '_';
fragment ESCAPED_CHAR : [abfnrtv\\'"];

WS                  : [ \t]+  -> skip ;
TERMINATOR          : [\r\n]+ -> skip;

// Símbolos

PRINT               : 'fmt.Println';
PLUS                : '+' ;
MINUS               : '-' ;
STAR                : '*' ;
DIV                 : '/' ;
AND_AND             : '&&' ;
EQ_EQ               : '==' ;
NOT_EQ              : '!=' ;
L_PR                : '(' ;
R_PR                : ')' -> mode(NLSEMI);
OR                  : '||' ;
LESS                : '<' ;
LESS_EQ             : '<=' ;
L_BRACKETS         : '[' ;
R_BRACKETS         : ']' -> mode(NLSEMI);
GREATER             : '>' ;
GREATER_EQ          : '>=' ;
L_BRACES            : '{' ;
R_BRACES            : '}' -> mode(NLSEMI);
INCREMENT           : '++' -> mode(NLSEMI);
ASSIGN              : '=' ;
SHORT_VAR_DECL      : ':=' ;
COMMA               : ',' ;
SEMI                : ';' ;
DECREMENT           : '--' -> mode(NLSEMI);
NOT                 : '!' ;
DOT                 : '.' ;
COLON               : ':' ;

// Tipo de dados

INT                 : 'int' -> mode(NLSEMI);
FLOAT32             : 'float32' -> mode(NLSEMI);
STRING              : 'string' -> mode(NLSEMI);
BOOLEAN             : 'bool' -> mode(NLSEMI);
VAR                 : 'var';

// Palavras reservadas

IF                  : 'if' ;
WHILE               : 'while';
BREAK               : 'break'  -> mode(NLSEMI);
DEFAULT             : 'default';
FUNC                : 'func';
INTERFACE           : 'interface';
SELECT              : 'select';
CASE                : 'case';
DEFER               : 'defer';
GO                  : 'go';
STRUCT              : 'struct';
ELSE                : 'else';
GOTO                : 'goto';
PACKAGE             : 'package';
SWITCH              : 'switch';
CONST               : 'const';
FALLTHROUGH         : 'fallthrough';
//RANGE               : 'range';
TYPE                : 'type';
CONTINUE            : 'continue' -> mode(NLSEMI);
FOR                 : 'for';
IMPORT              : 'import' .*? '\n' -> skip;
RETURN              : 'return' -> mode(NLSEMI);

// Strings

COMMENT_1           : '//'~[\r\n]* -> skip ;
COMMENT_2           : '/*' .*? '*/' -> skip;

NIL_LIT             : 'nil'-> mode(NLSEMI);
INT_LIT             : DIGIT+ -> mode(NLSEMI);
FLOAT_LIT           : DIGIT+'.'DIGIT+ -> mode(NLSEMI);
STR_LIT             : '"'(~["]|'\\"')*?'"'-> mode(NLSEMI);

ID                  : LETTER(LETTER|DIGIT)* -> mode(NLSEMI);


mode NLSEMI;

// whitespace as normal
WS_NLSEMI              : [ \t]+   -> skip;
// Ignore any comments that only span one line
COMMENT_NLSEMI         : '/*' ~[\r\n]*? '*/' -> skip;
LINE_COMMENT_NLSEMI    : '//' ~[\r\n]*       -> skip;
// Emit an EOS token for any newlines, semicolon, multiline comments or the EOF and 
//return to normal lexing
EOS                    : ([\r\n]+ | ';' | '/*' .*? '*/' | EOF) -> mode(DEFAULT_MODE);
// Did not find an EOS, so go back to normal lexing
OTHER                  : -> mode(DEFAULT_MODE), skip;
