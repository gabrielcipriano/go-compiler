lexer grammar golang;

fragment DIGIT        : [0-9] ;
fragment ALPHA        : [A-Za-z] ;
fragment NOZERO       : [1-9];
fragment LETTER       : ALPHA | '_';
fragment ESCAPED_CHAR : [abfnrtv\\'"];

// After a backslash, certain single-character escapes represent special values:
// \a   U+0007 alert or bell
// \b   U+0008 backspace
// \f   U+000C form feed
// \n   U+000A line feed or newline
// \r   U+000D carriage return
// \t   U+0009 horizontal tab
// \v   U+000B vertical tab
// \\   U+005C backslash
// \'   U+0027 single quote  (valid escape only within rune literals)
// \"   U+0022 double quote  (valid escape only within string literals)
RUNE_VAL            : '\''(~['] | '\\' ESCAPED_CHAR)'\'' ;

WS                  : [ \n\t]+ -> skip ;

STR_VAL             : '"'(~["]|'\\"')*'"';

// Símbolos

PRINT               : 'fmt.Printf';
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

// Strings

COMMENT_1           : '//'~[\r\n]* -> skip ;
COMMENT_2           : '/*' .*? '*/' -> skip;

INT_VAL             : DIGIT+ ;
REAL_VAL            : DIGIT+'.'DIGIT+ ;

ID                  : LETTER(LETTER|DIGIT)*;
