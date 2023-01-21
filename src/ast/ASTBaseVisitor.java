package ast;

public abstract class ASTBaseVisitor<T> {
	public void execute(AST root) {
		visit(root);
	}

	protected T visit(AST node) {
		switch(node.kind) {
			case PROGRAM_NODE:      return visitProgram(node);
			// *** STATEMENTS ***
			case BLOCK_NODE:        return visitBlock(node);
			case ASSIGN_NODE:       return visitAssign(node);
			case ASSIGN_LIST_NODE:  return visitAssignList(node);
			case VAR_DECL_NODE:     return visitVarDecl(node);
			case VAR_DECL_LIST_NODE: return visitVarDeclList(node);
			case SHORT_VAR_DECL_NODE:return visitShortVarDecl(node);
			case FUNC_DECL_NODE:    return visitFuncDecl(node);
			case RETURN_NODE:       return visitReturn(node);
			case IF_NODE:           return visitIf(node);
			case IF_CLAUSE_NODE:    return visitIfClause(node);
			case ELSE_NODE:         return visitElse(node);
			case FOR_NODE:          return visitFor(node);
			case FOR_CLAUSE_NODE:   return visitForClause(node);
			case INCREMENT:         return visitIncrement(node);
			case DECREMENT:         return visitDecrement(node);

			case READ_NODE:         return visitRead(node);
			case WRITE_NODE:        return visitWrite(node);
			case PRINT_NODE:        return visitPrint(node);

			// *** LITERALS ***
			case BOOL_LIT_NODE:     return visitBoolLit(node);
			case INT_LIT_NODE:      return visitIntLit(node);
			case FLOAT_LIT_NODE:    return visitFloatLit(node);
			case STR_LIT_NODE:      return visitStrLit(node);
			case NIL_LIT_NODE:      return visitNilLit(node);
			
			// *** OTHER OPERANDS ***
			case VAR_USE_NODE:      return visitVarUse(node);
			case FUNC_CALL_NODE:    return visitFuncCall(node);
			
			// *** OPERATIONS ***
			case MINUS_NODE:        return visitMinus(node);
			case PLUS_NODE:         return visitPlus(node);
			case DIV_NODE:          return visitDiv(node);
			case TIMES_NODE:        return visitTimes(node);
			case LESS_NODE:         return visitLess(node);
			case LESS_EQ_NODE:      return visitLessEqual(node);
			case GREATER_EQ_NODE:   return visitGreaterEqual(node);
			case GREATER_NODE:      return visitGreater(node);
			case EQ_NODE:           return visitEqual(node);
			case NOT_EQ_NODE:       return visitNotEqual(node);
			case AND_NODE:          return visitAnd(node);
			case OR_NODE:           return visitOr(node);
			case NOT_NODE:          return visitNot(node);

			case I2F_NODE:          return visitI2F(node);
			// case OVER_NODE:         return 
			// case EQ_PLUS_NODE:      return 
			// case EQ_MINUS_NODE:     return 
			// case EQ_TIMES_NODE:     return 
			// case EQ_DIV_NODE:       return 
			default:
				System.err.printf("Invalid kind: %s!\n", node.kind.toString());
				System.exit(1);
				return null; // NEVER REACHS
		}
	}

	protected abstract T visitAssign(AST node);

	protected abstract T visitShortVarDecl(AST node);

	protected abstract T visitProgram(AST node);

	protected abstract T visitBlock(AST node);

	protected abstract T visitAssignList(AST node);

	protected abstract T visitVarDecl(AST node);

	protected abstract T visitVarDeclList(AST node);

	protected abstract T visitFuncDecl(AST node);

	protected abstract T visitReturn(AST node);

	protected abstract T visitIf(AST node);

	protected abstract T visitIfClause(AST node);

	protected abstract T visitElse(AST node);

	protected abstract T visitFor(AST node);

	protected abstract T visitForClause(AST node);

	protected abstract T visitIncrement(AST node);

	protected abstract T visitDecrement(AST node);

	protected abstract T visitRead(AST node);

	protected abstract T visitWrite(AST node);

	protected abstract T visitPrint(AST node);

	protected abstract T visitIntLit(AST node);

	protected abstract T visitFloatLit(AST node);

	protected abstract T visitBoolLit(AST node);

	protected abstract T visitStrLit(AST node);

	protected abstract T visitNilLit(AST node);

	protected abstract T visitVarUse(AST node);

	protected abstract T visitFuncCall(AST node);

	protected abstract T visitMinus(AST node);

	protected abstract T visitPlus(AST node);

	protected abstract T visitDiv(AST node);

	protected abstract T visitTimes(AST node);

	protected abstract T visitLess(AST node);

	protected abstract T visitLessEqual(AST node);

	protected abstract T visitGreaterEqual(AST node);

	protected abstract T visitGreater(AST node);

	protected abstract T visitEqual(AST node);

	protected abstract T visitNotEqual(AST node);

	protected abstract T visitAnd(AST node);

	protected abstract T visitOr(AST node);

	protected abstract T visitNot(AST node);

	protected abstract T visitI2F(AST node);

}
