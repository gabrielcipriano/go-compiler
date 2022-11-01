parser grammar GOParser;

options {
  tokenVocab = GOLexer;
}

@header {
    package parser;
}

program: 
	package_clause eos (
		(function_decl | declaration) eos
	)* EOF;

package_clause: PACKAGE packageName = ID;

assignment:
  assignee_list assign_op expr_list;

assignee_list:
	 assignee (COMMA assignee)*;

assignee:
	ID index?;

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

primary_expr:
	operand
	| primary_expr (
		index
		| arguments
	);

index: L_BRACKETS expr R_BRACKETS;
/*simple_stmt:
	| inc_dec_stmt
	| assignment
	| expression_stmt
	| shortVar_decl;

expression_stmt : 
  expr;*/

for_stmt: FOR (expr? | for_clause) block;

block: L_BRACES statement_list? R_BRACES;

statement_list: (statement eos)+;

statement:
	declaration
 	| simple_stmt
	| return_stmt
	| break_stmt
	| continue_stmt
	| block
	| if_stmt
	| for_stmt
	| print;

print:
	PRINT L_PR expr_list R_PR;

function_decl: FUNC ID (signature block?);

signature:
	parameters result
	| parameters;
	
parameters:
	L_PR (parameterDecl (COMMA parameterDecl)* COMMA?)? R_PR;

arguments: 
	 L_PR expr_list? R_PR;

parameterDecl: identifier_list? type;

result: parameters | type;

declaration: const_decl | var_decl;

const_decl: CONST (const_spec | L_PR (const_spec eos)* R_PR);

var_decl: VAR (var_spec | L_PR (var_spec eos)* R_PR);

const_spec: identifier_list (type? ASSIGN expr_list);

var_spec:
	identifier_list (
		type (ASSIGN expr_list)?
		| ASSIGN expr_list
	);

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
	basic_lit | composite_lit 
;

composite_lit: literal_type literal_value;

literal_value: L_BRACES element_list? R_BRACES;

basic_lit:
  NIL_LIT
| INT_LIT
| STR_LIT
| FLOAT_LIT;

// composite_lit: 

literal_type:
  array_type;

element_list: expr (COMMA expr)*;

type_name: ID;

array_type: L_BRACKETS expr R_BRACKETS type;

type:
  INT | FLOAT32 | STRING | BOOLEAN | literal_type | L_PR type R_PR;

eos:
	SEMI
	| EOF
	| EOS
	;
