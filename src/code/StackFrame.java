package code;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ast.AST;
import tables.FunctionEntry;
import tables.FunctionTable;
import tables.VarEntry;
import tables.VarTable;

public class StackFrame {
  final int framePointer;
  private final FunctionEntry func;
  final Map<Integer, Integer> vars;
  final Memory memory;

  final static int GLOBAL_FRAME_VARS = -1;
  final static int NO_ADDRESS = -1;
  
  public StackFrame(FunctionEntry func, List<Word> params, VarTable varTable, Memory memory, Map<Integer,Integer> globalVar) {
    this.framePointer = memory.size();
    this.func = func;
    this.memory = memory;
    this.vars = new LinkedHashMap<Integer, Integer>();
    int offset = 0;
  
    vars.putAll(globalVar);
    for (int i = 0; i < varTable.size(); i++) {
      VarEntry var = varTable.get(i);
      if(var.funcId == func.id) {
        int address = framePointer + offset;
        if (params.size() > 0)
          memory.add(params.remove(0));
        else
          memory.add(0);
        this.vars.put(var.index, address);
        offset++;
      }
    }
  }

  public StackFrame(VarTable varTable, Memory memory) {
    this.framePointer = memory.size();
    this.func = null;
    this.memory = memory;
    this.vars = new LinkedHashMap<Integer, Integer>();
    int offset = 0;
  
    for (int i = 0; i < varTable.size(); i++) {
      VarEntry var = varTable.get(i);
      if(var.funcId == GLOBAL_FRAME_VARS) {
        int address = framePointer + offset;
        memory.add(0);
        this.vars.put(var.index, address);
        offset++;
      }
    }
  }

  public int getVarAddress(int varId) {
    if(!vars.containsKey(varId)) return StackFrame.NO_ADDRESS;
    return vars.get(varId);
  }

  public void clearMemory() {
    memory.setSize(framePointer);
  }

  public int getVarAdress(int idx){
    return vars.get(idx);
  }

  public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
    String funcName = func != null ? func.name : "global";
		f.format("*** StackFrame %s\n", funcName);
		f.format("%4s %10s %5s\n", "id", "addrs", "value");
    var entrySet = vars.entrySet().iterator();
		while (entrySet.hasNext()) {
      var entry = entrySet.next();
			f.format("%4s %10s %5s\n", entry.getKey(), entry.getValue(), memory.loadi(entry.getValue()));
		}
		f.format("\n");
		f.close();
		return sb.toString();
	}
}
