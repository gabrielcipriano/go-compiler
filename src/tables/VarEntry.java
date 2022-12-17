package tables;

import typing.Type;

public final class VarEntry {
  public final String name;
  public final int line;
  public final int scope;
  public final Type type;
  public final boolean isArray;

  VarEntry(String name, int line, int scope, Type type) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.isArray = false;
  }

  VarEntry(String name, int line, int scope, Type type, boolean isArray) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.isArray = isArray;
  }
}
