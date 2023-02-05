package backend;

import ast.AST;
import ast.ASTBaseVisitor;
import backend.commons.CodeOutput;
import backend.wasm.WasmEmitter;
import backend.wasm.WasmType;
import tables.FunctionTable;
import tables.StrTable;
import tables.VarTable;

import static backend.wasm.WasmType.f32;
import static backend.wasm.WasmType.i32;

import java.util.Iterator;

public class CodeGenerator extends ASTBaseVisitor<Void> {
  private final FunctionTable ft;
	private final StrTable st;
	private final VarTable vt;

  private final CodeOutput output = new StdPrinter();
  private final WasmEmitter emitter;

  CodeGenerator(StrTable st, VarTable vt, FunctionTable ft) {
    this.st = st;
		this.vt = vt;
		this.ft = ft;
  
    this.emitter = new WasmEmitter(output);
  }

  @Override
  protected Void visitAssign(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitShortVarDecl(AST node) {
    int idx = node.getChild(0).intData;
    var entry = vt.get(idx);
    String label = entry.name;

    if (entry.isGlobal()) {
      // TODO: atualmente, declaração de variaveis globais só suporta int e float literais
      var lit = node.getChild(1);
      emitter.emitGlobalDeclare(label, lit.isFloat() ? lit.floatData : lit.intData);
      return null;
    }

    emitter.emitLocalDeclare(entry.isFloat() ? f32 : i32, label);
    
    visit(node.getChild(1));

    emitter.emitLocalSet(label);
    
    return null;
  }

  @Override
  protected Void visitProgram(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitBlock(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitAssignList(AST node) {
    return visitAllChildren(node);
  }

  @Override
  protected Void visitVarDecl(AST node) {
    int idx = node.intData;
    var entry = vt.get(idx);
    String label = entry.name;

    if (entry.isGlobal())
      emitter.emitGlobalDeclare(label, entry.isFloat() ? 0.0f : 0);
    else
      emitter.emitLocalDeclare(entry.isFloat() ? f32 : i32, label);
    
    return null;
  }

  @Override
  protected Void visitVarAssign(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitVarDeclList(AST node) {
    return visitAllChildren(node);
  }

  @Override
  protected Void visitFuncDecl(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitReturn(AST node) {
    emitter.emitReturn();
    return null;
  }

  @Override
  protected Void visitIf(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitIfClause(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitElse(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitFor(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitForClause(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitIncrement(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitDecrement(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitRead(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitPrint(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitIntLit(AST node) {
    int value = node.intData;
    emitter.emitConst(value);
    return null;
  }

  @Override
  protected Void visitFloatLit(AST node) {
    float value = node.floatData;
    emitter.emitConst(value);
    return null;
  }

  @Override
  protected Void visitBoolLit(AST node) {
    // Boolean values in WebAssembly are represented as values of type i32.
    // In a boolean context, such as a br_if condition, any non-zero value is 
    // interpreted as true and 0 is interpreted as false.
    boolean isTrue = node.getBoolData();
    emitter.emitConst(isTrue ? 1 : 0);
    return null;
  }

  @Override
  protected Void visitStrLit(AST node) {
    // TODO lidar com string literals
    return null;
  }

  @Override
  protected Void visitNilLit(AST node) {
    // TODO? lidar com nil literals
    return null;
  }

  @Override
  protected Void visitVarUse(AST node) {
    int idx = node.intData;
    var entry = vt.get(idx);
    String label = entry.name;
  
    if (entry.isGlobal())
      emitter.emitGlobalGet(label);
    else 
      emitter.emitLocalGet(label);

    return null;
  }

  @Override
  protected Void visitFuncCall(AST node) {
    visitAllChildren(node);
    int idx = node.intData;

    String funcLabel = ft.get(idx).name;

    emitter.emitCall(funcLabel);
    return null;
  }

  @Override
  protected Void visitMinus(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;
  
    if (!node.hasChild(1)) { // unary
      // "0 - value" inverts signal
      emitter.emitConst(type == i32 ? 0 : 0.0f);
      visit(node.getChild(0));
    } else {
      visit(node.getChild(0));
      visit(node.getChild(1));
    }

    emitter.emitSub(type);
    return null;
  }

  @Override
  protected Void visitPlus(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;
  
    if (!node.hasChild(1))// unary
      return null;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitAdd(type);
    return null;
  }

  @Override
  protected Void visitDiv(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitDiv(type);
    return null;
  }

  @Override
  protected Void visitTimes(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitMul(type);
    return null;
  }

  @Override
  protected Void visitLess(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitLess(type);
    return null;
  }

  @Override
  protected Void visitLessEqual(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitLessEq(type);
    return null;
  }

  @Override
  protected Void visitGreaterEqual(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitGreaterEq(type);
    return null;
  }

  @Override
  protected Void visitGreater(AST node) {
    WasmType type = node.getChild(0).isInt() ? i32 : f32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitGreater(type);
    return null;
  }

  @Override
  protected Void visitEqual(AST node) {
    // i32 is also for boolean data
    WasmType type = node.getChild(0).isFloat() ? f32 : i32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitEq(type);
    return null;
  }

  @Override
  protected Void visitNotEqual(AST node) {
    // i32 is also for boolean data
    WasmType type = node.getChild(0).isFloat() ? f32 : i32;

    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitNotEq(type);
    return null;
  }

  @Override
  protected Void visitAnd(AST node) {
    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitAnd();
    return null;
  }

  @Override
  protected Void visitOr(AST node) {
    visit(node.getChild(0));
    visit(node.getChild(1));

    emitter.emitOr();
    return null;
  }

  @Override
  protected Void visitNot(AST node) {
    visit(node.getChild(0));

    emitter.emitNot();
    return null;
  }

  @Override
  protected Void visitI2F(AST node) {
    visit(node.getChild(0));

    emitter.emitI2F();
    return null;
  }

  // *** HELPERS ***
	private Void visitAllChildren(AST node) {
		Iterator<AST> children = node.iterateChildren();
		while(children.hasNext())
			visit(children.next());
		return null;
	}

}
