package checker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.GOParser;
import parser.GOParser.Program_sectContext;
import parser.GOParser.AssigneeContext;
import parser.GOParser.ExprContext;
import parser.GOParser.ParameterDeclContext;
import parser.GOParser.StatementContext;
import parser.GOParser.TypeContext;
import parser.GOParserBaseVisitor;
import tables.FunctionEntry;
import tables.FunctionTable;
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

import typing.Conv;
import typing.SpecialType;

import ast.AST;
import ast.NodeKind;

public class SemanticChecker extends GOParserBaseVisitor<AST> {

	private StrTable 		st = new StrTable(); // Tabela de strings.
	private FunctionTable 	ft = new FunctionTable();
	private VarTable 		vt = new VarTable(); // Tabela de variáveis.
	private ScopeHandler 	sh = new ScopeHandler();

	private AST root;

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
	private static Map<Type, NodeKind> nodeKindTypeMap = Map.of(
		INT_TYPE,NodeKind.INT_LIT_NODE,
		STRING_TYPE, NodeKind.STR_LIT_NODE,
		FLOAT32_TYPE, NodeKind.FLOAT_LIT_NODE,
		BOOLEAN_TYPE, NodeKind.BOOL_LIT_NODE
	);
	// Exibe a AST no formato DOT em stderr.
	public void printAST() {
		AST.printDot(root, vt, ft);
	}

	private void PANIC(String format, Object ... args) {
		System.err.printf(format, args);
		System.exit(1);
	}

	// Testa se o dado token foi declarado antes.
	private AST checkVar(Token token) {
		String varName = token.getText();
		int line = token.getLine();

		if (isFunction) {
			int idx = ft.lookupVar(varName);
			if (idx == -1)
				PANIC("SEMANTIC ERROR (%d): function '%s' was not declared.\n", line, varName);
			return new AST(NodeKind.FUNC_CALL_NODE, idx, ft.get(idx).returns.get(0)); // considerando atualmente um unico retorno
		}

		VarEntry entry = sh.lookupVar(varName);
		if (entry == null)
			PANIC("SEMANTIC ERROR (%d): variable '%s' was not declared.\n", line, varName);
		if (isArray && !entry.isArray)
			PANIC("SEMANTIC ERROR (%d): variable '%s' is not an array.\n", line, varName);
		if (!isArray && entry.isArray)
			PANIC("SEMANTIC ERROR (%d): variable '%s' is an array.\n", line, varName);
		int idx = vt.lookupVar(entry.name, entry.scope);
		return new AST(NodeKind.VAR_USE_NODE, idx, entry.type);
	}

	private List<AST> checkFuncArguments(FunctionEntry func, GOParser.ArgumentsContext ctx) {
		List<AST> exprNodes = getExprNodes(ctx.expr_list());

		if(func.params.size() != exprNodes.size()){
			String expectedParams = Type.listToString(func.params); 
			String givenParams = AST.typesToString(exprNodes);
			String qtt = func.params.size() > exprNodes.size() ? "not enough" : "too many";
				
			PANIC("SEMANTIC ERROR (%d): %s arguments in call to %s\n \t have (%s)\n\t want (%s)\n",
				ctx.start.getLine(),qtt,func.name,expectedParams,givenParams);
		}

		List<AST> argumentNodes = getExprNodes(ctx.expr_list());
		for (int i = 0; i < exprNodes.size(); i++) {
			var argNode = argumentNodes.get(i);
			if(argNode.type == INT_TYPE && func.params.get(i) == FLOAT32_TYPE) {
				argumentNodes.add(AST.createConvNode(Conv.I2F, argNode));
				continue;
			}
			if(argNode.type != func.params.get(i))
				diffTypeError(ctx.start.getLine(),"argument to function", ctx.getText(), argNode.type, func.params.get(i));
			argumentNodes.add(argNode);
		}
		return argumentNodes;
	}

	// Cria uma nova variável a partir do dado token.
	private int newVar(Token token, Type type) {
		String varName = token.getText();
		int line = token.getLine();
		VarEntry entry = sh.lookupVar(varName);
		if (entry != null)
			PANIC("SEMANTIC ERROR (%d): variable '%s' already declared at line %d.\n", line, varName, entry.line);
		if (ft.lookupVar(varName) != -1)
			PANIC("SEMANTIC ERROR (%d): name '%s' already declared as function at line %d.\n", line, varName, entry.line);

		if (isArray) {
			return newArray(varName, line, type);
		} /*else if (isFunction) {
			newFunc(varName, line, type);
		} */else {
			sh.addVar(varName, line, type);
			return vt.addVar(varName, line, sh.scopeDepth, type);
		}
	}

