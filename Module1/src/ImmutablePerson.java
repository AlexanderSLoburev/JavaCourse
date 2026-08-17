import java.util.Objects;

final class ImmutablePerson {
  private final String name;
  private final Address address;

  public ImmutablePerson(String name, Address address) {
    this.name = Objects.requireNonNull(name, "Name cannot be null");
    Objects.requireNonNull(address, "Address cannot be null");
    this.address = new Address(address);
  }

  public String getName() { return name; }

  public Address getAddress() { return new Address(address); }

  public static void main(String[] args) {
    Address mutableAddress = new Address("Moscow", "Tverskaya");
    ImmutablePerson person = new ImmutablePerson("Ivan", mutableAddress);

    mutableAddress.setCity("Sankt Petersburg");
    System.out.println(person.getAddress().getCity());

    Address retrieved = person.getAddress();
    retrieved.setStreet("Nevsky");
    System.out.println(person.getAddress().getStreet());
  }
}