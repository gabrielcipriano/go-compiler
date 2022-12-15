package checker;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.GOParser;
import parser.GOParser.AssigneeContext;
import parser.GOParser.ExprContext;
import parser.GOParser.ParameterDeclContext;
import parser.GOParser.StatementContext;
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
import static typing.Type.NIL_TYPE;
import static typing.Type.NO_TYPE;

import typing.SpecialType;

import ast.AST;
import ast.NodeKind;

public class SemanticChecker extends GOParserBaseVisitor<AST> {

	private StrTable st = new StrTable(); // Tabela de strings.
	private VarTable vt = new VarTable(); // Tabela de variáveis.
	private ScopeHandler sh = new ScopeHandler();

	private AST ast;

	private LinkedList<Type> exprTypesQueue = new LinkedList<Type>();

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
	private String text; // TODO: ma pq?

	// Testa se o dado token foi declarado antes.
	private AST checkVar(Token token) {
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
		if (isFunction) {
			if (entry.special != SpecialType.FUNCTION) {
				System.err.printf("SEMANTIC ERROR (%d): variable '%s' is not a function.\n", line, varName);
				System.exit(1);
			}
			return entry.funcReturn.size() > 0 ? entry.funcReturn.get(0): Type.NIL_TYPE;
		}
		//TODO 
		return new AST(NodeKind.VAR_USE_NODE,0 ,entry.type) ;
	}