	private int newFunc(Token token, List<Type> argList, List<Type> returnList ) {
		String funcName = token.getText();
		int line = token.getLine();
		VarEntry entry = sh.lookupVar(funcName);
		if (entry != null)
			PANIC("SEMANTIC ERROR (%d): variable '%s' already declared at line %d.\n", 
					line, funcName, entry.line);

		if (ft.lookupVar(funcName) != -1)
			PANIC("SEMANTIC ERROR (%d): function '%s' already declared at line %d.\n", 
				line, funcName,ft.get(ft.lookupVar(funcName)).line);

		int idx = ft.addFunction(funcName, line, argList, returnList);
		isFunction = false;
		return idx;
	}

	private int newArray(String text, int line, Type type) {
		int idx = vt.addVar(text, line, sh.scopeDepth, type, true);
		sh.addVar(text, line, type, true);
		isArray = false;
		return idx;
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
		PANIC("SEMANTIC ERROR [Invalid operation] (%d):  %s (mismatched types %s and %s for operation.).\n", 
			line, expr, typeA.toString(), typeB.toString());
	}

	private void operatorNotDefinedError(int line, String expr, String operator, Type type) {
		PANIC("SEMANTIC ERROR [Invalid operation] (%d): operator %s not defined on %s (%s).\n", 
			line, operator, expr, type.toString());
	}

	private void assignmentMismatchError(int line, String txt, int vars, int vals) {
		PANIC("SEMANTIC ERROR [assignment mismatch] (%d):  %s (%d variables but %d value).\n", 
			line, txt, vars, vals);
	}

	private void diffTypeError(int line,  String where, String txtVal, Type givenType, Type expectedType) {
		PANIC("SEMANTIC ERROR (%d):  cannot use %s (%s) as %s value in %s.\n", 
			line, txtVal, givenType.getName(), expectedType.getName(), where);
	};

	@Override
	public AST visitProgram(GOParser.ProgramContext ctx) {
		sh.push();
		this.root = AST.newSubtree(NodeKind.PROGRAM_NODE, NO_TYPE);
		visit(ctx.package_clause());
		for(var line : ctx.program_sect()){
			AST children = visit(line);
			if(children!= null){
				root.addChild(children);
			}
		}
		return root;
	}

	@Override
	public AST visitProgram_sect(GOParser.Program_sectContext ctx){
		if(ctx.var_decl() != null) return visit(ctx.var_decl());
		if(ctx.const_decl() != null) return visit(ctx.const_decl());
		return visit(ctx.function_decl());
	}

	@Override
	public AST visitBlock(GOParser.BlockContext ctx){
		AST block = AST.newSubtree(NodeKind.BLOCK_NODE, NO_TYPE);
		for(var line : ctx.statement_list().statement()){
			AST children = visit(line);
			if(children!= null){
				block.addChild(children);
			}
		}
		return block;
	}


	// @Override
	// public AST visitAssignee(GOParser.AssigneeContext ctx) {
	// 	return checkVar(ctx.ID().getSymbol());
	// }

	private List<AST> getExprNodes(GOParser.Expr_listContext ctx) {
		List<AST> exprNodes = new LinkedList<AST>();
		if(ctx == null) return exprNodes;
		for (ExprContext exprCtx : ctx.expr())
			exprNodes.add(visit(exprCtx));
		return exprNodes;
	}

