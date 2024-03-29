package tables;

import java.util.List;

import ast.AST;
import typing.Type;

public class FunctionEntry {
  public final String name;
  public final int line;
  public final int id;
  
  public List<Type> params;
  public final List<Type> returns;

  public AST declareNode;

  FunctionEntry(String name, int line, List<Type> returns, int id) {
    this.name = name;
    this.line = line;
    this.returns = returns;
    this.id = id;
  }

  public void setDeclareNode(AST declareNode) {
    this.declareNode = declareNode;
  }

  public void setParams(List<Type> params) {
    this.params = params;
  }
  
}