package code;
import java.util.Iterator;

import ast.AST;
import ast.ASTBaseVisitor;
import tables.FunctionEntry;
import tables.FunctionTable;
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
	private final CallStack callStack;
	private final FunctionTable ft;
	private final Memory memory;
	private final StrTable st;
	private final VarTable vt;
	private final Cpu cpu;
	private final Io io;
	

	// Construtor basicão.
	public Interpreter(StrTable st, VarTable vt, FunctionTable ft) {
		this.stack = new DataStack();
		this.memory = new Memory();
		this.st = st;
		this.vt = vt;
		this.ft = ft;
		this.cpu = new Cpu(stack, memory, st, vt);
		this.io = new Io(stack, memory, st);
		this.callStack = new CallStack(memory, vt, ft);
	}

	@Override
	protected Void visitProgram(AST node) {
		// visit(node.getChild(0)); // run var_list
		// visit(node.getChild(1)); // run block

		visitAllChildren(node);
    boolean hasMain = false;
		for (int i = 0; i < ft.getSize() ; i++) {
			FunctionEntry funcEntry = ft.get(i);
			if (funcEntry.name.equals("main")) {
				callStack.push(funcEntry.id);
				visit(funcEntry.declareNode.getChild(1));
				callStack.pop();
				hasMain = true;
        break;
			}
		}
    if(!hasMain) {
      System.err.println("PLEASE PROVIDE A MAIN FUNCTION");
			System.exit(1);
		}
		System.out.println();
		System.out.println(memory);
		io.close();

		return null; // Java exige um valor de retorno mesmo para Void... :/
	}

	@Override
	protected Void visitAssign(AST node) {
		// visits var assign (stacking var index)
		visit(node.getChild(0));
		// visits expression
		visit(node.getChild(1));

		Word value = stack.pop();
		int varIndex = stack.popi();

		memory.store(callStack.getVarAddress(varIndex), value);
		return null;
	}

	@Override
	protected Void visitShortVarDecl(AST node) {
		// visits var assign (stacking var index)
		visit(node.getChild(0));
		// visits expression
		visit(node.getChild(1));

		Word value = stack.pop();
		int varId = stack.popi();

		int varAddress = callStack.getVarAddress(varId);
		memory.store(varAddress,value);
		return null;
	}

	@Override
	protected Void visitAssignList(AST node) {
		return visitAllChildren(node); // visits all varAssign
	}

	@Override
	protected Void visitVarDecl(AST node) {
		// nada a fazer. O espaço na memória já foi reservado de acordo com a vartable
		return null;
	}

	@Override
	protected Void visitVarDeclList(AST node) {
		// possui nós ou de varDeclare (sem atribuição)
		// ou de ShortVarDecl (que possui atribuição)
		return visitAllChildren(node); 
	}

	@Override
	protected Void visitVarAssign(AST node) {
		int varIndex = node.intData;
		stack.push(varIndex);
		return null;
	}

	@Override
	protected Void visitBlock(AST node) {
		return visitAllChildren(node);
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

	@Override
	protected Void visitIf(AST node) {
		// visits clause, stacking boolean result
		visit(node.getChild(0));
		if (stack.popb() == true)
			visit(node.getChild(1)); // block
		else if (node.hasChild(2)) // has else stmt
			visit(node.getChild(2));
		return null;
	}

	@Override
	protected Void visitIfClause(AST node) {
		return visitAllChildren(node);
	}

	@Override
	protected Void visitElse(AST node) {
		return visitAllChildren(node);
	}

	@Override
	protected Void visitFor(AST node) {

		AST clause = node.getChild(0);
		// Avaliar pros casos sem ini_stmt e post_stmt
		if(clause.hasChild(1)){
			visit(clause);
			visit(clause.getChild(1));
			while(stack.popb()){
				visit(node.getChild(1));
				visit(clause.getChild(2));
				visit(clause.getChild(1));
			}
			return null;
		}

		
		visit(clause.getChild(0));
		while(stack.popb()){
			visit(node.getChild(1));
			visit(clause.getChild(0));
		}

		return null;
	}

	// TODO
	@Override
	protected Void visitForClause(AST node) {
		visit(node.getChild(0));

		return null;
	}

	@Override
	protected Void visitIncrement(AST node) {
		visitAllChildren(node);

		if(node.getChild(0).isInt()){
			cpu.doIntUnaryOperation((a) -> a + 1);
			Word value = stack.pop();
			int varIndex = stack.popi();
			int varAddress = callStack.getVarAddress(varIndex);
			memory.store(varAddress,value);
		}
		
		return null;
	}

	// TODO
	@Override
	protected Void visitDecrement(AST node) {
		visitAllChildren(node);

		if(node.getChild(0).isInt()){
			cpu.doIntUnaryOperation((a) -> a - 1);
			Word value = stack.pop();
			int varIndex = stack.popi();
			int varAddress = callStack.getVarAddress(varIndex);
			memory.store(varAddress,value);
		}
		
		return null;
	}

	// TODO
	@Override
	protected Void visitRead(AST node) {
		return null;
	}

	@Override
	protected Void visitPrint(AST node) {
		Iterator<AST> children = node.iterateChildren();
		while (children.hasNext()) {
			AST exprToPrint = children.next();
			visit(exprToPrint); // pushes expr value to stack
			switch (exprToPrint.type) {
				case INT_TYPE: io.writeInt(); break;
				case FLOAT32_TYPE: io.writeReal(); break;
				case BOOLEAN_TYPE: io.writeBool(); break;
				case STRING_TYPE: io.writeStr(); break;
				default: System.out.println("Should never happen");
			}
		}
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
		stack.push(node.intData);
		return null;
	}

	@Override
	protected Void visitFloatLit(AST node) {
		stack.push(node.floatData);
		return null;
	}

	@Override
	protected Void visitBoolLit(AST node) {
		stack.push(node.getBoolData());
		return null;
	}

	@Override
	protected Void visitStrLit(AST node) {
		int strIndex = node.intData;
		stack.push(strIndex);
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

		// ((ACHO)) que dá pra fazer isso diretamente sem checar o tipo
		// pois ambos stack e memory usam word internamente
		int varAddress = callStack.getVarAddress(varIndex);
		Word word = memory.get(varAddress);
		stack.push(word);
		
		return null;
	}

	@Override
	protected Void visitFuncCall(AST node) {
		int funcId = node.intData;
		callStack.push(funcId);

		visitAllChildren(node);
		visit(ft.get(funcId).declareNode.getChild(1)); // visits block
		callStack.pop();
		return null;
	}

	// *** OPERATIONS ***
	@Override
	protected Void visitMinus(AST node) {
		// remember, remember, the reverse polish notation
		visit(node.getChild(0));
		if (!node.hasChild(1)){ // unary
			if (node.getChild(0).isInt())
				stack.push(-stack.popi());
			else
				stack.push(-stack.popf());
			return null;
		}

		visit(node.getChild(1));

		if (node.getChild(0).isInt())
			cpu.doIntOperation((a,b) -> a - b);
		else
			cpu.doFloatOperation((a,b) -> a - b);

		return null;
	}

	@Override
	protected Void visitPlus(AST node) {
		// remember, remember, the reverse polish notation
		visit(node.getChild(0));

		if (!node.hasChild(1)) // unary
			return null;

		visit(node.getChild(1));

		if (node.getChild(0).isInt())
			cpu.doIntOperation((a,b) -> a + b);
		else if (node.getChild(0).isFloat())
			cpu.doFloatOperation((a,b) -> a + b);
		else // string
			cpu.doStrOperation((a,b) -> a + b);

		return null;
	}

	@Override
	protected Void visitDiv(AST node) {
		// remember, remember, the reverse polish notation
		visit(node.getChild(0));
		visit(node.getChild(1));
	
		if (node.getChild(0).isInt())
			cpu.doIntOperation((a,b) -> a / b);
		else
			cpu.doFloatOperation((a,b) -> a / b);

		return null;
	}

	@Override
	protected Void visitTimes(AST node) {
		// remember, remember, the reverse polish notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.getChild(0).isInt())
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

		if (node.getChild(0).isInt())
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

		if (node.getChild(0).isInt())
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

		if (node.getChild(0).isInt())
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

		if (node.getChild(0).isInt())
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

		if (node.getChild(0).isInt())
			cpu.doIntPredicate((a,b) -> a == b);
		else if (node.getChild(0).isFloat())
			cpu.doFloatPredicate((a,b) -> a == b);
		else if (node.getChild(0).isBool())
			cpu.doBoolPredicate((a,b) -> a == b);
		else // string
			cpu.doStrPredicate((a,b) -> a.equals(b));

		return null;
	}

	@Override
	protected Void visitNotEqual(AST node) {
		// remember, remember, the polish reverse notation
		visit(node.getChild(0));
		visit(node.getChild(1));

		if (node.getChild(0).isInt())
			cpu.doIntPredicate((a, b) -> a != b);
		else if (node.getChild(0).isFloat())
			cpu.doFloatPredicate((a, b) -> a != b);
		else if (node.getChild(0).isBool())
			cpu.doBoolPredicate((a, b) -> a != b);
		else // string
		  cpu.doStrPredicate((a,b) -> !a.equals(b));

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
		stack.push((float) value);

		return null;
	}

	// *** HELPERS ***

	private Void visitAllChildren(AST node) {
		Iterator<AST> children = node.iterateChildren();
		while(children.hasNext())
			visit(children.next());
		return null;
	}
	
}
