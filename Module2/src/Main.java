// Main.java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
  public static void main(String[] args) {
    String filePath = "students.txt";
    List<Student> students;

    try (var lines = Files.lines(Paths.get(filePath))) {
      students = lines.filter(line -> line != null && !line.trim().isEmpty())
                     .map(Main::parseStudentLine)
                     .collect(Collectors.toList());
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
      return;
    } catch (IllegalArgumentException e) {
      System.err.println("Invalid data format: " + e.getMessage());
      return;
    } catch (Exception e) {
      System.out.print(e.getMessage());
    }

    students.stream()
        .peek(System.out::println)
        .flatMap(student -> student.books().stream())
        .sorted(Comparator.comparingInt(Book::pages))
        .distinct()
        .filter(book -> book.year() > 2000)
        .limit(3)
        .map(Book::year)
        .findFirst()
        .ifPresentOrElse(year
                         -> System.out.println("Год выпуска: " + year),
                         () -> System.out.println("Книга не найдена"));
  }

  private static Student parseStudentLine(String line) {
    String[] parts = line.split(";");
    if (parts.length < 2) {
      throw new IllegalArgumentException(
          "Invalid line format (expected at "
          + "least student name and one book): " + line);
    }
    String name = parts[0].trim();

    List<Book> books = Arrays.stream(parts, 1, parts.length)
                           .map(String::trim)
                           .map(Main::parseBook)
                           .collect(Collectors.toList());

    return new Student(name, books);
  }

  private static Book parseBook(String bookDescription) {
    String[] fields = bookDescription.split("\\|");
    if (fields.length != 4) {
      throw new IllegalArgumentException("Invalid book description format: " +
                                         bookDescription);
    }
    try {
      return new Book(fields[0].trim(), fields[1].trim(),
                      Integer.parseInt(fields[2].trim()),
                      Integer.parseInt(fields[3].trim()));
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException(
          "Invalid number in book description: " + bookDescription, e);
    }
  }
}