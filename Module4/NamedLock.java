public class NamedLock {

  private final String name;

  public NamedLock(String name) { this.name = name; }

  @Override
  public String toString() {
    return name;
  }
}