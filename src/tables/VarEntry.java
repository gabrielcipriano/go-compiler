package tables;

import typing.Type;

public final class VarEntry {
  public final String name;
  public final int line;
  public final int scope;
  public final Type type;
  public final int arraySz;

  VarEntry(String name, int line, int scope, Type type) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.arraySz = -1;
  }

  VarEntry(String name, int line, int scope, Type type, int arraySz) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.arraySz = arraySz;
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
