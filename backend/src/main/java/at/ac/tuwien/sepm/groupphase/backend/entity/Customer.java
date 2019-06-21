package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_seq", initialValue = 1, allocationSize = 50)
    @GeneratedValue(generator = "customer_seq")
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "firstname")
    private String firstname;

    @Column(nullable = false, name = "email")
    private String email;

    @Column(nullable = false, name = "birthday")
    private LocalDate birthday;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    @Override
    public String toString() {
        String customer  = "Customer{" +
            "id=" + id +
            ", username='" + name + '\'' +
            ", firstname=" + firstname +
            ", e-mail=" + email;
        if (birthday != null)
            customer += ", birthday=" + birthday.toString();
        customer += '}';
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (id != null ? !id.equals(customer.id) : customer.id != null) return false;
        if (name != null ? !name.equals(customer.name) : customer.name != null) return false;
        if (firstname != null ? !firstname.equals(customer.firstname) : customer.firstname != null) return false;
        if (email != null ? !email.equals(customer.email) : customer.email != null) return false;
        return birthday != null ? birthday.equals(customer.birthday) : customer.birthday == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        return result;
    }

    public static final class CustomerBuilder {
        private Long id;
        private String name;
        private String firstname;
        private String email;
        private LocalDate birthday;

        private CustomerBuilder() {
        }

        public Customer.CustomerBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public Customer.CustomerBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Customer.CustomerBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Customer.CustomerBuilder email(String email) {
            this.email = email;
            return this;
        }

        public Customer.CustomerBuilder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.setId(id);
            customer.setName(name);
            customer.setFirstname(firstname);
            customer.setEmail(email);
            customer.setBirthday(birthday);
            return customer;
        }
    }
}
