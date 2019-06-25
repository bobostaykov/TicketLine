package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    /**
     * Maps the CustomerDTO object to Customer object
     * @param customerDTO to map
     * @return the mapped Customer object
     */
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    /**
     * Maps the Customer object to CustomerDTO object
     * @param customer to map
     * @return the mapped CustomerDTO object
     */
    CustomerDTO customerToCustomerDTO(Customer customer);

}
