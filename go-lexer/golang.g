lexer grammar golang;

fragment DIGIT      : [0-9] ;
fragment ALPHA      : [A-Za-z] ;

WS                : [ \n\t]+ -> skip ;

COMMENT_1         : '//'~[\n]* -> skip ;
COMMENT_2         : '/*' .*? '*/' ;
