package code;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import ast.AST;
import tables.FunctionEntry;
import tables.VarEntry;
import tables.VarTable;

public class StackFrame {
  final int framePointer;
  final Map<Integer, Integer> vars;

  final static int NO_ADDRESS = -1;
  
  public StackFrame(int funcIdx, VarTable varTable, Memory memory, Map<Integer,Integer> globalVar) {
    framePointer = memory.size();
    vars = new LinkedHashMap<Integer, Integer>();
    for (int i = 0; i < varTable.size(); i++) {
      VarEntry var = varTable.get(i);
      if(var.funcId == funcIdx) {
        int address = i + framePointer;
        memory.add(0);
        this.vars.put(var.index, address);
      }
    }
    vars.putAll(globalVar);
  }

  public int getVarAddress(int varId) {
    if(!vars.containsKey(varId)) return StackFrame.NO_ADDRESS;
    return vars.get(varId);
  }

  public void clearMemory(Memory memory) {
    memory.setSize(framePointer);
  }

  public int getVarAdress(int idx){
    return vars.get(idx);
  }
}
