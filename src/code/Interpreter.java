package code;
import java.util.Iterator;

import ast.AST;
import ast.ASTBaseVisitor;
import tables.StrTable;
import tables.VarTable;


/*
 * Interpretador de código para EZLang, implementado como
 * um visitador da AST gerada pelo front-end. Tipo genérico
 * foi instanciado para Void porque a gente não precisa de
 * um valor de retorno na visitação. Para o gerador de código
 * do próximo laboratório isso vai mudar.
 *
 * Para rodar, chame o método 'execute' da superclasse.
 */
public class Interpreter extends ASTBaseVisitor<Void> {

	// Tudo privado e final para simplificar.
	private final DataStack stack;
	private final Memory memory;
	private final StrTable st;
	private final VarTable vt;
	private final Cpu cpu;
	private final Io io;

	// Construtor basicão.
	public Interpreter(StrTable st, VarTable vt) {
		this.stack = new DataStack();
		this.memory = new Memory(vt);
		this.st = st;
		this.vt = vt;
		this.cpu = new Cpu(stack, memory, st, vt);
		this.io = new Io(stack, memory, st);
	}

	@Override
	protected Void visitProgram(AST node) {
		visit(node.getChild(0)); // run var_list
		visit(node.getChild(1)); // run block
		io.close();
		return null; // Java exige um valor de retorno mesmo para Void... :/
	}

	// TODO
	@Override
	protected Void visitAssign(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitShortVarDecl(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitAssignList(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitVarDecl(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitVarDeclList(AST node) {
		return null;
	}

	@Override
	protected Void visitBlock(AST node) {
		Iterator<AST> children = node.iterateChildren();
		while(children.hasNext())
			visit(children.next());
		return null;
	}

	// TODO
	@Override
	protected Void visitFuncDecl(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitReturn(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitIf(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitIfClause(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitElse(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitFor(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitForClause(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitIncrement(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitDecrement(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitRead(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitWrite(AST node) {
		return null;
	}

	// TODO
	@Override
	protected Void visitPrint(AST node) {
		return null;
	}




	// @Override
	// protected Void visitVarDecl(AST node) {
	// 	// Nothing to do.
	// 	return null; // Java exige um valor de retorno mesmo para Void... :/
	// }

	// *** LITERALS ***

	@Override
	protected Void visitIntLit(AST node) {
		stack.pushi(node.intData);
		return null;
	}

	@Override
	protected Void visitFloatLit(AST node) {
		stack.pushf(node.floatData);
		return null;
	}

	@Override
	protected Void visitBoolLit(AST node) {
		stack.pushb(node.getBoolData());
		return null;
	}

	@Override
	protected Void visitStrLit(AST node) {
		int strIndex = node.intData;
		stack.pushi(strIndex);
		return null;
	}

	// TODO
	@Override
	protected Void visitNilLit(AST node) {
		return null;
	}

	// *** OTHER OPERANDS ***

	@Override
	protected Void visitVarUse(AST node) {
		int varIndex = node.intData;
	
		// TODO: TRATAR ARRAY(??)

		// ((ACHO)) que dá pra fazer isso diretamente sem checar o tipo, pois ambos usam word internamente
		Word word = memory.get(varIndex);
		stack.push(word);
		
		return null;
	}

	@Override
	protected Void visitFuncCall(AST node) {
		return null;
	}

	// *** OPERATIONS ***
	@Override
	protected Void visitMinus(AST node) {
		// remember, remember, the polish reverse notation 
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntOperation((a,b) -> a - b);
		else
			cpu.doFloatOperation((a,b) -> a - b);

		return null;
	}

	@Override
	protected Void visitPlus(AST node) {
		// remember, remember, the polish reverse notation 
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntOperation((a,b) -> a + b);
		else if (node.isFloat())
			cpu.doFloatOperation((a,b) -> a + b);
		else // string
			cpu.doStrOperation((a,b) -> a + b);

		return null;
	}

	@Override
	protected Void visitDiv(AST node) {
		// remember, remember, the polish reverse notation 
		visit(node.getChild(0));
		visit(node.getChild(1));
	
		if (node.isInt())
			cpu.doIntOperation((a,b) -> a / b);
		else
			cpu.doFloatOperation((a,b) -> a / b);

		return null;
	}

	@Override
	protected Void visitTimes(AST node) {
		// remember, remember, the polish reverse notation 
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntOperation((a,b) -> a * b);
		else
			cpu.doFloatOperation((a,b) -> a * b);

		return null;
	}

	@Override
	protected Void visitLess(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntPredicate((a,b) -> a < b);
		else
			cpu.doFloatPredicate((a,b) -> a < b);

		return null;
	}

	@Override
	protected Void visitLessEqual(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntPredicate((a,b) -> a <= b);
		else
			cpu.doFloatPredicate((a,b) -> a <= b);

		return null;
	}

	@Override
	protected Void visitGreaterEqual(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntPredicate((a,b) -> a >= b);
		else
			cpu.doFloatPredicate((a,b) -> a >= b);

		return null;
	}

	@Override
	protected Void visitGreater(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntPredicate((a,b) -> a > b);
		else
			cpu.doFloatPredicate((a,b) -> a > b);

		return null;
	}

	@Override
	protected Void visitEqual(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.isInt())
			cpu.doIntPredicate((a,b) -> a == b);
		else if (node.isFloat())
			cpu.doFloatPredicate((a,b) -> a == b);
		else if (node.isBool())
			cpu.doBoolPredicate((a,b) -> a == b);
		else // string
			cpu.doStrPredicate((a,b) -> a == b);

		return null;
	}

	@Override
	protected Void visitNotEqual(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));
	
		if (node.isInt())
			cpu.doIntPredicate((a,b) -> a != b);
		else if (node.isFloat())
			cpu.doFloatPredicate((a,b) -> a != b);
		else if (node.isBool())
			cpu.doBoolPredicate((a,b) -> a != b);
		else // string
		  cpu.doStrPredicate((a,b) -> a != b);

		return null;
	}

	@Override
	protected Void visitAnd(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		cpu.doBoolPredicate((a, b) -> a && b);

		return null;
	}

	@Override
	protected Void visitOr(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		cpu.doBoolPredicate((a, b) -> a || b);

		return null;
	}

	@Override
	protected Void visitNot(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));

		cpu.doBoolSimplePredicate((val) -> !val);

		return null;
	}


	@Override
	protected Void visitI2F(AST node) {
		visit(node.getChild(0));

		int value = stack.popi();
		stack.pushf((float) value);

		return null;
	}

	// *** HELPERS ***
	
}
