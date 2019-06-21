package at.ac.tuwien.sepm.groupphase.backend.unit.persistance;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LocationRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class LocationRepositoryTest {

    private Location location1 = Location.builder().id(1L).locationName("Burgtheater").country("Austria").city("Vienna").street("NoStreet 100").postalCode("2000").description("Description").build();
    private Location location2 = Location.builder().id(2L).locationName("Royal Palace").country("Bulgaria").city("Sofia").street("DummyStreet 5").postalCode("5555").description("Oww").build();
    private Location location3 = Location.builder().id(3L).locationName("Holy Place").country("Austria").city("Graz").street("BrodaFromAnathaMada 0").postalCode("7777").build();
    private Page<Location> locationPage;

    private boolean init = false;

    @Autowired
    private LocationRepository locationRepository;

    @Before
    public void beforeEachTest() {
        if (!init) {
            location1 = locationRepository.save(location1);
            location2 = locationRepository.save(location2);
            location3 = locationRepository.save(location3);
            init = true;
        }
    }

    @Test
    public void searchByMultipleParameters() {
        locationPage = locationRepository.findLocationsFiltered(null,"a", null, "street", null, null, PageRequest.of(0,10));
        Assert.assertEquals(2, locationPage.getContent().size());
        Assert.assertEquals(1, locationPage.getTotalPages());
        Assert.assertTrue(locationPage.getContent().contains(location1));
        Assert.assertTrue(locationPage.getContent().contains(location2));
    }

    @Test(expected = NotFoundException.class)
    public void searchForNonExistingLocation_ThrowsNotFoundException() throws NotFoundException {
        locationRepository.findLocationsFiltered(null,"NOT FOUND", null, null, null, null, PageRequest.of(0,10));
    }

    @Test
    public void searchByCountry() {
        locationPage = locationRepository.findLocationsFiltered(null,"austria", null, null, null, null, PageRequest.of(0,10));
        Assert.assertEquals(2, locationPage.getContent().size());
        Assert.assertEquals(1, locationPage.getTotalPages());
        Assert.assertTrue(locationPage.getContent().contains(location1));
        Assert.assertTrue(locationPage.getContent().contains(location3));
    }

    @Test
    public void searchByCity() {
        locationPage = locationRepository.findLocationsFiltered(null,null, "i", null, null, null, PageRequest.of(0,10));
        Assert.assertEquals(2, locationPage.getContent().size());
        Assert.assertEquals(1, locationPage.getTotalPages());
        Assert.assertTrue(locationPage.getContent().contains(location1));
        Assert.assertTrue(locationPage.getContent().contains(location2));
    }

    @Test
    public void searchByStreet() {
        locationPage = locationRepository.findLocationsFiltered(null,null, null, "0", null, null, PageRequest.of(0,10));
        Assert.assertEquals(2, locationPage.getContent().size());
        Assert.assertEquals(1, locationPage.getTotalPages());
        Assert.assertTrue(locationPage.getContent().contains(location1));
        Assert.assertTrue(locationPage.getContent().contains(location3));
    }

    @Test
    public void searchByPostalCode() {
        locationPage = locationRepository.findLocationsFiltered(null,null, null, null, "5555", null, PageRequest.of(0,10));
        Assert.assertEquals(1, locationPage.getContent().size());
        Assert.assertEquals(1, locationPage.getTotalPages());
        Assert.assertTrue(locationPage.getContent().contains(location2));
    }

    @Test
    public void searchByDescription() {
        locationPage = locationRepository.findLocationsFiltered(null,null, null, null, null, "oww", PageRequest.of(0,10));
        Assert.assertEquals(1, locationPage.getContent().size());
        Assert.assertEquals(1, locationPage.getTotalPages());
        Assert.assertTrue(locationPage.getContent().contains(location2));
    }

    @Test
    public void getCountriesOrderedByName() {
        List<String> countries = locationRepository.getCountriesOrderedByName();
        Assert.assertTrue(countries.contains("Austria"));
        Assert.assertTrue(countries.contains("Bulgaria"));
        Assert.assertEquals(2, countries.size());
    }
}