	// TODO: verificar se os tipos das expressões estão batendo com os tipos dos identificadores

	
	@Override
	public AST visitAssignment(GOParser.AssignmentContext ctx) {
		List<AST> exprList = getExprNodes(ctx.expr_list());

		int exprSz = exprList.size();
		int valsSz = ctx.assignee_list().assignee().size();
		
		if(exprSz != valsSz)
			assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSz, valsSz);
		AST varListNode = AST.newSubtree(NodeKind.ASSIGN_LIST_NODE, NO_TYPE);
		for (AssigneeContext assignee : ctx.assignee_list().assignee()) {
			NodeKind nodeType = NodeKind.ASSIGN_NODE;
			// switch for operation
			if (ctx.assign_op().op != null) {
				switch (ctx.assign_op().op.getType()) {
					case GOParser.PLUS:
						nodeType = NodeKind.EQ_PLUS_NODE;
						break;
					case GOParser.MINUS:
						nodeType = NodeKind.EQ_MINUS_NODE;
						break;
					case GOParser.STAR:
						nodeType = NodeKind.EQ_TIMES_NODE;
						break;
					default:
						nodeType = NodeKind.EQ_DIV_NODE;
						break;
				}
			}
			
			AST assignment = AST.newSubtree(nodeType, NO_TYPE);
			if (assignee.index() != null)
				isArray = true;
			AST assignType = checkVar(assignee.ID().getSymbol());
			AST exprType = exprList.remove(0);
			if (assignType.type != exprType.type && !(assignType.type == FLOAT32_TYPE && exprType.type == INT_TYPE))
				diffTypeError(ctx.start.getLine(),"variable assignment", ctx.expr_list().getText(), exprType.type, assignType.type);
			assignment.addChild(assignType);
			assignment.addChild(exprType);
			varListNode.addChild(assignment);
			
		}

		return varListNode;
	}

	// @Override
	// public AST visitVar_decl(GOParser.Var_declContext ctx){
	// 	AST children = visit(ctx.var_spec());
	// 	return children;
	// }

	@Override
	public AST visitVar_spec(GOParser.Var_specContext ctx) { // var a, b int = 1, 2 || var c, d string
		AST type = visit(ctx.type());
		AST varListNode = AST.newSubtree(NodeKind.VAR_DECL_LIST_NODE, NO_TYPE);
		List<AST> exprTypes = ctx.expr_list() != null ? getExprNodes(ctx.expr_list()) : null;

		if (exprTypes != null) {
			int exprSz = exprTypes.size();
			int valsSz = ctx.identifier_list().ID().size();
			if(exprSz != valsSz)
				assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSz, valsSz);
			
			for (AST t : exprTypes)
				if (t.type != type.type && !(type.type == Type.FLOAT32_TYPE && t.type == Type.INT_TYPE))
					diffTypeError(ctx.start.getLine(), "variable declaration", ctx.expr_list().getText(), t.type, type.type);
		}

		
		// // TODO: atribuindo as variaveis ao(s) retorno(s) de uma função
		// if (ctx.parameters() != null) { // cannot use sum(1, 2) (value of type int) as type string in assignment
		// 	VarEntry func = sh.lookupVar(ctx.funcOrExprList().operand_name().ID().getText());
		// 	for (Type returnType : func.funcReturn)
		// 		if(returnType != type)
		// 			diffTypeError(ctx.start.getLine(), "assignment", ctx.funcOrExprList().operand_name().ID().getText() + ctx.funcOrExprList().parameters().getText(), returnType, type);
		// }
		if(exprTypes == null)
			for (TerminalNode id : ctx.identifier_list().ID()){
				int idx = newVar(id.getSymbol(), type.type);
				varListNode.addChild(new AST(NodeKind.VAR_DECL_NODE,idx,type.type));
			}
		else 
			for (TerminalNode id : ctx.identifier_list().ID()){
				int idx = newVar(id.getSymbol(), type.type);
				AST children = new AST(NodeKind.VAR_DECL_NODE,idx,type.type);
				AST assingNode = AST.newSubtree(NodeKind.ASSIGN_NODE, NO_TYPE,children,exprTypes.remove(0));
				varListNode.addChild(assingNode);
			}
		return varListNode;
	}

	@Override
	public AST visitConst_spec(GOParser.Const_specContext ctx) {
		Type type = visit(ctx.type()).type;
		AST varListNode = AST.newSubtree(NodeKind.VAR_DECL_LIST_NODE, NO_TYPE);
		List<AST> exprTypes = getExprNodes(ctx.expr_list());

		int exprSize = exprTypes.size();
		int valuesSize = ctx.identifier_list().ID().size();

		if(exprSize != valuesSize)
			assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSize, valuesSize);

		for (AST node : exprTypes)
			if (node.type != type && !(type == Type.FLOAT32_TYPE && node.type == Type.INT_TYPE))
				diffTypeError(ctx.start.getLine(), "variable declaration", ctx.expr_list().getText(), node.type, type);
		

		for (TerminalNode id : ctx.identifier_list().ID()){
			int idx = newVar(id.getSymbol(), type);
			AST children = new AST(NodeKind.VAR_DECL_NODE,idx,type);
			AST assingNode = AST.newSubtree(NodeKind.ASSIGN_NODE, NO_TYPE,children,exprTypes.remove(0));
			varListNode.addChild(assingNode);
		}
		return varListNode;
	}

	@Override
	public AST visitShortVar_decl(GOParser.ShortVar_declContext ctx) {
		List<AST> exprNodes = getExprNodes(ctx.expr_list());
		AST varDeclNode = AST.newSubtree(NodeKind.VAR_DECL_LIST_NODE, NO_TYPE);
	 	int exprSz = exprNodes.size();
	 	int valsSz = ctx.identifier_list().ID().size();

	 	if(exprSz != valsSz)
	 		assignmentMismatchError(ctx.start.getLine(), ctx.getText(), exprSz, valsSz);
		
	 	for (TerminalNode id : ctx.identifier_list().ID()){
	 		int idx = newVar(id.getSymbol(), exprNodes.get(0).type);
			AST children = new AST(NodeKind.VAR_DECL_NODE, idx, exprNodes.get(0).type);
			AST assingNode = AST.newSubtree(NodeKind.ASSIGN_NODE, NO_TYPE,children,exprNodes.remove(0));
			varDeclNode.addChild(assingNode);
		}
	 	return varDeclNode;
	}

	@Override
	public AST visitInc_dec_stmt(GOParser.Inc_dec_stmtContext ctx){
		if(ctx.INCREMENT() != null)
			return AST.newSubtree(NodeKind.INCREMENT, NO_TYPE, visit(ctx.expr()));
		return AST.newSubtree(NodeKind.DECREMENT, NO_TYPE, visit(ctx.expr()));
	}

