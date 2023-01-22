package tables;

import typing.Type;

public final class VarEntry {
  public final String name;
  public final int line;
  public final int scope;
  public final Type type;
  public final int arraySz;
  public final boolean isConstant;

  VarEntry(String name, int line, int scope, Type type, boolean isConst) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.arraySz = -1;
    this.isConstant = isConst;
  }

  VarEntry(String name, int line, int scope, Type type, int arraySz, boolean isConst) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.arraySz = arraySz;
    this.isConstant = isConst;
  }

  public boolean isConst() {
    return this.isConstant;
  }

  public boolean isArray(){
    return arraySz > 0;
  }

  public boolean isInt() {
		return type == Type.INT_TYPE;
	}

	public boolean isFloat() {
		return type == Type.FLOAT32_TYPE;
	}

	public boolean isBool() {
		return type == Type.BOOLEAN_TYPE;
	}

  public boolean isString() {
    return type == Type.STRING_TYPE;
  }
}
