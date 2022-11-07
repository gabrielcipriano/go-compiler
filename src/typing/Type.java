package typing;

// Enumeração dos tipos primitivos
public enum Type {
	INT_TYPE("int"),
	FLOAT32_TYPE("float32"),
	STRING_TYPE("string"),
	BOOLEAN_TYPE("bool"),
	INFERED_TYPE("infer");

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
			case INFERED_TYPE:
				return "infer";
			default:
				System.err.println("ERROR: Fall through in Type enumeration!");
				System.exit(1);
				return ""; // Never reached.
		}
	}
}
