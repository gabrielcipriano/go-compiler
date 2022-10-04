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
ROUND_BOIS_L        : '(';
ROUND_BOIS_R        : ')';
CURVY_BOIS_L        : '{';
CURVY_BOIS_R        : '}';
SQUARE_BOIS_L       : '[';
SQUARE_BOIS_R       : ']';
ASSIGN              : '=';
COMMA               : ',';
SEMI                : ';';
COLON               : ':';
DOT                 : '.';
PLUS_PLUS           : '++';
MINUS_MINUS         : '--';
DECLARE_ASSIGN      : ':=';
ELLIPSIS            : '...';

PLUS                   : '+';
MINUS                  : '-';
CARET                  : '^';
STAR                   : '*';
AMPERSAND              : '&';
RECEIVE                : '<-';

EQUALS                 : '==';
NOT_EQUALS             : '!=';
LESS                   : '<';
LESS_OR_EQUALS         : '<=';
GREATER                : '>';
GREATER_OR_EQUALS      : '>=';

// Lógica

LOGICAL_OR             : '||';
LOGICAL_AND            : '&&';

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

NUMBER              : NOZERO DIGIT*;
INT_VAL             : DIGIT+ ;
REAL_VAL            : DIGIT+'.'DIGIT+ ;

ID                  : LETTER(LETTER|DIGIT)*;
