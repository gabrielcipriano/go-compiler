package checker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.GOParser;
import parser.GOParser.ExprContext;
import parser.GOParser.TypeContext;
import parser.GOParserBaseVisitor;
import tables.ScopeHandler;
import tables.StrTable;
import tables.VarEntry;
import tables.VarTable;
import typing.Type;
import static typing.Type.INT_TYPE;
import static typing.Type.FLOAT32_TYPE;
import static typing.Type.BOOLEAN_TYPE;
import static typing.Type.STRING_TYPE;
import static typing.Type.INFERED_TYPE;
import static typing.Type.NIL_TYPE;

import typing.SpecialType;

/*
 * Analisador semântico de EZLang implementado como um visitor
 * da ParseTree do ANTLR. A classe GOParserBaseVisitor é gerada
 * automaticamente e já possui métodos padrão aonde o comportamento
 * é visitar todos os filhos. Por conta disto, basta sobreescrever
 * os métodos que a gente quer alterar.
 * 
 * Por enquanto só há uma verificação simples de declaração de
 * variáveis usando uma tabela de símbolos. Funcionalidades adicionais
 * como análise de tipos serão incluídas no próximo laboratório.
 * 
 * O tipo Void indicado na super classe define o valor de retorno dos
 * métodos do visitador. Depois vamos alterar isso para poder construir
 * a AST.
 * 
 * Lembre que em um 'visitor' você é responsável por definir o
 * caminhamento nos filhos de um nó da ParseTree através da chamada
 * recursiva da função 'visit'. Ao contrário do 'listener' que
 * caminha automaticamente em profundidade pela ParseTree, se
 * você não chamar 'visit' nos métodos de visitação, o caminhamento
 * para no nó que você estiver, deixando toda a subárvore do nó atual
 * sem visitar. Tome cuidado neste ponto pois isto é uma fonte
 * muito comum de erros. Veja o método visitAssign_stmt abaixo para
 * ter um exemplo.
 */
public class SemanticChecker extends GOParserBaseVisitor<Type> {

	private StrTable st = new StrTable(); // Tabela de strings.
	private VarTable vt = new VarTable(); // Tabela de variáveis.
	private ScopeHandler sh = new ScopeHandler();

	private LinkedList<Type> typeQueue = new LinkedList<Type>();

	private Type lastInferType; // Variável "global" com o último tipo inferido.
	private Type lastDeclType; // Variável "global" com o último tipo declarado.
	private boolean infer = false;
	private boolean isArray = false;
	private boolean isFunction = false;

	private static Map<Integer, Type> basicLitTypeMap = Map.of(
		GOParser.NIL_LIT, NIL_TYPE,
		GOParser.INT_LIT, INT_TYPE,
		GOParser.STR_LIT, STRING_TYPE,
		GOParser.FLOAT_LIT, FLOAT32_TYPE,
		GOParser.TRUE_LIT, BOOLEAN_TYPE,
		GOParser.FALSE_LIT, BOOLEAN_TYPE
	);

