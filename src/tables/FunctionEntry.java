package tables;

import java.util.List;

import ast.AST;
import typing.Type;

public class FunctionEntry {
  public final String name;
  public final int line;
  public AST funcDeclare;

  public final List<Type> params;
  public final List<Type> returns;

  FunctionEntry(String name, int line, List<Type> params, List<Type> returns) {
    this.name = name;
    this.line = line;
    this.params = params;
    this.returns = returns;
  }

  public void setFuncDeclare(AST funcDeclare) {
    this.funcDeclare = funcDeclare;
  }
  
}