package code;

import ast.AST;
import tables.FunctionEntry;
import tables.FunctionTable;
import tables.StrTable;
import tables.VarTable;

public class FuncExecutor {
  // Tudo privado e final para simplificar.
	private final DataStack stack;


  private final CallStack callStack;
	private final FunctionTable ft;
	private final Memory memory;
	private final StrTable st;
	private final VarTable vt;
	private final Cpu cpu;
	private final Io io;

  public FuncExecutor(
    DataStack stack,
    CallStack callStack,
    FunctionTable ft,
    Memory memory,
    StrTable st,
    VarTable vt,
    Cpu cpu,
    Io io
    ) {
    this.stack = stack;
    this.callStack = callStack;
    this.ft = ft;
    this.memory = memory;
    this.st = st;
    this.vt = vt;
    this.cpu = cpu;
    this.io = io;
  }

  public Void call(FunctionEntry funcEntry) {
    FuncInterpreter funcInterpreter = new FuncInterpreter(stack, callStack, ft, memory, st, vt, cpu, io);
    callStack.push(funcEntry.id, stack);
    funcInterpreter.visit(funcEntry.declareNode.getChild(1)); // body
    callStack.popFrame();
    return null;
  }


  private class FuncInterpreter extends Interpreter {
    private boolean isAlreadyReturned = false;
  
    public FuncInterpreter(
      DataStack stack,
      CallStack callStack,
      FunctionTable ft,
      Memory memory,
      StrTable st,
      VarTable vt,
      Cpu cpu,
      Io io
    ) {
      super(stack, callStack, ft, memory, st, vt, cpu, io);
    }

    @Override
    public Void visit(AST node) {
      if (isAlreadyReturned)
        return null;
      return super.visit(node);
    }

    @Override
	  protected Void visitReturn(AST node) {
      super.visitReturn(node);
      isAlreadyReturned = true;
      return null;
    }
  }
}
