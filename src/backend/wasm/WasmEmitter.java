package backend.wasm;

import backend.commons.CodeOutput;
import backend.commons.Indent;

public class WasmEmitter {
  public final WasmLabels labels = new WasmLabels();
  public final Indent idt = new Indent();
  public final CodeOutput out;

  public WasmEmitter(CodeOutput output) {
    this.out = output;
    this.out.setIndent(idt);
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

  public void emitLocalGet(int idx) {
    out.iwriteln("(local.get " + idx + ")");
  }

  public void emitLocalGet(String label) {
    out.iwriteln("(local.get " + label + ")");
  }

  public void emitLocalSet(String label) {
    out.iwriteln("(local.set " + label + ")");
  }
  
  /** The local.tee instruction sets the value of a local variable and loads the value onto the stack. */
  public void emitLocalTee(String label) {
    out.iwriteln("(local.tee " + label + ")");
  }

  public void emitGlobalGet(String label) {
    out.iwriteln("(global.get " + label + ")");
  }

  public void emitGlobalSet(String label) {
    out.iwriteln("(global.set " + label + ")");
  }

  /** global auxiliar var for temporary values */
  public void emitAuxGet(WasmType type) {
    String label = "$aux_" + type;
    emitGlobalGet(label);
  }

  /** global auxiliar var for temporary values */
  public void emitAuxSet(WasmType type) {
    String label = "$aux_" + type;
    emitGlobalSet(label);
  }

  /** global auxiliar var for temporary values.
   * tee instruction sets the value of a local variable and loads the value onto the stack */
  public void emitAuxTee(String type) {
    String label = "$aux_" + type;
    emitGlobalSet(label);
    emitGlobalGet(label);
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
    out.iwriteln("(call " + funcLabel + ")");
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

  public void emitReturn() {
    out.iwriteln("(return)");
  }

}
