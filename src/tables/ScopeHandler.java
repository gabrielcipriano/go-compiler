package tables;

import java.util.LinkedList;

import typing.Type;

public final class ScopeHandler {
  LinkedList<LinkedList<VarEntry>> scopes = new LinkedList<LinkedList<VarEntry>>();

  public int scopeDepth = 0;

  public ScopeHandler() {
  }

  public Void push() {
    this.scopes.add(new LinkedList<VarEntry>());
    this.scopeDepth++;
    return null;
  }

  public Void pop() {
    this.scopes.removeLast();
    this.scopeDepth--;
    return null;
  }

  public Void addVar(String name, int line, Type type, int arraySz) {
    this.scopes.getLast().add(new VarEntry(name, line, this.scopeDepth, type, arraySz));
    return null;
  }

  public Void addVar(String name, int line, Type type) {
    this.scopes.getLast().add(new VarEntry(name, line, this.scopeDepth, type));
    return null;
  }

  public VarEntry lookupVar(String name) {
    for (LinkedList<VarEntry> scope : this.scopes) {
      for (VarEntry varEntry : scope) {
        if(varEntry.name.equals(name)) {
          return varEntry;
        }
      }
    }
    return null;
  }
}
