package tables;

import java.util.List;

import typing.SpecialType;
import typing.Type;

public final class VarEntry {
  public final String name;
  public final int line;
  public final int scope;
  public final Type type;
  public final SpecialType special;

  // var arr = [1, 2, fun()]

  // so se aplica a funcao
  public final List<Type> funcParams;
  public final List<Type> funcReturn;

  // so se aplica a array (tirar duvida com professor)
  // public final int size;

  VarEntry(String name, int line, int scope, SpecialType special,
    List<Type> funcParams, List<Type> funcReturn
    ) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = Type.NIL_TYPE;
    this.special = special;
    this.funcParams = funcParams;
    this.funcReturn = funcReturn;
  }

  VarEntry(String name, int line, int scope, Type type, SpecialType special) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.special = special;
    this.funcParams = null;
    this.funcReturn = null;
  }

  VarEntry(String name, int line, int scope, Type type) {
    this.name = name;
    this.line = line;
    this.scope = scope;
    this.type = type;
    this.special = null;
    this.funcParams = null;
    this.funcReturn = null;
  }
}
