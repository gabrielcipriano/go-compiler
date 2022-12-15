package typing;

public enum Conv {
	// B2I,  // Bool to Int
  // B2R,  // Bool to Real
  // B2S,  // Bool to String
	I2F,  	 // Int to Float
	// I2S,  // Int to String
	// R2S,  // Real to String
	NONE; // No type conversion

	// Classe que define as informações de unificação para os tipos em expressões.
	public static final class Unif {
		public final Type type; // Tipo unificado
		public final Conv lc; 	// Conversão do lado esquerdo
		public final Conv rc; 	// Conversão do lado direito

		public Unif(Type type, Conv lc, Conv rc) {
			this.type = type;
			this.lc = lc;
			this.rc = rc;
		}
	}
}