	// Testa se o dado token foi declarado antes.
	private Type checkVar(Token token) {
		String varName = token.getText();
		int line = token.getLine();
		VarEntry entry = sh.lookupVar(varName);
		if (entry == null) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' was not declared.\n", line, varName);
			System.exit(1);
		}
		if (isArray && entry.special != SpecialType.ARRAY) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' is not an array.\n", line, varName);
			System.exit(1);
		}
		if (isFunction && entry.special != SpecialType.FUNCTION) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' is not a function.\n", line, varName);
			System.exit(1);
		}
		return entry.type;
	}

	// Cria uma nova variável a partir do dado token.
	private void newVar(Token token, Type type) {
		String varName = token.getText();
		int line = token.getLine();
		VarEntry entry = sh.lookupVar(varName);
		if (entry != null) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' already declared at line %d.\n", 
					line, varName, entry.line);
			System.exit(1);
		}
		if (isArray) {
			newArray(varName, line, type);
		} else if (isFunction) {
			newFunc(varName, line, type);
		} else {
			vt.addVar(varName, line, sh.scopeDepth, type);
			sh.addVar(varName, line, type);
		}
	}

	private void newFunc(String text, int line, Type type) {
		vt.addVar(text, line, sh.scopeDepth, type, SpecialType.FUNCTION);
		sh.addVar(text, line, type, SpecialType.FUNCTION);
		isFunction = false;
	}

	private void newArray(String text, int line, Type type) {
		vt.addVar(text, line, sh.scopeDepth, type, SpecialType.ARRAY);
		sh.addVar(text, line, type, SpecialType.ARRAY);
		isArray = false;
	}

	// Exibe o conteúdo das tabelas em stdout.
	public void printTables() {
		System.out.print("\n\n");
		System.out.print(st);
		System.out.print("\n\n");
		System.out.print(vt);
		System.out.print("\n\n");
	}

	private void mismatchedOperationError(int line, String expr, Type typeA, Type typeB) {
		System.err.printf(
			"SEMANTIC ERROR [Invalid operation] (%d):  %s (mismatched types %s and %s for operation.).\n", 
			line, expr, typeA.toString(), typeB.toString()
		);
		System.exit(1);
	}

	private void operatorNotDefinedError(int line, String expr, String operator, Type type) {
		System.err.printf(
			"SEMANTIC ERROR [Invalid operation] (%d): operator %s not defined on %s (%s).\n", 
			line, operator, expr, type.toString()
		);
		System.exit(1);
	}

	@Override
	public Type visitProgram(GOParser.ProgramContext ctx) {
		sh.push();
		return visitChildren(ctx); 
	}


	@Override
	public Type visitAssignee(GOParser.AssigneeContext ctx) {
		return checkVar(ctx.ID().getSymbol());
	}

	

	// TODO: verificar se os tipos das expressões estão batendo com os tipos dos identificadores

	@Override
	public Type visitAssignment(GOParser.AssignmentContext ctx) {
		LinkedList<Type> exprTypes = new LinkedList<Type>();
	
		for (ExprContext exprCtx : ctx.expr_list().expr())
			exprTypes.add(visit(exprCtx));

		return visitChildren(ctx);
	}

	@Override
	public Type visitIdentifier_list(GOParser.Identifier_listContext ctx) {
		for (TerminalNode id : ctx.ID()) {
			if (infer) {
				newVar(id.getSymbol(), typeQueue.removeFirst());
			} else {
				newVar(id.getSymbol(), lastDeclType);
			}
		}
		return null;
	}

	@Override
	public Type visitParameterDecl(GOParser.ParameterDeclContext ctx) {
		infer = false;
		this.lastDeclType = visit(ctx.type());
		visit(ctx.identifier_list());
		return null;
	}

	@Override
	public Type visitVar_spec(GOParser.Var_specContext ctx) { // var a, b int
		infer = false;
		this.lastDeclType = visit(ctx.type());
		LinkedList<Type> exprTypes = new LinkedList<Type>();
		
		if (ctx.expr_list() != null)
			for (ExprContext exprCtx : ctx.expr_list().expr())
				exprTypes.add(visit(exprCtx));

		visit(ctx.identifier_list());
		return null;
	}

	@Override
	public Type visitConst_spec(GOParser.Const_specContext ctx) {
		infer = false;
		// if (ctx.type() != null) {
		// 	infer = false;
		// 	visit(ctx.type());
		// }
		this.lastDeclType = visit(ctx.type());
		LinkedList<Type> exprTypes = new LinkedList<Type>();
		
		if (ctx.expr_list() != null)
			for (ExprContext exprCtx : ctx.expr_list().expr())
				exprTypes.add(visit(exprCtx));
	
		this.lastDeclType = visit(ctx.type());
		visit(ctx.identifier_list());
		return null;
	}

	@Override
	public Type visitFloatType(GOParser.FloatTypeContext ctx) {
		return FLOAT32_TYPE;
	}

	@Override
	public Type visitIntType(GOParser.IntTypeContext ctx) {
		return INT_TYPE;
	}

	@Override
	public Type visitStringType(GOParser.StringTypeContext ctx) {
		return STRING_TYPE;
	}

	@Override
	public Type visitBoolType(GOParser.BoolTypeContext ctx) {
		return BOOLEAN_TYPE;
	}

	// @Override
	// public Type visitOperand(GOParser.OperandContext ctx) {
	// 	return visit(ctx.operand_name());
	// }

	@Override
	public Type visitOperand_name(GOParser.Operand_nameContext ctx) {
		return checkVar(ctx.ID().getSymbol());
	}


	@Override
	public Type visitShortVar_decl(GOParser.ShortVar_declContext ctx) {
		// a, b := 1, "sim"
		// var a, b, c int
		//list lastDeclType<Type>

		// TODO Comparar a lista de variáveis com a lista de valores(quantidade)
		lastDeclType = INFERED_TYPE;
		LinkedList<Type> exprTypes = new LinkedList<Type>();

		for (ExprContext exprCtx : ctx.expr_list().expr())
			exprTypes.add(visit(exprCtx));

		visitChildren(ctx);
		return null;
	}

	// @Override 
	// public Type visitExpr_list(GOParser.Expr_listContext ctx) { 
	// 	 /* TODO Precisamos pegar o tipo do valor que está sendo atribuído
	// 	mesmo no caso de experssões e índices */
	// 	for (ExprContext expr : ctx.expr()) {
			
	// 	}
	// 	return null;
	// }

	@Override
	public Type visitBasicLiteral(GOParser.BasicLiteralContext ctx) {
		return basicLitTypeMap.get(ctx.basic.getType());
	}

	//isso aqui vai ser chato
	// @Override
	// public Type visitArrayLiteral(GOParser.ArrayLiteralContext ctx) {
	// }

	@Override
	public Type visitFunction_decl(GOParser.Function_declContext ctx) {
		LinkedList<Type> resultTypes = new LinkedList<Type>();

		if (ctx.result() != null)
			for (TypeContext typeCtx : ctx.result().type())
				resultTypes.add(visit(typeCtx));
		else
			resultTypes.add(INT_TYPE);

		isFunction = true;
		newVar(ctx.ID().getSymbol(), resultTypes.getFirst()); // TODO: Function table

		sh.push();
		visit(ctx.parameters());

		if(ctx.block() != null)
			visit(ctx.block());

		sh.pop();
		return null;
	}

	@Override
	public Type visitFor_stmt(GOParser.For_stmtContext ctx) {
		sh.push();
		visitChildren(ctx);
		sh.pop();
		return null;
	}

	@Override
	public Type visitIf_stmt(GOParser.If_stmtContext ctx) {
		sh.push();
		visitChildren(ctx);
		sh.pop();
		return null;
	}

	@Override
	public Type visitArrayType(GOParser.ArrayTypeContext ctx) {
		this.isArray = true;
		return visitChildren(ctx);
	}

	@Override
	public Type visitOperandExpr(GOParser.OperandExprContext ctx) {
		if(ctx.index() != null){
			isArray = true;
			Type type = visit(ctx.operand());
			isArray = false;
			visit(ctx.index());
			return type;
		}
		if(ctx.arguments() != null){ // TODO: checar argumentos expr_list com assinatura da funcao
			isFunction = true;
			Type type = visit(ctx.operand());
			isFunction = false;
			visit(ctx.arguments());
			return type;
		}
		return visit(ctx.operand());
	}

	@Override
	public Type visitUnary(GOParser.UnaryContext ctx) {
		Type type = visitChildren(ctx);
		boolean wasExpectingBoolType = ctx.NOT() != null && type != BOOLEAN_TYPE;
		boolean wasExpectingNumType = type != INT_TYPE && type != FLOAT32_TYPE;
		if (wasExpectingBoolType || wasExpectingNumType)
			operatorNotDefinedError(ctx.start.getLine(), ctx.getText(), ctx.unary_op.getText(), type);

		return type;
	}

	@Override
	public Type visitMultDiv(GOParser.MultDivContext ctx) {
		Type left = visit(ctx.expr(0));
		Type right = visit(ctx.expr(1));
		Type type = Type.numberTypeWidening(left, right);

		if (type == null)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left, right);

		return type;
	}

	@Override
	public Type visitPlusMinus(GOParser.PlusMinusContext ctx) {
		Type left = visit(ctx.expr(0));
		Type right = visit(ctx.expr(1));

		if (left == STRING_TYPE && right == STRING_TYPE && ctx.PLUS() != null)
			return STRING_TYPE;

		Type type = Type.numberTypeWidening(left, right);
		if (type == null)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left, right);

		return type;
	}

	@Override
	public Type visitRelation(GOParser.RelationContext ctx) {
		Type left = visit(ctx.expr(0));
		Type right = visit(ctx.expr(1));

		if (left == STRING_TYPE && right == STRING_TYPE)
			return STRING_TYPE;

		Type type = Type.numberTypeWidening(left, right);
		if (type == null)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left, right);

		return type;
	}

	@Override
	public Type visitAnd(GOParser.AndContext ctx) {
		Type left = visit(ctx.expr(0));
		Type right = visit(ctx.expr(1));

		if (left != BOOLEAN_TYPE || right != BOOLEAN_TYPE)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left, right);

		return BOOLEAN_TYPE;
	}

	@Override
	public Type visitOr(GOParser.OrContext ctx) {
		Type left = visit(ctx.expr(0));
		Type right = visit(ctx.expr(1));

		if (left != BOOLEAN_TYPE || right != BOOLEAN_TYPE)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left, right);
		
		return BOOLEAN_TYPE;
	}

}
