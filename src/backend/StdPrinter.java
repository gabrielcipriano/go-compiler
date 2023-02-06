package backend;

import backend.commons.CodeOutput;
import backend.commons.Indent;

public class StdPrinter implements CodeOutput {
  private Indent idt = new Indent(); // default indent (no indent)

  @Override
  public void write(String str) {
      System.out.print(str);
  }

  @Override
  public void writef(String format, Object... args) {
    System.out.printf(format, args);
  }

  @Override
  public void iwrite(String str) {
      System.out.print(idt + str);
  }

  @Override
  public void iwritef(String format, Object... args) {
    System.out.printf(idt + format, args);
  }

  @Override
  public void writeln(String str) {
    System.out.println(str);
  }

  @Override
  public void writelnf(String format, Object... args) {
    System.out.printf(format + "%n", args);
  }

  @Override
  public void iwriteln(String str) {
    System.out.println(idt + str);
  }

  @Override
  public void iwritelnf(String format, Object... args) {
    System.out.printf(idt + format + "%n", args);
  }

  @Override
  public void indent() {
    this.idt.rightShift();
  }

  @Override
  public void unindent() {
    this.idt.leftShift();
  }
}
