import java.util.Objects;

class Address {
  private String city;
  private String street;

  public Address(String city, String street) {
    this.city = Objects.requireNonNull(city, "City cannot be null");
    this.street = Objects.requireNonNull(street, "Street cannot be null");
  }

  public Address(Address other) {
    Objects.requireNonNull(other, "Address to copy cannot be null");
    this.city = other.city;
    this.street = other.street;
  }

  public void setCity(String city) {
    this.city = Objects.requireNonNull(city, "City cannot be null");
  }

  public void setStreet(String street) {
    this.street = Objects.requireNonNull(street, "Street cannot be null");
  }

  public String getCity() { return city; }

  public String getStreet() { return street; }
}