	private void checkFuncArguments(VarEntry func, GOParser.ArgumentsContext ctx) {
		LinkedList<Type> argumentsTypes = getExprTypes(ctx.expr_list());

		if(func.funcParams.size() != argumentsTypes.size()){
			String expectedParams = Type.listToString(func.funcParams); 
			String givenParams = Type.listToString(argumentsTypes); 
			String qtt = func.funcParams.size() > argumentsTypes.size() ? "not enough" : "too many";
				
			System.err.printf("SEMANTIC ERROR (%d): %s arguments in call to %s\n \t have (%s)\n\t want (%s)\n",
				ctx.start.getLine(),qtt,func.name,expectedParams,givenParams);
			System.exit(1);
		}
		for (Type paramType : func.funcParams) {
			Type atype = argumentsTypes.removeFirst();
			if (paramType == atype || (paramType == FLOAT32_TYPE && atype == INT_TYPE))
				continue;
			diffTypeError(ctx.start.getLine(),"argument to function", ctx.getText(), atype, paramType);
		}
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
		} /*else if (isFunction) {
			newFunc(varName, line, type);
		} */else {
			vt.addVar(varName, line, sh.scopeDepth, type);
			sh.addVar(varName, line, type);
		}
	}

	private void newFunc(Token token, LinkedList<Type> argList, LinkedList<Type> returnList ) {
		String varName = token.getText();
		int line = token.getLine();
		VarEntry entry = sh.lookupVar(varName);
		if (entry != null) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' already declared at line %d.\n", 
					line, varName, entry.line);
			System.exit(1);
		}
		
		vt.addVar(varName, line, sh.scopeDepth, !returnList.isEmpty()? returnList.getFirst():Type.NIL_TYPE, SpecialType.FUNCTION);
		sh.addVar(varName, line, SpecialType.FUNCTION,argList,returnList);
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

	private void assignmentMismatchError(int line, String txt, int vars, int vals) {
		System.err.printf(
			"SEMANTIC ERROR [assignment mismatch] (%d):  %s (%d variables but %d value).\n", 
			line, txt, vars, vals);
		System.exit(1);
	}

	private void diffTypeError(int line,  String where, String txtVal, Type givenType, Type expectedType) {
		System.err.printf(
			"SEMANTIC ERROR (%d):  cannot use %s (%s) as %s value in %s.\n", 
			line, txtVal, givenType.getName(), expectedType.getName(), where);
		System.exit(1);
	};

	@Override
	public AST visitProgram(GOParser.ProgramContext ctx) {
		sh.push();
		return visitChildren(ctx);
	}


	@Override
	public AST visitAssignee(GOParser.AssigneeContext ctx) {
		return checkVar(ctx.ID().getSymbol());
	}

	private LinkedList<Type> getExprTypes(GOParser.Expr_listContext ctx) {
		LinkedList<Type> exprTypes = new LinkedList<Type>();
		if(ctx == null) return exprTypes;
		for (ExprContext exprCtx : ctx.expr())
			exprTypes.add(visit(exprCtx));
		return exprTypes;
	}

	// TODO: verificar se os tipos das expressões estão batendo com os tipos dos identificadores

	@Override
	public Type visitAssignment(GOParser.AssignmentContext ctx) {
		LinkedList<Type> exprTypes = getExprTypes(ctx.expr_list());

		int exprSz = exprTypes.size();
		int valsSz = ctx.assignee_list().assignee().size();

		if(exprSz != valsSz)
			assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSz, valsSz);
		
		for (AssigneeContext assignee : ctx.assignee_list().assignee()) {
			if (assignee.index() != null)
				isArray = true;
			Type assignType = checkVar(assignee.ID().getSymbol());
			Type exprType = exprTypes.removeFirst();

			if (assignType == exprType || (assignType == FLOAT32_TYPE && exprType == INT_TYPE))
				continue;
			
			diffTypeError(ctx.start.getLine(),"variable assignment", ctx.expr_list().getText(), exprType, assignType);
		}

		return visitChildren(ctx);
	}

	@Override
	public Type visitParameterDecl(GOParser.ParameterDeclContext ctx) {
		// infer = false;
		newVar(ctx.ID().getSymbol(), visit(ctx.type()));
		// this.lastDeclType = visit(ctx.type());
		// visit(ctx.identifier_list());
		return null;
	}


	@Override
	public Type visitVar_spec(GOParser.Var_specContext ctx) { // var a, b int = 1, 2 || var c, d string
		Type type = visit(ctx.type());
		
		LinkedList<Type> exprTypes = ctx.expr_list() != null ? getExprTypes(ctx.expr_list()) : null;

		if (exprTypes != null) {
			int exprSz = exprTypes.size();
			int valsSz = ctx.identifier_list().ID().size();
			if(exprSz != valsSz)
				assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSz, valsSz);
			
			for (Type t : exprTypes)
				if (t != type)
					diffTypeError(ctx.start.getLine(), "variable declaration", ctx.expr_list().getText(), t, type);
		}

		
		// // TODO: atribuindo as variaveis ao(s) retorno(s) de uma função
		// if (ctx.parameters() != null) { // cannot use sum(1, 2) (value of type int) as type string in assignment
		// 	VarEntry func = sh.lookupVar(ctx.funcOrExprList().operand_name().ID().getText());
		// 	for (Type returnType : func.funcReturn)
		// 		if(returnType != type)
		// 			diffTypeError(ctx.start.getLine(), "assignment", ctx.funcOrExprList().operand_name().ID().getText() + ctx.funcOrExprList().parameters().getText(), returnType, type);
		// }

		for (TerminalNode id : ctx.identifier_list().ID())
			newVar(id.getSymbol(), type);

		return null;
	}

	@Override
	public Type visitConst_spec(GOParser.Const_specContext ctx) {
		Type type = visit(ctx.type());
		
		LinkedList<Type> exprTypes = ctx.expr_list() != null ? getExprTypes(ctx.expr_list()) : null;

		int exprSz = exprTypes.size();
		int valsSz = ctx.identifier_list().ID().size();

		if(exprSz != valsSz)
			assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSz, valsSz);

		for (Type t : exprTypes)
			if (t != type)
				diffTypeError(ctx.start.getLine(), "variable declaration", ctx.expr_list().getText(), t, type);
		
		for (TerminalNode id : ctx.identifier_list().ID())
			newVar(id.getSymbol(), type);

		return null;
	}

	@Override
	public Type visitShortVar_decl(GOParser.ShortVar_declContext ctx) {
		LinkedList<Type> exprTypes = getExprTypes(ctx.expr_list());

		int exprSz = exprTypes.size();
		int valsSz = ctx.identifier_list().ID().size();

		if(exprSz != valsSz)
			assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSz, valsSz);
		
		for (TerminalNode id : ctx.identifier_list().ID())
			newVar(id.getSymbol(), exprTypes.removeFirst());

		return null;
	}

	@Override
	public AST visitFloatType(GOParser.FloatTypeContext ctx) {
		float number = Float.parseFloat(ctx.getText());
		return new AST(NodeKind.FLOAT_LIT_NODE,number,Type.FLOAT32_TYPE);
	}

	@Override
	public AST visitIntType(GOParser.IntTypeContext ctx) {
		int number = Integer.parseInt(ctx.getText());
		return new AST(NodeKind.INT_LIT_NODE,number,Type.INT_TYPE);
	}

	@Override
	public AST visitStringType(GOParser.StringTypeContext ctx) {
		String str = ctx.getText();
		//TODO tabela de String
		return STRING_TYPE;
	}

	@Override
	public AST visitBoolType(GOParser.BoolTypeContext ctx) {
		int bool = ctx.getText() == "true"? 1 : 0;
		return new AST(NodeKind.BOOL_LIT_NODE,bool,Type.BOOLEAN_TYPE);
	}

	@Override
	public AST visitOperand_name(GOParser.Operand_nameContext ctx) {
		return checkVar(ctx.ID().getSymbol());
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
		LinkedList<Type> argsTypes = new LinkedList<Type>();

		if (ctx.parameters().parameterDecl() != null)
			for (ParameterDeclContext paramCtx : ctx.parameters().parameterDecl())
				argsTypes.add(visit(paramCtx.type()));

		if (ctx.result() != null)
			for (TypeContext typeCtx : ctx.result().type())
				resultTypes.add(visit(typeCtx));

		newFunc(ctx.ID().getSymbol(),argsTypes,resultTypes); 
		
		sh.push();
		visit(ctx.parameters());

		if(ctx.block() != null)
			visit(ctx.block());

		sh.pop();
		return null;
	}

	@Override
	public AST visitFor_stmt(GOParser.For_stmtContext ctx) {
		AST blockNode = AST.newSubtree(NodeKind.BLOCK_NODE, NO_TYPE);
		AST forNode = AST.newSubtree(NodeKind.FOR_NODE, NO_TYPE,blockNode);
		sh.push();
		//TODO criar a arvore para comparação
		for(StatementContext stmt :ctx.block().statement_list().statement()){
			AST children = visitChildren(stmt);
			blockNode.addChild(children);
		}
		sh.pop();
		return forNode;
	}

	@Override
	public AST visitIf_stmt(GOParser.If_stmtContext ctx) {
		AST blockNode = AST.newSubtree(NodeKind.BLOCK_NODE, NO_TYPE);
		AST ifNode = AST.newSubtree(NodeKind.IF_NODE, Type.NO_TYPE,blockNode);
		sh.push();
		//TODO criar a arvore para comparação
		for(StatementContext stmt :ctx.block(0).statement_list().statement()){
			AST children = visitChildren(stmt);
			blockNode.addChild(children);
		}
		if(ctx.ELSE() != null){
			AST elseNode;
			if(ctx.if_stmt()!= null)
				elseNode = AST.newSubtree(NodeKind.ELSE_NODE, Type.NO_TYPE,visit(ctx.if_stmt()));
			else {
				AST blockElse = AST.newSubtree(NodeKind.BLOCK_NODE, NO_TYPE);
				elseNode = AST.newSubtree(NodeKind.IF_NODE, Type.NO_TYPE,blockElse);
				for(StatementContext stmt :ctx.block(1).statement_list().statement()){
					AST children = visitChildren(stmt);
					blockElse.addChild(children);
				}
			}
			ifNode.addChild(elseNode);
		}
		sh.pop();
		return ifNode;
	}

	@Override
	public Type visitFunccall_stmt(GOParser.Funccall_stmtContext ctx) {
		isFunction = true;
		checkVar(ctx.ID().getSymbol());
		isFunction = false;

		VarEntry func = sh.lookupVar(ctx.ID().getText());
		checkFuncArguments(func, ctx.arguments());

		return null;
	}

	@Override
	public Type visitArrayType(GOParser.ArrayTypeContext ctx) {
		this.isArray = true;
		return visitChildren(ctx);
	}

