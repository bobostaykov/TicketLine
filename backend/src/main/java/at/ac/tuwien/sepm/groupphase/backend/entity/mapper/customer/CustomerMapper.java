package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.customer;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.customer.CustomerDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);

}
