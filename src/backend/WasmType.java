package backend;

public enum WasmType {
  i32,
  f32;

	public String toString() {
    switch(this) {
      case f32:  return "f32";
      case i32:  
      default:   return "i32";
    }
  }
}
