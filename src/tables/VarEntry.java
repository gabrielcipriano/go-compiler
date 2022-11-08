package tables;

import typing.SpecialType;
import typing.Type;

public final class VarEntry {
  public final String name;
  public final int line;
  public final int scope;
  public final Type type;
  public final SpecialType special;

  VarEntry(String name, int line, int scope, Type type, SpecialType special) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.special = special;
  }

  VarEntry(String name, int line, int scope, Type type) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.special = null;
  }
}
