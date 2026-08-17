// Book.java
public record Book(String title, String author, int year, int pages) {
  public Book {
    if (title == null || title.isBlank()) {
      throw new IllegalArgumentException("Title cannot be null or blank");
    }
    if (author == null || author.isBlank()) {
      throw new IllegalArgumentException("Author cannot be null or blank");
    }
    if (year < 0) {
      throw new IllegalArgumentException("Year cannot be negative");
    }
    if (pages <= 0) {
      throw new IllegalArgumentException("Pages must be positive");
    }
  }
}