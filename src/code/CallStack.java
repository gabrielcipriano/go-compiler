package code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tables.FunctionTable;
import tables.VarTable;

public class CallStack extends ArrayDeque<StackFrame>{
  private final VarTable vt;
  private final FunctionTable ft;
  private final Memory memory;

  public CallStack(Memory memory, VarTable vt, FunctionTable ft) {
    super();
    this.vt = vt;
    this.ft = ft;
    this.memory = memory;
    this.add(new StackFrame(vt, memory));
    System.out.println(this.getLast().toString());
  }
  
  public int getVarAddress(int varId) {
    return this.getLast().getVarAddress(varId);
  }

  private List<Word> computeParams(DataStack stack, int num) {
    List<Word> params = new ArrayList<Word>();

    for (int i = 0; i < num; i++)
      params.add(0, stack.pop());
    return params;
  }

  public void push(int funcId, DataStack stack){
    List<Word> params = computeParams(stack, this.ft.get(funcId).params.size());
    this.add(
      new StackFrame(this.ft.get(funcId), params, vt, memory, this.getGlobalAddressMap())
    );

		String debug = System.getenv().get("DEBUG") == null ? "false" : System.getenv().get("DEBUG");
		boolean isDebugging = debug.equals("true") || debug.equals("1");
    if (isDebugging)
      System.out.println(this.getLast().toString());
  }

  public StackFrame popFrame(){
    StackFrame last = this.removeLast();
    last.clearMemory();
    return last;
  }

  private Map<Integer, Integer> getGlobalAddressMap(){
    return this.getFirst().vars;
  }
}
