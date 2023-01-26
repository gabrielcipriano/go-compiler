package code;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;

import tables.FunctionTable;
import tables.VarTable;

public class CallStack extends ArrayDeque<StackFrame>{
  public final int GLOBAL_VARS = -1;
  private final VarTable vt;
  private final FunctionTable ft;
  private final Memory memory;

  public CallStack(Memory memory, VarTable vt, FunctionTable ft) {
    super();
    this.vt = vt;
    this.ft = ft;
    this.memory = memory;
    Map<Integer,Integer> emptyAddressMap = new LinkedHashMap<Integer,Integer>();
    this.add(new StackFrame(GLOBAL_VARS, vt, memory, emptyAddressMap));
    System.out.println(this.getLast().toString(ft));
  }
  
  public int getVarAddress(int varId) {
    return this.getLast().getVarAddress(varId);
  }

  public void push(int funcId){
    this.add(new StackFrame(funcId, vt, memory, this.getGlobalAddressMap()));
    System.out.println(this.getLast().toString(ft));
  }

  @Override
  public StackFrame pop(){
    StackFrame last = this.removeLast();
    last.clearMemory();
    return last;
  }

  private Map<Integer, Integer> getGlobalAddressMap(){
    return this.getFirst().vars;
  }
}