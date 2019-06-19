package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@ApiModel(value = "CustomerDTO", description = "A DTO for customer entries via rest")
public class CustomerDTO {
    @ApiModelProperty(name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(name = "The username of the customer")
    @NotBlank(message = "name may not be blank")
    private String name;

    @ApiModelProperty(name = "The firstname of the customer")
    @NotBlank(message = "firstname may not be blank")
    private String firstname;

    @ApiModelProperty(name = "The e-mail address of the customer")
    @Email(message = "email invalid")
    private String email;

    @ApiModelProperty(name = "The birthday of the customer")
    @PastOrPresent(message = "birthday may not be in the future")
    private LocalDate birthday;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public static CustomerDTOBuilder builder() {return new CustomerDTOBuilder();}

    @Override
    public String toString() {
        String customer  = "CustomerDTO{" +
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

        CustomerDTO customerDTO = (CustomerDTO) o;

        if (id != null ? !id.equals(customerDTO.id) : customerDTO.id != null) return false;
        if (name != null ? !name.equals(customerDTO.name) : customerDTO.name != null) return false;
        if (firstname != null ? !firstname.equals(customerDTO.firstname) : customerDTO.firstname != null) return false;
        if (birthday != null ? !birthday.equals(customerDTO.birthday) : customerDTO.birthday != null) return false;
        return email != null ? email.equals(customerDTO.email) : customerDTO.email == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    public static final class CustomerDTOBuilder {

        private Long id;
        private String name;
        private String firstname;
        private LocalDate birthday;
        private String email;

        public CustomerDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CustomerDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CustomerDTOBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public CustomerDTOBuilder email(String email) {
            this.email = email;
            return this;
        }

        public CustomerDTOBuilder birthday(LocalDate birthday) {
            this.birthday = birthday;
            return this;
        }

        public CustomerDTO build() {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setId(id);
            customerDTO.setName(name);
            customerDTO.setFirstname(firstname);
            customerDTO.setEmail(email);
            customerDTO.setBirthday(birthday);
            return customerDTO;
        }
    }
}
