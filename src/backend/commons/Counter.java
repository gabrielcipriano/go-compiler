package backend.commons;

public class Counter {
  private int n = 0;

  public int next() {
    return n++;
  }

  public void prev() {
    n--;
  }

  public int reset() {
    n = 0;
    return n;
  }

  public int size() {
    return n;
  }
}
