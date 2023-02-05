package backend.commons;

public interface CodeOutput {
  void write(String str);
  void writeln(String str);
  // writes with identation
  void iwrite(String str);
  void iwriteln(String str);


  void setIndent(Indent idt);
}
