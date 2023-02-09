package tables;

import java.util.Formatter;

import typing.Type;

public final class VarEntry {
  public int index;
  public final String name;
  public final int line;
  public final int scope;
  public final Type type;
  public final int arraySz;
  public final boolean isConstant;
  public int funcId;

	public static final int NO_FUNCTION = -1;

  VarEntry(String name, int line, int scope, Type type, boolean isConst) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.arraySz = 0;
    this.isConstant = isConst;
    this.funcId = VarEntry.NO_FUNCTION;
  }

  VarEntry(String name, int line, int scope, Type type, int arraySz, boolean isConst) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.arraySz = arraySz;
    this.isConstant = isConst;
    this.funcId = VarEntry.NO_FUNCTION;
  }

  public void setFuncId(int funcId) {
    this.funcId = funcId;
  }

  public void setIndex(int id) {
    this.index = id;
  }

  public boolean isConst() {
    return this.isConstant;
  }

  public boolean isArray(){
    return arraySz != 0;
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

  public boolean isGlobal() {
    return this.scope == 1;
  }

  public String toStringNonVerbose() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
    String typeStr = (this.isArray() ? "[" + arraySz + "]" : "") + type.toString();
    f.format("%3d %10s %10s\n", line, name, typeStr).close();
		return sb.toString();
	}
}
