package backend.wasm;

public enum RuntimeStd {
  printlnInt,
  printlnFloat,
  printlnString,
  printlnBoolean;

	public String toString() {
    switch(this) {
      case printlnFloat:  return "printlnFloat";
      case printlnString: return "printlnString";
      case printlnBoolean: return "printlnBoolean";
      case printlnInt:
      default:            return "printlnInt";
    }
  }
}
