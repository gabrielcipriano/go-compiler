parser grammar GOParser;

options {
  tokenVocab = GOLexer;
}

@header {
    package parser;
}

program: 
	package_clause eos (
		(function_decl | const_decl | var_decl) eos
	)* EOF;

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

expr: 
  operand (index	| arguments)?						# operandExpr
| unary_op = (PLUS | MINUS | NOT) expr 		# unary
| expr mul_op = (STAR | DIV) expr					# multDiv
| expr add_op = (PLUS | MINUS) expr				# plusMinus
| expr rel_op = (
    EQ_EQ 
  | NOT_EQ
  | LESS
  | LESS_EQ
  | GREATER
  | GREATER_EQ
) expr 																		# relation
| expr AND_AND expr												# and
| expr OR expr														# or
;

// primary_expr:
// 	operand
// 	| primary_expr (
// 		index
// 		| arguments
// 	);


index: L_BRACKETS expr R_BRACKETS;

for_stmt: FOR (expr? | for_clause) block;

block: L_BRACES statement_list? R_BRACES;

statement_list: (statement eos)+;

statement:
	const_decl | var_decl
 	| simple_stmt
	| return_stmt
	| break_stmt
	| continue_stmt
	// | block
	| if_stmt
	| for_stmt
	| print;

print:
	PRINT L_PR expr_list R_PR;

function_decl: FUNC ID parameters result? block?;

parameters:
	L_PR (parameterDecl (COMMA parameterDecl)*)? R_PR;

arguments:
	L_PR expr_list? R_PR;

parameterDecl: ID type;

result: type | L_PR type (COMMA type)* R_PR;

// declaration: const_decl | var_decl;

const_decl: CONST const_spec;

var_decl: VAR var_spec;

const_spec: identifier_list type ASSIGN expr_list;

var_spec: identifier_list type (ASSIGN expr_list)?;

for_clause:
	init_stmt = simple_stmt? eos expr eos post_stmt = simple_stmt?;

if_stmt:
	IF 
		( expr
				| eos expr
				| simple_stmt eos expr
		) 
	block 
	(
		ELSE (if_stmt | block)
	)?;

simple_stmt:
	inc_dec_stmt
	| assignment
	| expression_stmt
	| shortVar_decl;

break_stmt: BREAK ID?;

continue_stmt: CONTINUE ID?;

inc_dec_stmt: expr (INCREMENT | DECREMENT);

return_stmt: RETURN expr_list?;

expression_stmt: expr;

shortVar_decl: identifier_list SHORT_VAR_DECL expr_list;

operand:
  literal | operand_name | L_PR expr R_PR;

operand_name:
	ID;

literal:
basic = (
  NIL_LIT
| INT_LIT
| STR_LIT
| FLOAT_LIT
| TRUE_LIT
| FALSE_LIT
)															# basicLiteral
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

array_type: L_BRACKETS INT_LIT R_BRACKETS type;

type:
  INT 							# intType
  | FLOAT32 				# floatType
  | STRING 					# stringType
  | BOOLEAN 				# boolType
  | array_type 			# arrayType
  | L_PR type R_PR	# brackets
  ;

eos:
	SEMI
	| EOF
	| EOS
	;
