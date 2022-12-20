package ast;

import static typing.Type.NO_TYPE;

import java.util.ArrayList;
import java.util.List;

import tables.FunctionTable;
import tables.VarTable;
import typing.Conv;
import typing.Type;

// Implementação dos nós da AST.
public class AST {

	// Todos os campos são finais para simplificar, assim não precisa de getter/setter.
	// Note que não há union em Java, então o truque de ler
	// e/ou escrever o campo com formatos diferentes não funciona aqui.
	// Os campos 'data' NÃO ficam sincronizados!
	public  final NodeKind kind;
	public  final int intData;
	public  final float floatData;
	public  final Type type;
	private final List<AST> children; // Privado para que a manipulação da lista seja controlável.

	// Construtor completo para poder tornar todos os campos finais.
	// Privado porque não queremos os dois campos 'data' preenchidos ao mesmo tempo.
	private AST(NodeKind kind, int intData, float floatData, Type type) {
		this.kind = kind;
		this.intData = intData;
		this.floatData = floatData;
		this.type = type;
		this.children = new ArrayList<AST>();
	}

	// Cria o nó com um dado inteiro.
	public AST(NodeKind kind, int intData, Type type) {
		this(kind, intData, 0.0f, type);
	}

	// Cria o nó com um dado float.
	public AST(NodeKind kind, float floatData, Type type) {
		this(kind, 0, floatData, type);
	}

	// Adiciona um novo filho ao nó.
	public void addChild(AST child) {
		// A lista cresce automaticamente, então nunca vai dar erro ao adicionar.
		this.children.add(child);
	}

	public void addChildren(List<AST> children) {
		for (AST child: children)
			this.children.add(child);
	}

	// Retorna o filho no índice passado.
	// Não há nenhuma verificação de erros!
	public AST getChild(int idx) {
		// Claro que um código em produção precisa testar o índice antes para
		// evitar uma exceção.
	    return this.children.get(idx);
	}

	public List<Type> getChildrenTypes() {
		List<Type> types = new ArrayList<Type>();
		for (AST child : children)
			types.add(child.type);
		return types;
	}

	// Cria um nó e pendura todos os filhos passados como argumento.
	public static AST newSubtree(NodeKind kind, Type type, AST... children) {
		AST node = new AST(kind, 0, type);
	    for (AST child: children) {
	    	node.addChild(child);
	    }
	    return node;
	}

	// Cria um nó e pendura todos os filhos passados como argumento.
	public static AST newSubtree(NodeKind kind, Type type, int intData, AST... children) {
		AST node = new AST(kind, intData, type);
			for (AST child: children) {
				node.addChild(child);
			}
			return node;
	}

	// Cria um nó e pendura todos os filhos passados como argumento.
	public static AST[] numberWidening(AST left, AST right) {
		// Verifica se é necessário o widening
		if (Type.isI2FWideningNeeded(left.type, right.type)){
			// Verifica quem precisa de conversão e cria o nó
			if (Type.isI2FTarget(left.type) ){
				var l = AST.createConvNode(Conv.I2F, left);
				return new AST[]{ l, right };
			} else {
				var r = AST.createConvNode(Conv.I2F, right);
				return new AST[]{ left, r };
			}
		}
		return new AST[]{ left, right };
	}

	// Cria e retorna um novo nó de conversão da AST segundo o parâmetro 'conv' passado.
	// O parâmetro 'n' é o nó que será pendurado como filho do nó de conversão.
	// Caso a conversão indicada seja 'NONE', a função simplesmente retorna o próprio
	// nó passado como argumento.
	public static AST createConvNode(Conv conv, AST n) {
		switch(conv) {
				case I2F:  return AST.newSubtree(NodeKind.I2F_NODE, Type.FLOAT32_TYPE, n);
				case NONE: return n;
				default:
					System.err.printf("INTERNAL ERROR: invalid conversion of types!\n");
					System.exit(1);
					return null; // Never reached...
		}
	}

	public static String typesToString(List<AST> asts) {
		StringBuilder sb = new StringBuilder();
		for (AST ast : asts) {
			sb.append(ast.type.toString());
			sb.append(", ");
		}
	
		return sb.toString();
	}

	// Variáveis internas usadas para geração da saída em DOT.
	// Estáticas porque só precisamos de uma instância.
	private static int nr;
	private static VarTable vt;
	private static FunctionTable ft;

	// Imprime recursivamente a codificação em DOT da subárvore começando no nó atual.
	// Usa stderr como saída para facilitar o redirecionamento, mas isso é só um hack.
	private int printNodeDot() {
		int myNr = nr++;

	    System.err.printf("node%d[label=\"", myNr);
	    if (this.type != NO_TYPE) {
	    	System.err.printf("(%s) ", this.type.toString());
	    }
	    if (this.kind == NodeKind.VAR_DECL_NODE || this.kind == NodeKind.VAR_USE_NODE) {
				if(vt.get(this.intData).isArray()) 
					System.err.printf("%s[%d]@", vt.get(this.intData).name, vt.get(this.intData).arraySz);
	    	else System.err.printf("%s@", vt.get(this.intData).name);
	    } else if (this.kind == NodeKind.FUNC_CALL_NODE || this.kind ==  NodeKind.FUNC_DECL_NODE) {
	    	System.err.printf("%s@", ft.get(this.intData).name);
	    } else {
	    	System.err.printf("%s", this.kind.toString());
	    }
	    if (NodeKind.hasData(this.kind)) {
	        if (this.kind == NodeKind.FLOAT_LIT_NODE) {
	        	System.err.printf("%.2f", this.floatData);
	        } else if (this.kind == NodeKind.STR_LIT_NODE) {
	        	System.err.printf("@%d", this.intData);
	        } else {
	        	System.err.printf("%d", this.intData);
	        }
	    }
	    System.err.printf("\"];\n");

	    for (int i = 0; i < this.children.size(); i++) {
	        int childNr = this.children.get(i).printNodeDot();
	        System.err.printf("node%d -> node%d;\n", myNr, childNr);
	    }
	    return myNr;
	}

	// Imprime a árvore toda em stderr.
	public static void printDot(AST tree, VarTable varTable, FunctionTable funcTable) {
	    nr = 0;
	    vt = varTable;
	    ft = funcTable;
	    System.err.printf("digraph {\ngraph [ordering=\"out\"];\n");
	    tree.printNodeDot();
	    System.err.printf("}\n");
	}
}
