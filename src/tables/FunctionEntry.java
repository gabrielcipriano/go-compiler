package tables;

import java.util.List;

import typing.Type;

public class FunctionEntry {
  public final String name;
  public final int line;

  public final List<Type> params;
  public final List<Type> returns;

  FunctionEntry(String name, int line, List<Type> params, List<Type> returns) {
    this.name = name;
    this.line = line;
    this.params = params;
    this.returns = returns;
  }
}