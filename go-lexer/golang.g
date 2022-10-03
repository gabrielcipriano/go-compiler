lexer grammar golang;

fragment DIGIT      : [0-9] ;
fragment ALPHA      : [A-Za-z] ;
fragment NOZERO     : [1-9];
fragment LETTER     : ALPHA|'_';

WS                  : [ \n\t]+ -> skip ;

COMMENT_1           : '//'~[\n]* -> skip ;
COMMENT_2           : '/*' .*? '*/' -> skip ;

NUMBER              : NOZERO DIGIT*;

IF                  : 'if' ;
WHILE               : 'while';

INT                 : 'int';
FLOAT32             : 'float32';
STRING              : 'string';
BOOLEAN             : 'bool';
VAR                 : 'var';
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
CHAN                : 'chan';
ELSE                : 'else';
GOTO                : 'goto';
PACKAGE             : 'package';
SWITCH              : 'switch';
CONST               : 'const';
FALLTHROUGH         : 'fallthrough';
RANGE               : 'range';
TYPE                : 'type';
CONTINUE            : 'continue';
FOR                 : 'for';
IMPORT              : 'import';
RETURN              : 'return';

NUMBER_VAL          : NOZERO DIGIT* ;
ID                  : LETTER(LETTER|DIGIT)*;

SUM_OP              : '+' ;
PLUS_EQ_OP          : '+=' ;
AND_AND_OP          : '&&' ;
EQ_EQ_OP            : '==' ;
NOT_EQ_OP           : '!=' ;
OPEN_PAREN          : '(' ;
CLOSE_PAREN         : ')' ;
DIFF_OP             : '-' ;
DIFF_EQ             : '-=' ;
OR_OP               : '||' ;
LESS_OP             : '<' ;
LESS_EQ_OP          : '<=' ;
OPEN_SQUARE_BR_OP   : '[' ;
CLOSE_SQUARE_BR_OP  : ']' ;
MULT_OP             : '*' ;
MULT_EQ_OP          : '*=' ;
// : '<-' ; 
GREATER_OP          : '>' ;
GREATER_EQ_OP       : '>=' ;
OPEN_BRACES_OP      : '{' ;
CLOSE_BRACES_OP     : '}' ;
QUOTIENT_OP         : '/' ;
QUOTIENT_EQ_OP      : '/=' ;
INCREMENT_OP        : '++' ;
ASSIGN_OP           : '=' ;
SHORT_VAR_DECL      : ':=' ;
COMMA               : ',' ;
SEMICOLON           : ';' ;
// REMINDER_OP  : '%' ;
// REMINDER_EQ_OP  : '%=' ;
DECREMENT_OP        : '--' ;
NOT_OP              : '!' ;
// : '...' ;
DOT                 : '.' ;
COLON               : ':' ;
// : '~' ;
