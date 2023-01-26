package tables;

import java.util.LinkedList;

import typing.Type;

public final class ScopeHandler {
  LinkedList<Scope> scopes = new LinkedList<Scope>();

	public final int GLOBAL_SCOPE = 1;

  public int scopesCounter;

  public ScopeHandler() {
    this.scopes.add(new Scope(GLOBAL_SCOPE));
    this.scopesCounter = 1;
  }

  public Void push() {
    this.scopes.add(new Scope(++scopesCounter));
    return null;
  }

  public boolean isGlobalScope() {
    return getScopeDepth() == GLOBAL_SCOPE;
  }

  public int getScopeDepth() {
    return this.scopes.size();
  }

  public Void pop() {
    this.scopes.removeLast();
    return null;
  }

  public VarEntry addVar(String name, int line, Type type, int arraySz, boolean isConst) {
    VarEntry newVar = new VarEntry(name, line, this.scopes.getLast().id, type, arraySz, isConst);
    this.scopes.getLast().add(newVar);
    return newVar;
  }

  public VarEntry addVar(String name, int line, Type type, boolean isConst) {
    VarEntry newVar = new VarEntry(name, line, this.scopes.getLast().id, type, isConst);
    this.scopes.getLast().add(newVar);
    return newVar;
  }

  public VarEntry lookupVar(String name) {
    var scopesStack = this.scopes.descendingIterator();
    while (scopesStack.hasNext()) {
      LinkedList<VarEntry> scope  = scopesStack.next();
      for (VarEntry varEntry : scope) {
        if(varEntry.name.equals(name)) {
          return varEntry;
        }
      }
    }
    return null;
  }

  private final class Scope extends LinkedList<VarEntry> {
    protected final int id;
    Scope(int id) {
      super();
      this.id = id;
    }
  }
}
