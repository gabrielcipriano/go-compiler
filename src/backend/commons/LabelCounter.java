package backend.commons;

public class LabelCounter {
  private String name;
  private Counter counter = new Counter();

  public LabelCounter(String name) {
    this.name = name;
  }

  public String next() {
    return "$" + this.name + this.counter.next();
  }

  public void reset() {
    this.counter.reset();
  }
}
