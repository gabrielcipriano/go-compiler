package backend.commons;

public class Indent {
  int size = 0;

  void push() {
    size += 2;
  }  

  void pop() {
    size -= 2;
  }

  @Override
  public String toString() {
    return new String(new char[size]).replace("\0", " ");
  }
}
