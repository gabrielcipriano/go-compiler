package backend.wasm;

public enum RuntimeStd {
  printlnInt,
  printlnFloat,
  printlnString;

	public String toString() {
    switch(this) {
      case printlnFloat:  return "printlnFloat";
      case printlnString: return "printlnString";
      case printlnInt:
      default:            return "printlnInt";
    }
  }
}
