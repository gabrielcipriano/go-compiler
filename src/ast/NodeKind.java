package ast;

// Enumeração dos tipos de nós de uma AST.
// Adaptado da versão original em C.
// Algumas pessoas podem preferir criar uma hierarquia de herança para os
// nós para deixar o código "mais OO". Particularmente eu não sou muito
// fã, acho que só complica mais as coisas. Imagine uma classe abstrata AST
// com mais de 20 classes herdando dela, uma classe para cada tipo de nó...
public enum NodeKind {
    FLOAT_LIT_NODE,
    BOOL_LIT_NODE,
    INT_LIT_NODE,
    STR_LIT_NODE,
    VAR_DECL_NODE,
    VAR_LIST_NODE,
    VAR_USE_NODE,
    ASSIGN_NODE,
    BLOCK_NODE,
    IF_NODE,
    ELSE_NODE,
    MINUS_NODE,
    OVER_NODE,
    PLUS_NODE,
    PROGRAM_NODE,
    READ_NODE,
    FOR_NODE,
    TIMES_NODE,
    WRITE_NODE,
    // Type conversion nodes
    I2F_NODE,
    
    AND_NODE,
    OR_NODE,
    PRINT_NODE,
    EQ_NODE,
    NOT_EQ_NODE,
    LESS_NODE,
    LESS_EQ_NODE,
    GREATER_NODE,
    GREATER_EQ_NODE,
    NOT_NODE,
    INCREMENT;

	public String toString() {
		switch(this) {
            case ASSIGN_NODE:       return "=";
            case EQ_NODE:           return "==";
            case BLOCK_NODE:        return "block";
            case BOOL_LIT_NODE:     return "";
            case IF_NODE:           return "if";
            case INT_LIT_NODE:      return "";
            case LESS_NODE:         return "<";
            case MINUS_NODE:        return "-";
            case OVER_NODE:         return "/";
            case PLUS_NODE:         return "+";
            case PROGRAM_NODE:      return "program";
            case READ_NODE:         return "read";
            case FLOAT_LIT_NODE:    return "";
            case FOR_NODE:          return "for";
            case STR_LIT_NODE:      return "";
            case TIMES_NODE:        return "*";
            case VAR_DECL_NODE:     return "var_decl";
            case VAR_LIST_NODE:     return "var_list";
            case VAR_USE_NODE:      return "var_use";
            case WRITE_NODE:        return "write";
            case I2F_NODE:          return "I2F";
            case AND_NODE:          return "&&";
            case PRINT_NODE:        return "print" ;
            case NOT_EQ_NODE:       return "!=";
            case OR_NODE:           return "||";
            case LESS_EQ_NODE:      return "<=";
            case GREATER_EQ_NODE:   return ">=";
            case GREATER_NODE:      return ">";
            case NOT_NODE:          return "!";
            case INCREMENT:         return "++";
			default:
				System.err.println("ERROR: Fall through in NodeKind enumeration!");
				System.exit(1);
				return ""; // Never reached.
		}
	}

	public static boolean hasData(NodeKind kind) {
		switch(kind) {
	        case BOOL_LIT_NODE:
	        case INT_LIT_NODE:
	        case FLOAT_LIT_NODE:
	        case STR_LIT_NODE:
	        case VAR_DECL_NODE:
	        case VAR_USE_NODE:
	            return true;
	        default:
	            return false;
		}
	}
}
