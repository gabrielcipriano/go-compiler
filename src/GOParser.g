parser grammar GOParser;

options {
  tokenVocab = GOLexer;
}

program: 
  statement+
;


assignment:
  identifier_list assign_op expr_list;

assign_op: (
  PLUS
| MINUS
| STAR
| DIV
)? ASSIGN;


identifier_list :
  ID (COMMA ID)*
;

expr_list:
  expr (COMMA expr)*;

expr: 
  primary_expr
| unary_op = ( PLUS | MINUS | NOT ) expr
| expr mul_op = (STAR | DIV) expr
| expr add_op = (PLUS | MINUS) expr
| expr rel_op = (
    EQ_EQ 
  | NOT_EQ
  | LESS
  | LESS_EQ
  | GREATER
  | GREATER_EQ
) expr
| expr AND_AND expr
| expr OR expr;

/*simpleStmt:
	| incDecStmt
	| assignment
	| expressionStmt
	| shortVarDecl;

expressionStmt : 
  expr;*/

forStmt: FOR (expr? | forClause) block;

block: L_BRACES statementList? R_BRACES;

statementList: (statement SEMI)+;

statement:
	//declaration
	//| labeledStmt
	simpleStmt
	//| goStmt
	| returnStmt
	| breakStmt
	| continueStmt
	//| gotoStmt
	//| fallthroughStmt
	| block
	//| ifStmt
	//| switchStmt
	//| selectStmt
	| forStmt;
	//| deferStmt;

forClause:
	initStmt = simpleStmt? SEMI expr SEMI postStmt = expr?;

simpleStmt:
	incDecStmt
	| assignment
	| expressionStmt
	| shortVarDecl;

breakStmt: BREAK ID?;

continueStmt: CONTINUE ID?;

incDecStmt: expr (INCREMENT | DECREMENT);

returnStmt: RETURN expr_list?;

expressionStmt: expr;

shortVarDecl: identifier_list SHORT_VAR_DECL expr_list;

primary_expr:
  operand;

operand:
 literal;

literal:
	basic_lit
//  basic_lit | composite_lit
;

basic_lit:
  NIL_LIT
| INT_LIT
| STR_LIT
| FLOAT_LIT;

// composite_lit: 

literal_type:
  array_type;

//literal_value:
 // L_BRACES (elementList COMMA?)? R_BRACES
 // ;

element_list: expr (COMMA expr)*;

type_name: ID;

array_type: L_SQUARE_BR expr R_SQUARE_BR type;

type:
  INT | FLOAT32 | STRING | BOOLEAN | array_type | R_PR type L_PR;
