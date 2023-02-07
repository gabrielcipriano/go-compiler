package backend.wasm;

import backend.commons.CodeOutput;

public class WasmEmitter {
  public final WasmLabels labels = new WasmLabels();
  public final CodeOutput out;

  public WasmEmitter(CodeOutput output) {
    this.out = output;
  }

  public void emitComment(String comment) {
    out.iwriteln(";; " + comment);
  }

  public void emitConst(int value) {
    out.iwriteln("(i32.const " + value + ")");
  }

  public void emitConst(float value) {
    out.iwriteln("(f32.const " + value + ")");
  }

  public void emitLocalDeclare(WasmType type, String label) {
    out.iwritelnf("(local $%s %s)", label, type);
  }

  public void emitLocalGet(int idx) {
    out.iwriteln("(local.get $" + idx + ")");
  }

  public void emitLocalGet(String label) {
    out.iwriteln("(local.get $" + label + ")");
  }

  public void emitLocalSet(String label) {
    out.iwriteln("(local.set $" + label + ")");
  }
  
  /** The local.tee instruction sets the value of a local variable and loads the value onto the stack. */
  public void emitLocalTee(String label) {
    out.iwriteln("(local.tee $" + label + ")");
  }

  public void emitGlobalDeclare(String label, int value) {
    out.iwritelnf("(global $%s (mut i32) (i32.const %d))", label, value);
  }

  public void emitGlobalDeclare(String label, float value) {
    out.iwritelnf("(global $%s (mut f32) (f32.const %f))", label, value);
  }


  public void emitGlobalGet(String label) {
    out.iwriteln("(global.get $" + label + ")");
  }

  public void emitGlobalSet(String label) {
    out.iwriteln("(global.set $" + label + ")");
  }

  public void emitGlobalTee(String label) {
    emitGlobalSet(label);
    emitGlobalGet(label);
  }

  /** global auxiliar var for temporary values */
  public void emitAuxGet(WasmType type) {
    String label = "aux_" + type;
    emitGlobalGet(label);
  }

  /** global auxiliar var for temporary values */
  public void emitAuxSet(WasmType type) {
    String label = "aux_" + type;
    emitGlobalSet(label);
  }

  /** global auxiliar var for temporary values.
   * tee instruction sets the value of a local variable and loads the value onto the stack */
  public void emitAuxTee(String type) {
    String label = "aux_" + type;
    emitGlobalTee(label);
  }

  /** The drop instruction, pops a value from the stack, and discards it. */
  public void emitDrop() {
    out.iwriteln("(drop)");
  }

  /** The drop instruction, pops a value from the stack, and discards it. */
  public void emitDrop(int num) {
    for (int i = 0; i < num; i++) {
      out.iwriteln("(drop)");
    }
  }

  /** The load instructions, are used to load a number from memory onto the stack. */
  public void emitLoad(WasmType type) {
    out.iwriteln("(" + type + ".load)");
  }

  /** The store instructions, are used to store a number from memory onto the stack. */
  public void emitStore(WasmType type) {
    out.iwriteln("(" + type + ".store)");
  }

  /** function call */
  public void emitCall(String funcLabel) {
    out.iwriteln("(call $" + funcLabel + ")");
  }

  // OPERATIONS

  public void emitAdd(WasmType type) {
    out.iwriteln("(" + type + ".add)");
  }

  public void emitSub(WasmType type) {
    out.iwriteln("(" + type + ".sub)");
  }

  public void emitMul(WasmType type) {
    out.iwriteln("(" + type + ".mul)");
  }

  public void emitDiv(WasmType type) {
    out.iwriteln("(" + type + ".div)");
  }

  public void emitI2F() {
    out.iwriteln("(f32.convert_i32_s)");
  }

  public void emitEndIf(){
    emitEnd();
    emitEnd();
  }

  public void emitThenBegin(){
    out.iwriteln("(then");
    out.indent();
  }

