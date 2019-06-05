package at.ac.tuwien.sepm.groupphase.backend.unit.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer.CustomerMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.sepm.groupphase.backend.entity.mapper")
    public static class CustomerMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private CustomerMapper customerMapper;

    private static final Long CUSTOMER_ID = 1L;
    private static final String CUSTOMER_NAME = "Müller";
    private static final String CUSTOMER_FIRSTNAME = "Petra";
    private static final String CUSTOMER_EMAIL = "petra.mueller@gmail.com";
    private static final LocalDate CUSTOMER_BIRTHDAY =
        LocalDate.of(1982,07,22);

    @Test
    public void shouldMapCustomerToCustomerDTO() {
        Customer customer = Customer.builder()
            .id(CUSTOMER_ID)
            .name(CUSTOMER_NAME)
            .firstname(CUSTOMER_FIRSTNAME)
            .email(CUSTOMER_EMAIL)
            .birthday(CUSTOMER_BIRTHDAY)
            .build();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        assertThat(customerDTO).isNotNull();
        assertThat(customerDTO.getId()).isEqualTo(1L);
        assertThat(customerDTO.getName()).isEqualTo(CUSTOMER_NAME);
        assertThat(customerDTO.getFirstname()).isEqualTo(CUSTOMER_FIRSTNAME);
        assertThat(customerDTO.getEmail()).isEqualTo(CUSTOMER_EMAIL);
        assertThat(customerDTO.getBirthday()).isEqualTo(CUSTOMER_BIRTHDAY);
    }

    @Test
    public void shouldMapCustomerDTOToCustomer() {
        CustomerDTO customerDTO = CustomerDTO.builder()
            .id(CUSTOMER_ID)
            .name(CUSTOMER_NAME)
            .firstname(CUSTOMER_FIRSTNAME)
            .email(CUSTOMER_EMAIL)
            .birthday(CUSTOMER_BIRTHDAY)
            .build();
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(1L);
        assertThat(customer.getName()).isEqualTo(CUSTOMER_NAME);
        assertThat(customer.getFirstname()).isEqualTo(CUSTOMER_FIRSTNAME);
        assertThat(customer.getEmail()).isEqualTo(CUSTOMER_EMAIL);
        assertThat(customer.getBirthday()).isEqualTo(CUSTOMER_BIRTHDAY);

    }
}
