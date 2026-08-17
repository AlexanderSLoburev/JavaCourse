// Student.java
import java.util.List;

public record Student(String name, List<Book> books) {
  public Student {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException(
          "Student name cannot be null or blank");
    }
    if (books == null || books.size() < 5) {
      throw new IllegalArgumentException(
          "Student must have at least 5 books, got " +
          (books == null ? 0 : books.size()));
    }
    // Protective copy to preserve invariant (student must have at least 5
    // books) after object creation
    books = List.copyOf(books);
  }

  @Override
  public String toString() {
    return "Student{"
        + "name='" + name + '\'' + ", books=" + books + '}';
  }
}