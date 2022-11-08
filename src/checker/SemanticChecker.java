package checker;

import java.util.LinkedList;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import parser.GOParser;
import parser.GOParserBaseVisitor;
import tables.ScopeHandler;
import tables.StrTable;
import tables.VarEntry;
import tables.VarTable;
import typing.Type;
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
public class SemanticChecker extends GOParserBaseVisitor<Void> {

	private StrTable st = new StrTable(); // Tabela de strings.
	private VarTable vt = new VarTable(); // Tabela de variáveis.
	private ScopeHandler sh = new ScopeHandler();

	private LinkedList<Type> typeQueue = new LinkedList<Type>();

	private Type lastInferType; // Variável "global" com o último tipo inferido.
	private Type lastDeclType; // Variável "global" com o último tipo declarado.
	private boolean infer = false;
	private boolean isArray = false;
	private boolean isFunction = false;
	// Testa se o dado token foi declarado antes.
	private Type checkVar(Token token) {
		String varName = token.getText();
		int line = token.getLine();
		VarEntry entry = sh.lookupVar(varName);
		if (entry == null) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' was not declared.\n", line, varName);
			System.exit(1);
			return null; // Never reached.
		}
		if (isArray && entry.special != SpecialType.ARRAY) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' is not an array.\n", line, varName);
			System.exit(1);
			return null; // Never reached.
		}
		if (isFunction && entry.special != SpecialType.FUNCTION) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' is not a function.\n", line, varName);
			System.exit(1);
			return null; // Never reached.
		}
		return entry.type;
	}

	// Cria uma nova variável a partir do dado token.
	private void newVar(Token token, Type type) {
		String varName = token.getText();
		int line = token.getLine();
		VarEntry entry = sh.lookupVar(varName);
		if (entry != null) {
			System.err.printf("SEMANTIC ERROR (%d): variable '%s' already declared at line %d.\n", line, varName,
					entry.line);
			System.exit(1);
			return; // Never reached.
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

	@Override
	public Void visitProgram(GOParser.ProgramContext ctx) {
		sh.push();
		return visitChildren(ctx); 
	}


	@Override
	public Void visitAssignee(GOParser.AssigneeContext ctx) {
		checkVar(ctx.ID().getSymbol());
		return null;
	}

	@Override
	public Void visitIdentifier_list(GOParser.Identifier_listContext ctx) {
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
	public Void visitParameterDecl(GOParser.ParameterDeclContext ctx) {
		infer = false;
		visit(ctx.type());
		visit(ctx.identifier_list());
		return null;
	}

	@Override
	public Void visitVar_spec(GOParser.Var_specContext ctx) { // var a, b int
		infer = false;
		visit(ctx.type());
		if (ctx.expr_list() != null) {
			visit(ctx.expr_list());
		}
		visit(ctx.identifier_list());
		return null;
	}

	@Override
	public Void visitConst_spec(GOParser.Const_specContext ctx) {
		infer = false;
		// if (ctx.type() != null) {
		// 	infer = false;
		// 	visit(ctx.type());
		// }
		visit(ctx.type());
		visit(ctx.expr_list());
		visit(ctx.identifier_list());
		return null;
	}

	@Override
	public Void visitFloatType(GOParser.FloatTypeContext ctx) {
		this.lastDeclType = Type.FLOAT32_TYPE;
		return null;
	}

	@Override
	public Void visitIntType(GOParser.IntTypeContext ctx) {
		this.lastDeclType = Type.INT_TYPE;
		return null;
	}

	@Override
	public Void visitStringType(GOParser.StringTypeContext ctx) {
		this.lastDeclType = Type.STRING_TYPE;
		return null;
	}

	@Override
	public Void visitBoolType(GOParser.BoolTypeContext ctx) {
		this.lastDeclType = Type.BOOLEAN_TYPE;
		return null;
	}

	@Override
	public Void visitOperand_name(GOParser.Operand_nameContext ctx) {
		checkVar(ctx.ID().getSymbol());
		return null;
	}


	@Override
	public Void visitShortVar_decl(GOParser.ShortVar_declContext ctx) {
		// a, b := 1, "sim"
		// var a, b, c int
		//list lastDeclType<Type>

		// TODO Comparar a lista de variáveis com a lista de valores(quantidade)
		lastDeclType = Type.INFERED_TYPE;
		visitChildren(ctx);
		return null;
	}

	@Override 
	public Void visitExpr_list(GOParser.Expr_listContext ctx) { 
		 /* TODO Precisamos pegar o tipo do valor que está sendo atribuído
		mesmo no caso de experssões e índices */
		visitChildren(ctx);
		return null;
	}

	@Override
	public Void visitLiteral(GOParser.LiteralContext ctx) {
		if(ctx.basic != null && ctx.basic.getType() == GOParser.STR_LIT) {
			st.add(ctx.basic.getText());
		}
		return null;
	}

	@Override
	public Void visitFunction_decl(GOParser.Function_declContext ctx) {
		if(ctx.result() != null) 
			visit(ctx.result());
		else
			lastDeclType = Type.INT_TYPE;
		isFunction = true;
		newVar(ctx.ID().getSymbol(),lastDeclType);

		sh.push();
		visit(ctx.parameters());

		if(ctx.block() != null) {
			visit(ctx.block());
		}

		sh.pop();
		return null;
	}

	@Override
	public Void visitFor_stmt(GOParser.For_stmtContext ctx) {
		sh.push();
		visitChildren(ctx);
		sh.pop();
		return null;
	}

	@Override
	public Void visitIf_stmt(GOParser.If_stmtContext ctx) {
		sh.push();
		visitChildren(ctx);
		sh.pop();
		return null;
	}



	@Override
	public Void visitArrayType(GOParser.ArrayTypeContext ctx) {
		this.isArray = true;
		return visitChildren(ctx);
	}


	@Override
	public Void visitOperandExpr(GOParser.OperandExprContext ctx) {
		if(ctx.index() != null  ){
			isArray  = true;
			visit(ctx.operand());
			isArray = false;
			visit(ctx.index());
			return null;
		}
		if(ctx.arguments() != null  ){
			isFunction = true;
			visit(ctx.operand());
			isFunction = false;
			visit(ctx.arguments());
			return null;
		}
		visitChildren(ctx);
		return null;
	}

}
