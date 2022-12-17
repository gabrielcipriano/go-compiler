package typing;

// Enumeração dos tipos especiais
@Deprecated // function ganhou a propria table, varTable agora utiliza isArray (boolean)
public enum SpecialType {
	ARRAY("array"),
	FUNCTION("func");

	private String name;

	SpecialType(String name) {
		this.name = name;
	}

	SpecialType() {

	}

	public String getName() {
		return name;
	}

	public String toString() {
		switch (this) {
			case ARRAY:
				return "array";
			case FUNCTION:
				return "func";
			default:
				System.err.println("ERROR: Fall through in Type enumeration!");
				System.exit(1);
				return ""; // Never reached.
		}
	}
}
