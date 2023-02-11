package backend.wasm;

public enum RuntimeStd {
  println,
  printlnInt,
  printlnFloat,
  printlnString,
  printlnBoolean,
  getArrValAddress,
  getArrLen,
  arrAlLoc,
  randInt
  ;

	public String toString() {
    switch(this) {
      case arrAlLoc:  return "__arrAlLoc";
      case getArrValAddress:  return "__getArrValAddress";
      case getArrLen:  return "__getArrLen";
      case println:  return "println";
      case printlnFloat:  return "printlnFloat";
      case printlnString: return "printlnString";
      case printlnBoolean: return "printlnBoolean";
      case randInt: return "randInt";
      case printlnInt:
      default:            return "printlnInt";
    }
  }
}
