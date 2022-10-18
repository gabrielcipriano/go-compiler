parser grammar GOParser;

options {
  tokenVocab = GOLexer;
}

program: 
  stmt+
;

stmt:
  assignment
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
  unary_op = ( PLUS | MINUS | NOT ) expr
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
| expr OR expr
| L_PR expr R_PR
| ID
| INT_LIT;

/*simpleStmt:
	| incDecStmt
	| assignment
	| expressionStmt
	| shortVarDecl;

expressionStmt : 
  expr;*/

forStmt: FOR (expr? | forClause | rangeClause?) block;

block: L_BRACES statementList? R_BRACES;

statementList: (/*(SEMI? | EOS? | {this.closingBracket()}?)*/ statement /*eos*/)+;

statement:
	//declaration
	//| labeledStmt
	//| simpleStmt
	//| goStmt
	//| returnStmt
	//| breakStmt
	//| continueStmt
	//| gotoStmt
	//| fallthroughStmt
	//| block
	//| ifStmt
	//| switchStmt
	//| selectStmt
	forStmt;
	//| deferStmt;


rangeClause: /*(
		expressionList ASSIGN
		| identifierList DECLARE_ASSIGN
	)?*/ RANGE expr;

forClause:
	initStmt = /*simpleStmt? eos */expr? /*eos*/ postStmt = expr?;