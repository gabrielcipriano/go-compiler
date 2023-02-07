package backend;

import ast.AST;
import ast.ASTBaseVisitor;
import backend.commons.CodeOutput;
import backend.wasm.WasmEmitter;
import backend.wasm.WasmType;
import tables.FunctionTable;
import tables.StrTable;
import tables.VarTable;
import typing.Type;

import static backend.wasm.WasmType.f32;
import static backend.wasm.WasmType.i32;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CodeGenerator extends ASTBaseVisitor<Void> {
  private final FunctionTable ft;
	private final StrTable st;
	private final VarTable vt;

  private final CodeOutput output = new StdPrinter();
  private final WasmEmitter emitter;

  private int forCount = 0;

  private final List<Integer> strOffsets = new ArrayList<Integer>();

  public CodeGenerator(StrTable st, VarTable vt, FunctionTable ft) {
    this.st = st;
		this.vt = vt;
		this.ft = ft;
  
    this.emitter = new WasmEmitter(output);
  }

  @Override
  protected Void visitProgram(AST node) {
    emitter.emitModuleBegin();
      emitter.emitRuntimeSetup();

      emitter.emitComment(" adding strings to memory");
      int offSet = 0;
      var strings = st.iterator();
      while(strings.hasNext()){
        String str = strings.next();
        int strSz = str.length()-2; // ignore quotes
        strOffsets.add(offSet);
        emitter.emitInt32Data(offSet, strSz);
        offSet += 4; // i32 = 4bytes
        emitter.emitStringData(offSet, str);
        offSet += strSz;
      }
      emitter.emitGlobalDeclare("offset", offSet);
      emitter.emitNewLine();

      visitAllChildren(node);
    emitter.emitEnd();

    return null;
  }

  @Override
  protected Void visitAssign(AST node) {
    // int idx = node.getChild(0).intData; // do not visits var assign
    // var entry = vt.get(idx);
    // String label = entry.name;
    // if (entry.isGlobal()) {
    //   // TODO: funcao para atribuir esses valores?
    // }
    // visit(node.getChild(1));
    // emitter.emitLocalSet(label);
    return null;
  }

  @Override
  protected Void visitShortVarDecl(AST node) {
    int idx = node.getChild(0).intData; // do not visits var assign
    var entry = vt.get(idx);
    String label = entry.name;

    if (entry.isGlobal()) {
      // TODO: atualmente, declaração de variaveis globais só suporta literais int e float
      var lit = node.getChild(1);
      if (lit.isFloat())
        emitter.emitGlobalDeclare(label, lit.floatData);
      else if (lit.isString())
        emitter.emitGlobalDeclare(label, strOffsets.get(lit.intData));
      else
        emitter.emitGlobalDeclare(label, lit.intData);
      return null;
    }

    emitter.emitLocalDeclare(entry.isFloat() ? f32 : i32, label);
    
    visit(node.getChild(1));
    emitter.emitLocalSet(label);
    
    return null;
  }

  @Override
  protected Void visitBlock(AST node) {
    return visitAllChildren(node);
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
      if (entry.isFloat())
        emitter.emitGlobalDeclare(label,0.0f);
      else
        emitter.emitGlobalDeclare(label, 0);
    else
      emitter.emitLocalDeclare(entry.isFloat() ? f32 : i32, label);
    
    return null;
  }

  @Override
  protected Void visitVarAssign(AST node) {
    int idx = node.intData;
    var entry = vt.get(idx);
    String label = entry.name;

    if (entry.isGlobal())
      emitter.emitGlobalSet(label);
    else
      emitter.emitLocalSet(label);

    return null;
  }

  @Override
  protected Void visitVarDeclList(AST node) {
    return visitAllChildren(node);
  }

  @Override
  protected Void visitFuncDecl(AST node) {
    int funcIdx = node.intData;
    var entry = ft.get(funcIdx);
    String funcLabel = entry.name;

    emitter.emitFuncBegin(funcLabel);
      AST funcParams = node.getChild(0);
      visitFuncParams(funcParams);

      if (entry.returns.size() > 0) {
        var returnType = entry.returns.get(0);
        var wtype = returnType == Type.FLOAT32_TYPE ? f32 : i32;

        emitter.emitResult(wtype);
      }

      emitter.emitNewLine();

      AST funcBody = node.getChild(1);
      visit(funcBody);
    emitter.emitEnd();

    return null;
  }

  @Override
  protected Void visitReturn(AST node) {
    if (node.hasChild(0))
      visit(node.getChild(0));

    emitter.emitReturn();
    return null;
  }

  @Override
  protected Void visitIf(AST node) {
    visit(node.getChild(0)); // if clause
    emitter.emitIfBegin();
      emitter.emitThenBegin();
        visit(node.getChild(1)); // if block
      emitter.emitEnd();
      if(node.hasChild(2)) // has else block
        visit(node.getChild(2));
    emitter.emitEnd();
    return null;
  }

  @Override
  protected Void visitIfClause(AST node) {
    visitAllChildren(node);
    return null;
  }

  @Override
  protected Void visitElse(AST node) {
    emitter.emitElseBegin();
      visit(node.getChild(0));
    emitter.emitEnd();
    return null;
  }

  @Override
  protected Void visitFor(AST node) {
    String blockLabel = "BLOCK_"+forCount;
    String forLabel = "FOR_"+forCount;
    forCount++;
    emitter.emitBlock(blockLabel);
    AST clause = node.getChild(0);
    AST condition;
    if(clause.hasChild(1)){
      visit(clause.getChild(0));
      condition = clause.getChild(1);
    }
    else condition = clause.getChild(0);
    visit(condition);
    emitter.emitEqz();
    emitter.emitBrIf(blockLabel);
    emitter.emitFor(forLabel);
    visit(node.getChild(1));
    if(clause.hasChild(2)){
      visit(clause.getChild(2));
    }
    visit(condition);
    emitter.emitBrIf(forLabel);
    emitter.emitEnd();
    emitter.emitEnd();
    return null;
  }

  @Override
  protected Void visitForClause(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitIncrement(AST node) {
    int idx = node.getChild(0).intData; // do not visits var assign
    var entry = vt.get(idx);
    String label = entry.name;

    if (entry.isGlobal()) {
      emitter.emitGlobalGet(label);
      emitter.emitConst(1);
      emitter.emitAdd(i32);
      emitter.emitGlobalSet(label);
      return null;
    }
    emitter.emitLocalGet(label);
    emitter.emitConst(1);
    emitter.emitAdd(i32);
    emitter.emitLocalSet(label);
    return null;
  }

  @Override
  protected Void visitDecrement(AST node) {
    int idx = node.getChild(0).intData; // do not visits var assign
    var entry = vt.get(idx);
    String label = entry.name;

    if (entry.isGlobal()) {
      emitter.emitGlobalGet(label);
      emitter.emitConst(1);
      emitter.emitSub(i32);
      emitter.emitGlobalGet(label);
      return null;
    }
    emitter.emitLocalGet(label);
    emitter.emitConst(1);
    emitter.emitSub(i32);
    emitter.emitLocalSet(label);
    return null;

  }

  @Override
  protected Void visitRead(AST node) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Void visitPrint(AST node) {
    var expressions = node.iterateChildren();
    while(expressions.hasNext()) {
      var expr = expressions.next();
      visit(expr);

      switch (expr.type) {
        case FLOAT32_TYPE:
          emitter.emitPrintlnFloat();
          break;
        case BOOLEAN_TYPE:
          emitter.emitPrintlnBoolean();
          break;
        case STRING_TYPE:
          emitter.emitPrintlnString();
          break;
        case INT_TYPE:
        default:
          emitter.emitPrintlnInt();
      }
    }
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
    int strIdx = node.intData;
    var strOffset = strOffsets.get(strIdx);

    emitter.emitConst(strOffset);

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
      if (type == i32)
        emitter.emitConst(0);
      else
        emitter.emitConst(0.0f);
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

  // *** CUSTOM VISITORS ***

  /** the param node is actually a var_decl_list  */
  protected void visitFuncParams(AST node) {
    var children = node.iterateChildren();
    while (children.hasNext()) {
      var child = children.next();

      int idx = child.intData;
      var entry = vt.get(idx);
      String varLabel = entry.name;

      var wtype = entry.isFloat() ? f32 : i32;
      
      emitter.emitParam(wtype, varLabel);
    }
  }

  // *** HELPERS ***
	private Void visitAllChildren(AST node) {
		Iterator<AST> children = node.iterateChildren();
		while(children.hasNext()) {
			visit(children.next());
      if(children.hasNext())
        emitter.emitNewLine();
    }
		return null;
	}

}