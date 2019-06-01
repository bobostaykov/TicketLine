package at.ac.tuwien.sepm.groupphase.backend.unit.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location.LocationMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class LocationMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.sepm.groupphase.backend.entity.mapper")
    public static class LocationMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private LocationMapper locationMapper;

    private static final Long ID = 1L;
    private static final String COUNTRY = "Austria";
    private static final String CITY = "Vienna";
    private static final String POSTAL_CODE = "1020";
    private static final String STREET = "Mumbo Jumbo Str. 69";
    private static final String DESCRIPTION = "Description";

    @Test
    public void shouldMapLocationToLocationDTO(){
        Location location = Location.builder()
            .id(ID)
            .country(COUNTRY)
            .city(CITY)
            .postalCode(POSTAL_CODE)
            .street(STREET)
            .description(DESCRIPTION)
            .build();

        LocationDTO locationDTO = locationMapper.locationToLocationDTO(location);
        assertThat(locationDTO).isNotNull();
        assertThat(locationDTO.getId()).isEqualTo(ID);
        assertThat(locationDTO.getCountry()).isEqualTo(COUNTRY);
        assertThat(locationDTO.getCity()).isEqualTo(CITY);
        assertThat(locationDTO.getPostalCode()).isEqualTo(POSTAL_CODE);
        assertThat(locationDTO.getStreet()).isEqualTo(STREET);
        assertThat(locationDTO.getDescription()).isEqualTo(DESCRIPTION);
    }

    @Test
    public void shouldMapLocationDTOToLocation(){
        LocationDTO locationDTO = LocationDTO.builder()
            .id(ID)
            .country(COUNTRY)
            .city(CITY)
            .postalCode(POSTAL_CODE)
            .street(STREET)
            .description(DESCRIPTION)
            .build();

        Location location = locationMapper.locationDTOToLocation(locationDTO);
        assertThat(location).isNotNull();
        assertThat(location.getId()).isEqualTo(ID);
        assertThat(location.getCountry()).isEqualTo(COUNTRY);
        assertThat(location.getCity()).isEqualTo(CITY);
        assertThat(location.getPostalCode()).isEqualTo(POSTAL_CODE);
        assertThat(location.getStreet()).isEqualTo(STREET);
        assertThat(location.getDescription()).isEqualTo(DESCRIPTION);

    }
}
