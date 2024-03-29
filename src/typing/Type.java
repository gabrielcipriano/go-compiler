package typing;

import java.util.List;


// Enumeração dos tipos primitivos
public enum Type {
	INT_TYPE("int"),
	FLOAT32_TYPE("float32"),
	STRING_TYPE("string"),
	BOOLEAN_TYPE("bool"),
	NIL_TYPE("nil"),
	NO_TYPE("no_type");

	private String name;

	Type(String name) {
		this.name = name;
	}

	Type() {
	}

	public String getName() {
		return name;
	}

	public String toString() {
		switch (this) {
			case INT_TYPE:
				return "int";
			case FLOAT32_TYPE:
				return "float32";
			case BOOLEAN_TYPE:
				return "bool";
			case STRING_TYPE:
				return "string";
			case NIL_TYPE:
			 return "nil";
			case NO_TYPE:
			 return "no_type";
			default:
				System.err.println("ERROR: Fall through in Type enumeration!");
				System.exit(1);
				return ""; // Never reached.
		}
	}

	public static boolean isNumber(Type type ) {
		return type == INT_TYPE || type == FLOAT32_TYPE;
	}

	public static Type numberTypeWidening(Type left, Type right) {
		if(!isNumber(left) || !isNumber(right)) {
			return null;
		}
		if (left == FLOAT32_TYPE || right == FLOAT32_TYPE) {
			return FLOAT32_TYPE;
		}
		return INT_TYPE;
	}

	public static String listToString(List<Type> types) {
		
		StringBuilder sb = new StringBuilder();
		for (Type t : types) {
			sb.append(t.toString());
			sb.append(", ");
		}
		return sb.toString();
	}

	public static boolean isBothNumbers(Type r_type, Type l_type) {
		return isNumber(l_type) && isNumber(r_type);
	}

  public static boolean isI2FWideningNeeded(Type r, Type l) {
    return (r == INT_TYPE && l == FLOAT32_TYPE) || (r == FLOAT32_TYPE && l == INT_TYPE);
  }

  public static boolean isI2FTarget(Type type) {
    return type == INT_TYPE;
  }
}