  public void emitIfBegin(){
    out.iwriteln("(if");
    out.indent();
  }

  public void emitElseBegin(){
    out.iwriteln("(else");
    out.indent();
  }

  public void emitBlock(String label){
    out.iwritelnf("(block $%S",label);
    out.indent();
  }
  public void emitFor(String label){
    out.iwritelnf("(loop $%S",label);
    out.indent();
  }


  public void emitLess(WasmType type) {
    out.iwriteln("(" + type + ".lt_s)");
  }

  public void emitLessEq(WasmType type) {
    out.iwriteln("(" + type + ".le_s)");
  }

  public void emitGreater(WasmType type) {
    out.iwriteln("(" + type + ".gt_s)");
  }

  public void emitGreaterEq(WasmType type) {
    out.iwriteln("(" + type + ".ge_s)");
  }

  public void emitEq(WasmType type) {
    out.iwriteln("(" + type + ".eq)");
  }
  
  public void emitNotEq(WasmType type) {
    out.iwriteln("(" + type + ".ne)");
  }

  /** eqz instruction returns true (1) if the operand is equal to zero, or false (0) otherwise.  */
  public void emitEqz() {
    out.iwriteln("(i32.eqz)");
  }

  public void emitNot() {
    // eqz serves as a "logical not" operation which can be used to invert boolean values.
    emitEqz();
  }

  public void emitAnd() {
    out.iwriteln("(i32.and)");
  }

  public void emitOr() {
    out.iwriteln("(i32.or)");
  }

  // FLOW CONTROL

  /** The br statement branches to a loop, block, or if. */
  public void emitBr(String label) {
    out.iwriteln("(br " + label + ")");
  }

  /** A variant of br, for branching on condition. */
  public void emitBrIf(String label) {
    // Boolean values in WebAssembly are represented as values of type i32.
    // In a boolean context, such as a br_if condition, any non-zero value is 
    // interpreted as true and 0 is interpreted as false.
    out.iwriteln("(br_if " + label + ")");
  }

  // /** The block statement creates a label that can later be branched OUT of with a br. */
  // public void emitBlock(String label) {
  //   out.iwriteln("( " + label + ")");
  // }

  // FUNCTION

  public void emitFuncBegin(String label) {
    out.iwritef("(func $%1$s (export \"%1$s\") ", label);
    out.indent();
  }

  public void emitParam(WasmType type, String label) {
    out.writef("(param $%s %s) ", label, type);
  }

  public void emitResult(WasmType type) {
    out.writef("(result %s)", type);
  }

  public void emitReturn() {
    out.iwriteln("(return)");
  }

  /** after an instruction like "emitAbcBegin" you must emit an End at some point */
  public void emitEnd() {
    out.unindent();
    out.iwriteln(")");
  }

  public void emitStringData(int offSet, String str){
    out.iwritelnf("(data(i32.const %d) %s)", offSet, str);
  }

  /** calls the start function (usually main) */
  public void emitStart(String label) {
    out.iwriteln("(start $" + label + ")");
  }

  public void emitModuleBegin() {
    out.iwriteln("(module ");
    out.indent();
  }

  public void emitRuntimeSetup() {
    emitComment("Importing std i/o");
    String importFormat = "(import \"std\" \"%1$s\" (func $%1$s %2$s))";
    out.iwritelnf(importFormat, RuntimeStd.printlnInt, "(param i32)");
    out.iwritelnf(importFormat, RuntimeStd.printlnBoolean, "(param i32)");
    out.iwritelnf(importFormat, RuntimeStd.printlnFloat, "(param f32)");
    out.iwritelnf(importFormat, RuntimeStd.printlnString, "(param i32) (param i32)");
    emitNewLine();
    emitComment("creating and exporting memory");
    out.iwriteln("(memory $memory 1)");
    out.iwriteln("(export \"memory\" (memory $memory))");
    emitNewLine();
  }

  public void emitNewLine() {
    out.iwriteln("");
  }
}
