package backend.wasm;

import backend.commons.LabelCounter;

public class WasmLabels {
  public final LabelCounter global = new LabelCounter("global");
  public final LabelCounter func = new LabelCounter("func");
  public final LabelCounter local = new LabelCounter("local");
  public final LabelCounter block = new LabelCounter("block");
  public final LabelCounter loop = new LabelCounter("loop");

  void resetFuncScopeLabels() {
    this.local.reset();
    this.block.reset();
    this.loop.reset();
  }
}
