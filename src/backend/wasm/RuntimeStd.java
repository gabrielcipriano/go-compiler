package backend.wasm;

public enum RuntimeStd {
  printlnInt,
  printlnFloat,
  printlnString,
  printlnBoolean,
  loadArrValInt,
  loadArrValFloat;

	public String toString() {
    switch(this) {
      case loadArrValInt:  return "__loadArrValInt";
      case loadArrValFloat:  return "__loadArrValFloat";
      case printlnFloat:  return "printlnFloat";
      case printlnString: return "printlnString";
      case printlnBoolean: return "printlnBoolean";
      case printlnInt:
      default:            return "printlnInt";
    }
  }
}
