package backend.commons;

public interface CodeOutput {
  void write(String str);
  void writef(String format, Object ... args);
  void writeln(String str);
  void writelnf(String format, Object ... args);

  // writes with identation
  void iwrite(String str);
  void iwritef(String format, Object ... args);
  void iwriteln(String str);
  void iwritelnf(String format, Object ... args);

  void indent();
  void unindent();
}
