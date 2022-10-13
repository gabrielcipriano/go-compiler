lexer grammar ezlang_exemplo;

fragment DIGIT      : [0-9] ;
fragment ALPHA      : [A-Za-z] ;

WS          : [ \n\t]+ -> skip ;



// -----------------------------------------------------------------------
// reserved worlds
READ        : 'read' ;
REAL        : 'real' ;
REPEAT      : 'repeat' ;
STRING      : 'string' ;
THEN        : 'then' ;
TRUE        : 'true' ;
UNTIL       : 'until' ;
VAR         : 'var' ;
WRITE       : 'write' ;
BEGIN       : 'begin' ;
BOOL        : 'bool' ;
ELSE        : 'else' ;
END         : 'end' ;
FALSE       : 'false' ;
IF          : 'if' ;
INT         : 'int' ;
PROGRAM     : 'program' ;

ASSIGN        : ':=' ;
EQUALS        : '=' ;
LESS          : '<' ;
PLUS          : '+' ;
MINUS         : '-' ;
MULT          : '*' ;
DIVI          : '/' ;
PAREN_OPEN    : '(' ;
PAREN_CLOSE   : ')' ;
SEMI          : ';' ;

INT_VAL  : DIGIT+ ;
REAL_VAL : DIGIT+'.'DIGIT+ ;
STR_VAL      : '"'(~["]|'\\"')*'"';

COMMENT     : '{'~[{}]*'}' -> skip ;

ID          : (ALPHA)+;