//region Types

	@Override
	public AST visitFloatType(GOParser.FloatTypeContext ctx) {
		return new AST(NodeKind.FLOAT_LIT_NODE,0.0f,Type.FLOAT32_TYPE);
	}

	@Override
	public AST visitIntType(GOParser.IntTypeContext ctx) {
		return new AST(NodeKind.VAR_DECL_NODE,0,Type.INT_TYPE);
	}

	@Override
	public AST visitStringType(GOParser.StringTypeContext ctx) {
		//TODO tabela de String
		return new AST(NodeKind.VAR_DECL_NODE,0,Type.STRING_TYPE);
	}

	@Override
	public AST visitBoolType(GOParser.BoolTypeContext ctx) {

		return new AST(NodeKind.BOOL_LIT_NODE,1,Type.BOOLEAN_TYPE);
	}
//endregion

	@Override
	public AST visitOperand_name(GOParser.Operand_nameContext ctx) {
		return checkVar(ctx.ID().getSymbol());
	}

	// // TODO: isso aqui vai ser chato
	// // @Override
	// // public Type visitArrayLiteral(GOParser.ArrayLiteralContext ctx) {
	// // }

	@Override
	public AST visitParameterDecl(GOParser.ParameterDeclContext ctx) {
		AST typeNode = visit(ctx.type());
		int idx = newVar(ctx.ID().getSymbol(), typeNode.type);
		return new AST(NodeKind.VAR_DECL_NODE, idx, typeNode.type);
	}

	@Override
	public AST visitParameters(GOParser.ParametersContext ctx){
		AST argsNode = AST.newSubtree(NodeKind.VAR_DECL_LIST_NODE, NO_TYPE);

		for (ParameterDeclContext paramCtx : ctx.parameterDecl())
			argsNode.addChild(visit(paramCtx));
		return  argsNode;
	}

	@Override
	public AST visitFunction_decl(GOParser.Function_declContext ctx) {
		List<Type> resultTypes = new ArrayList<Type>();
		sh.push();
		
		AST paramsNode = visit(ctx.parameters());

		if (ctx.result() != null)
			for (TypeContext typeCtx : ctx.result().type())
				resultTypes.add(visit(typeCtx).type);

		int idx = newFunc(ctx.ID().getSymbol(), paramsNode.getChildrenTypes(), resultTypes); 

		AST blockNode = visit(ctx.block());

		AST funcNode = AST.newSubtree(NodeKind.FUNC_DECL_NODE, NO_TYPE, idx, paramsNode, blockNode);

		sh.pop();
		
		return funcNode;
	} 

	@Override
	public AST visitFor_stmt(GOParser.For_stmtContext ctx) {
		sh.push();
		AST blockNode = visit(ctx.block());
		//TODO criar a arvore para comparação
		AST argsNode = AST.newSubtree(NodeKind.FOR_CLAUSE_NODE, NO_TYPE);
		if(ctx.for_clause()!=null){
			if(ctx.for_clause().init_stmt !=null){
				AST initNode = visit(ctx.for_clause().init_stmt);
				argsNode.addChild(initNode);
			}
			AST exprNode = visit(ctx.for_clause().expr());
			argsNode.addChild(exprNode);
			if(ctx.for_clause().post_stmt !=null){
				AST postNode = visit(ctx.for_clause().post_stmt);
				argsNode.addChild(postNode);
			}
		}
		else if(ctx.expr() != null){
			AST exprNode = visit(ctx.expr());
			argsNode.addChild(exprNode);
		}
		AST forNode = AST.newSubtree(NodeKind.FOR_NODE, NO_TYPE,argsNode,blockNode);
		sh.pop();
		return forNode;
	}

	// @Override
	// public AST visitIf_stmt(GOParser.If_stmtContext ctx) {
	// 	AST blockNode = AST.newSubtree(NodeKind.BLOCK_NODE, NO_TYPE);
	// 	AST ifNode = AST.newSubtree(NodeKind.IF_NODE, Type.NO_TYPE,blockNode);
	// 	sh.push();
	// 	AST exprNode = visit(ctx.expr());
	// 	ifNode.addChild(exprNode);
	// 	for(StatementContext stmt :ctx.block(0).statement_list().statement()){
	// 		AST children = visitChildren(stmt);
	// 		blockNode.addChild(children);
	// 	}
	// 	if(ctx.ELSE() != null){
	// 		AST elseNode;
	// 		if(ctx.if_stmt()!= null)
	// 			elseNode = AST.newSubtree(NodeKind.ELSE_NODE, Type.NO_TYPE,visit(ctx.if_stmt()));
	// 		else {
	// 			AST blockElse = AST.newSubtree(NodeKind.BLOCK_NODE, NO_TYPE);
	// 			elseNode = AST.newSubtree(NodeKind.IF_NODE, Type.NO_TYPE,blockElse);
	// 			for(StatementContext stmt :ctx.block(1).statement_list().statement()){
	// 				AST children = visitChildren(stmt);
	// 				blockElse.addChild(children);
	// 			}
	// 		}
	// 		ifNode.addChild(elseNode);
	// 	}
	// 	sh.pop();
	// 	return ifNode;
	// }

	@Override
	public AST visitFunccall_stmt(GOParser.Funccall_stmtContext ctx) {
		isFunction = true;
		var funcNode = checkVar(ctx.ID().getSymbol());
		isFunction = false;

		FunctionEntry func = ft.get(funcNode.intData);
		var argNodes = checkFuncArguments(func, ctx.arguments());

		for (AST arg : argNodes)
			funcNode.addChild(arg);
		return funcNode;
	}

	@Override
	public AST visitArrayType(GOParser.ArrayTypeContext ctx) {
		this.isArray = true;
		return visitChildren(ctx);
	}

