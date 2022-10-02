lexer grammar golang;

fragment DIGIT      : [0-9] ;
fragment ALPHA      : [A-Za-z] ;
fragment NOZERO     : [1-9];
fragment LETTER     : [a-zA-Z]| '_';

WS                  : [ \n\t]+ -> skip ;

COMMENT_1           : '//'~[\n]* -> skip ;
COMMENT_2           : '/*' .*? '*/' ;

NUMBER              : NOZERO DIGIT*;

IF                  : 'if' ;
WHILE               : 'while';

INT                 : 'int';
FLOAT32             : 'float32';
STRING              : 'string';
BOOLEAN             : 'bool';
VAR                 : 'var';
ID                  : LETTER(LETTER|NUMBER)*;
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
