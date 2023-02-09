parser grammar GOParser;

options {
  tokenVocab = GOLexer;
}

@header {
    package parser;
}

program: 
	package_clause eos (program_sect)* EOF;
	// ((if_stmt | for_stmt) eos)* EOF;

program_sect: (function_decl | const_decl | var_decl) eos;


package_clause: PACKAGE packageName = ID;

assignment:
	assignee_list assign_op expr_list;

assignee_list:
	assignee (COMMA assignee)*;

assignee:
	ID index?;

assign_op: op = (
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

func:
	len | rand;

expr: 
len																# funcExpr
| arr_address													# arrAddress
| operand (index	| arguments)?			# operandExpr
| unary_op = (PLUS | MINUS | NOT) expr 		# unary
| expr mul_op = (STAR | DIV) expr			# multDiv
| expr add_op = (PLUS | MINUS) expr			# plusMinus
| expr rel_op = (
    EQ_EQ 
  | NOT_EQ
  | LESS
  | LESS_EQ
  | GREATER
  | GREATER_EQ
) expr 										# relation
| expr AND_AND expr							# and
| expr OR expr								# or
;

// primary_expr:
// 	operand
// 	| primary_expr (
// 		index
// 		| arguments
// 	);

index: L_BRACKETS expr R_BRACKETS;

for_stmt: FOR (expr? | for_clause) block;

for_clause:
	init_stmt = simple_stmt? eos expr eos post_stmt = simple_stmt?;

block: L_BRACES statement_list? R_BRACES;

statement_list: (statement eos)+;

statement:
	const_decl | var_decl
 	| simple_stmt
	| return_stmt
	| break_stmt
	//function_call
	| continue_stmt
	// | block
	| if_stmt
	| for_stmt
	| print
	| scan;
/*
function_call:
	ID arguments
 */

scan:
	SCAN ID R_PR;

rand:
	RAND L_PR expr R_PR;

len:
	LEN L_PR ID R_PR;

print:
	PRINT L_PR expr_list R_PR;

function_decl: FUNC ID parameters result? block; // block obrigatorio na nossa implementacao

parameters:
	L_PR (parameterDecl (COMMA parameterDecl)*)? R_PR;

arguments:
	L_PR expr_list? R_PR;

parameterDecl: ID type; // simplifying parameter declaration

result: type | L_PR type (COMMA type)* R_PR;

// declaration: const_decl | var_decl;

const_decl: CONST const_spec;

var_decl: VAR var_spec;

const_spec: identifier_list type ASSIGN expr_list;

// var a int = foo(1, 2)
var_spec: identifier_list type (ASSIGN expr_list)?;

// funcOrExprList: 
// 	operand_name parameters
// 	| L_PR  expr_list R_PR;
// 	| expr

if_stmt:
	IF 
		( expr // if a := 1 { ... }
				| eos expr // if ; a == 1 { ... }
				| simple_stmt eos expr // if a := 1; a == 1 { ... }
		) 
	block 
	(
		ELSE (if_stmt | block)
	)?	;

simple_stmt:
	inc_dec_stmt
	| assignment
	| funccall_stmt
	| shortVar_decl;

break_stmt: BREAK ID?;

continue_stmt: CONTINUE ID?;

inc_dec_stmt: operand_name index? (INCREMENT | DECREMENT); // a = ++b

return_stmt: RETURN expr_list?;

// expression_stmt: expr;

funccall_stmt: ID arguments;

shortVar_decl: identifier_list SHORT_VAR_DECL expr_list;

operand:
  len | rand
  |literal | operand_name | L_PR expr R_PR;

operand_name:
	ID;

literal:
//basic = (
  NIL_LIT					#nilVal
| INT_LIT					#intVal
| STR_LIT					#strVal
| FLOAT_LIT					#floatVal
| TRUE_LIT					#trueVal
| FALSE_LIT					#falseVal
//)															# basicLiteral
| array_type literal_array    # arrayLiteral
;


// composite_lit: array_type literal_array;

literal_array: L_BRACES element_list? R_BRACES;

// basic_lit:
//   NIL_LIT
// | INT_LIT
// | STR_LIT
// | FLOAT_LIT;

// composite_lit: 

// literal_type:
//   array_type;

element_list: expr (COMMA expr)*;

type_name: ID;

array_type: L_BRACKETS INT_LIT? R_BRACKETS type;

arr_address: ADDRESS ID;

type:
  INT 							# intType
  | FLOAT32 				# floatType
  | STRING 					# stringType
  | BOOLEAN 				# boolType
  | array_type 			# arrayType
  ;

eos:
	SEMI
	| EOF
	| EOS
	;