// region Literal Values 
	@Override
	public AST visitIntVal(GOParser.IntValContext ctx){
		return new AST(NodeKind.INT_LIT_NODE,Integer.parseInt(ctx.getText()),INT_TYPE);
	}
	
	@Override
	public AST visitNilVal(GOParser.NilValContext ctx) {
		return new AST(NodeKind.NIL_LIT_NODE, 0, NIL_TYPE);
	}

	public AST visitStrVal(GOParser.StrValContext ctx) {
		int idx = st.addString(ctx.getText());
		return new AST(NodeKind.STR_LIT_NODE, idx, STRING_TYPE);
	}

	public AST visitFloatVal(GOParser.FloatValContext ctx){
		return new AST(NodeKind.FLOAT_LIT_NODE,Float.parseFloat(ctx.getText()),FLOAT32_TYPE);
	}

	public AST visitTrueVal(GOParser.TrueValContext ctx){
		return new AST(NodeKind.BOOL_LIT_NODE,1,BOOLEAN_TYPE);
	}

	public AST visitFalseVal(GOParser.FalseValContext ctx){
		return new AST(NodeKind.BOOL_LIT_NODE ,0,BOOLEAN_TYPE);
	}
//endregion

//#region Expressions
	// @Override
	// public AST visitOperandExpr(GOParser.OperandExprContext ctx) {		
	// 	if(ctx.index() != null){
	// 		isArray = true;
	// 		AST type = visit(ctx.operand());
	// 		isArray = false;
	// 		AST indexNode = visit(ctx.index());
	// 		if(indexNode.kind != NodeKind.INT_LIT_NODE && indexNode.type != Type.INT_TYPE){
	// 			//TODO erro de indice não inteiro
	// 		}
	// 		return type;
	// 	}
	// 	if(ctx.arguments() != null){
	// 		isFunction = true;
	// 		Token nameFunc = ctx.operand().operand_name().ID().getSymbol();
	// 		checkVar(nameFunc);
	// 		isFunction = false;
	// 		// visit(ctx.arguments());
	// 		LinkedList<Type> argumentsTypes = getExprNodes(ctx.arguments().expr_list());
	// 		String funcName = nameFunc.getText();
	// 		VarEntry func = sh.lookupVar(funcName);

	// 		checkFuncArguments(func, ctx.arguments());

	// 		// função em uma expressão deve ter um unico valor de retorno
	// 		if (func.funcReturn.size() == 1) 
	// 			return func.funcReturn.get(0);
			
	// 		if (func.funcReturn.size() == 0)
	// 			PANIC("SEMANTIC ERROR (%d): %s (no value) used as value", nameFunc.getLine(),ctx.getText());
	// 		else
	// 			PANIC("SEMANTIC ERROR (%d):  multiple-value %s (value of type (%s)) in single-value context\n",
	// 				nameFunc.getLine(),ctx.getText(),Type.listToString(func.funcReturn));
	// 	}
	// 	return visit(ctx.operand());
	// }

	@Override
	public AST visitOperand(GOParser.OperandContext ctx) { // literal | operand_name | L_PR expr R_PR;
		if (ctx.literal() != null)
			return visit(ctx.literal());
		if(ctx.expr() != null)
			return visit(ctx.expr());
		return visit(ctx.operand_name());
	}

	@Override
	public AST visitUnary(GOParser.UnaryContext ctx) {
		AST expr = visitChildren(ctx);
		boolean wasExpectingBoolType = ctx.NOT() != null && expr.type != BOOLEAN_TYPE; // ex: !5
		boolean wasExpectingNumType = (ctx.MINUS() != null || ctx.PLUS() != null) && !Type.isNumber(expr.type); // ex: -5 

		if ( wasExpectingBoolType || wasExpectingNumType )
			operatorNotDefinedError(ctx.start.getLine(), ctx.getText(), ctx.unary_op.getText(), expr.type);

		return expr;
	}

	@Override
	public AST visitMultDiv(GOParser.MultDivContext ctx) {
		AST left = visit(ctx.expr(0));
		AST right = visit(ctx.expr(1));
		Type type = Type.numberTypeWidening(left.type, right.type);

		NodeKind node = null;

		if (ctx.DIV() != null) {
			node = NodeKind.DIV_NODE;
		} else if (ctx.STAR() != null) {
			node = NodeKind.TIMES_NODE;
		}

		if (type == null)
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), left.type, right.type);

		return AST.newSubtree(node, type, left, right);
	}

	@Override
	public AST visitPlusMinus(GOParser.PlusMinusContext ctx) {
		AST l = visit(ctx.expr(0));
		AST r = visit(ctx.expr(1));

		NodeKind node = null;

		if (ctx.PLUS() != null) {
			node = NodeKind.PLUS_NODE;
		} else if ( ctx.MINUS() != null ) {
			node = NodeKind.MINUS_NODE;
		}

		
		if (l.type == STRING_TYPE && r.type == STRING_TYPE && node == NodeKind.PLUS_NODE)
			return AST.newSubtree(node, STRING_TYPE, l, r);

		if (!Type.isBothNumbers(l.type, r.type))
			mismatchedOperationError(ctx.start.getLine(), ctx.getText(), l.type, r.type);

		// Verifica se é necessário o widening
		if (Type.isI2FWideningNeeded(l.type, r.type)){
			// Verifica quem precisa de conversão e cria o nó
			if (Type.isI2FTarget(l.type) ){
				r = AST.newSubtree(NodeKind.I2F_NODE, Type.FLOAT32_TYPE, r);
			} else {
				l = AST.newSubtree(NodeKind.I2F_NODE, Type.FLOAT32_TYPE, l);
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
