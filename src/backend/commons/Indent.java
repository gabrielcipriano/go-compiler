package backend.commons;

public class Indent {
  int size = 0;

  public void rightShift() {
    size += 2;
  }  

  public void leftShift() {
    size -= 2;
  }

  @Override
  public String toString() {
    return new String(new char[size]).replace("\0", " ");
  }
}
