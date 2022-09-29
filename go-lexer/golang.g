lexer grammar golang;

fragment DIGIT      : [0-9] ;
fragment ALPHA      : [A-Za-z] ;
fragment NOZERO     : [1-9];
fragment LETTER     : [a-zA-Z]| '_';

WS                  : [ \n\t]+ -> skip ;

COMMENT_1           : '//'~[\n]* -> skip ;
COMMENT_2           : '/*' .*? '*/' ;

#REGION ROGER

NUMBER              : NOZERO DIGIT*

IF                  : "if" ;
WHILE               : "while";

INT                 : "int";
FLOAT32             : "float32";
STRING              : "string";
BOOLEAN             : "bool";
VAR                 : "var";
ID                  : LETTER(LETTER|NUMBER)
break
default
func
interface
select
case
defer
go
map
struct
chan
else
goto
package
switch
const
fallthrough
if
range
type
continue
for
import
return
var








#ENDREGION