//#region Expressions
	@Override
	public Type visitOperandExpr(GOParser.OperandExprContext ctx) {		if(ctx.index() != null){
			isArray = true;
			Type type = visit(ctx.operand());
			isArray = false;
			visit(ctx.index()); // TODO verificar se index é um inteiro
			return type;
		}
		if(ctx.arguments() != null){
			isFunction = true;
			Token nameFunc = ctx.operand().operand_name().ID().getSymbol();
			checkVar(nameFunc);
			isFunction = false;
			// visit(ctx.arguments());
			LinkedList<Type> argumentsTypes = getExprTypes(ctx.arguments().expr_list());
			String funcName = nameFunc.getText();
			VarEntry func = sh.lookupVar(funcName);

			checkFuncArguments(func, ctx.arguments());

			// função em uma expressão deve ter um unico valor de retorno
			if (func.funcReturn.size() == 1) 
				return func.funcReturn.get(0);
			
			if (func.funcReturn.size() == 0) {
				System.err.printf("SEMANTIC ERROR (%d): %s (no value) used as value", nameFunc.getLine(),ctx.getText());
			} else {
				System.err.printf("SEMANTIC ERROR (%d):  multiple-value %s (value of type (%s)) in single-value context\n",
					nameFunc.getLine(),ctx.getText(),Type.listToString(func.funcReturn));
			}
			System.exit(1);
		}
		return visit(ctx.operand());
	}

	@Override
	public Type visitOperand(GOParser.OperandContext ctx) { // literal | operand_name | L_PR expr R_PR;
		if (ctx.literal() != null)
			return visit(ctx.literal());
		if(ctx.expr() != null)
			return visit(ctx.expr());
		return visit(ctx.operand_name());
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
	public AST visitPlusMinus(GOParser.PlusMinusContext ctx) {
		AST l = visit(ctx.expr(0));
		AST r = visit(ctx.expr(1));

		NodeKind node = null;

		if (ctx.PLUS() != null) {
			node = NodeKind.PLUS_NODE;
		} else if ( ctx.MINUS() != null) {
			node = NodeKind.MINUS_NODE;
		}

		
		if (l.type == STRING_TYPE && r.type == STRING_TYPE)
			return AST.newSubtree(node, STRING_TYPE, l, r);

		if (!Type.isBothNumbers(l.type, r.type))
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), l.type, r.type);

		if (Type.isI2FWideningNeeded(l.type, r.type)){
			if (Type.isI2FTarget(l.type) ){

			}
		}
				
		return AST.newSubtree(node, NO_TYPE, l, r);
	}

	@Override
	public AST visitRelation(GOParser.RelationContext ctx) {
		AST left = visit(ctx.expr(0));
		AST right = visit(ctx.expr(1));

		// Convert from Token to NodeKind

		NodeKind node = null;

		if (ctx.EQ_EQ() != null) {
			node = NodeKind.EQ_NODE;
		} else if (ctx.NOT_EQ() != null) {
			node = NodeKind.NOT_EQ_NODE;			
		} else if(ctx.LESS() != null) {
			node = NodeKind.LESS_NODE;
		} else if(ctx.LESS_EQ() != null) {
			node = NodeKind.LESS_EQ_NODE; 
		} else if(ctx.GREATER() != null) {
			node = NodeKind.GREATER_NODE;
		} else if(ctx.GREATER_EQ() != null) {
			node = NodeKind.GREATER_EQ_NODE;
		}

		if (left.type == STRING_TYPE && right.type == STRING_TYPE)
			return AST.newSubtree(node, STRING_TYPE, left, right);

		Type type = Type.numberTypeWidening(left.type, right.type);
		if (type == null)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left.type, right.type);

		return AST.newSubtree(node, BOOLEAN_TYPE, left, right);
	}

	@Override
	public AST visitAnd(GOParser.AndContext ctx) {
		AST l = visit(ctx.expr(0));
		AST r = visit(ctx.expr(1));

		if (l.type != BOOLEAN_TYPE || r.type != BOOLEAN_TYPE)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), l.type, r.type);

		return AST.newSubtree(NodeKind.AND_NODE, BOOLEAN_TYPE, l, r);
	}

	@Override
	public AST visitOr(GOParser.OrContext ctx) {
		AST left = visit(ctx.expr(0));
		AST right = visit(ctx.expr(1));

		if (left.type != BOOLEAN_TYPE || right.type != BOOLEAN_TYPE)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left.type, right.type);
		
		return AST.newSubtree(NodeKind.OR_NODE, BOOLEAN_TYPE, left, right);
	}

//#endregion

}
