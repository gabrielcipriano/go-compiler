parser grammar GOParser;

options {
  tokenVocab = GOLexer;
}

program: 
  stmt+
;

stmt:
  PRINT 
| op
;

assig:
  ID ASSIGN_OP expr;

expr: 
   expr;




binary_op  : 
  mul_op
| rel_op | add_op
| OR_OP| AND_AND_OP
;

rel_op     : 
  EQ_EQ_OP | NOT_EQ_OP| LESS_OP | LESS_EQ_OP | GREATER_OP | GREATER_EQ_OP ;
add_op     : 
  SUM_OP | DIFF_OP ;
mul_op     : 
  MULT_OP | QUOTIENT_OP ;

unary_op   : 
  SUM_OP | DIFF_OP | NOT_OP | MULT_OP ;

identifier_list :
  ID (COMMA ID)*
;
