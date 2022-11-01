package typing;

// Enumeração dos tipos primitivos que podem existir em EZLang.
public enum Type {
  INT_TYPE,
  FLOAT32_TYPE,
  STRING_TYPE,
  BOOLEAN_TYPE;

	public String toString() {
		switch(this) {
			case INT_TYPE:	return "int";
			case FLOAT32_TYPE: return "float32";
			case BOOLEAN_TYPE: return "bool";
			case STRING_TYPE: 	return "string";
			default:
				System.err.println("ERROR: Fall through in Type enumeration!");
				System.exit(1);
				return ""; // Never reached.
		}
	}
}
