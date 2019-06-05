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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class LocationRepositoryTest {

    private List<Location> locationList = new ArrayList<>();
    private Location location1 = Location.builder().id(1L).country("Austria").city("Vienna").street("NoStreet 100").postalCode("2000").description("Description").build();
    private Location location2 = Location.builder().id(2L).country("Bulgaria").city("Sofia").street("DummyStreet 5").postalCode("5555").description("Oww").build();
    private Location location3 = Location.builder().id(3L).country("Austria").city("Graz").street("BrodaFromAnathaMada 0").postalCode("7777").build();
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
        locationList = locationRepository.findLocationsFiltered("a", null, "street", null, null);
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(location1));
        Assert.assertTrue(locationList.contains(location2));
    }

    @Test(expected = NotFoundException.class)
    public void searchForNonExistingLocation_ThrowsNotFoundException() throws NotFoundException {
        locationRepository.findLocationsFiltered("NOT FOUND", null, null, null, null);
    }

    @Test
    public void searchByCountry() {
        locationList = locationRepository.findLocationsFiltered("austria", null, null, null, null);
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(location1));
        Assert.assertTrue(locationList.contains(location3));
    }

    @Test
    public void searchByCity() {
        locationList = locationRepository.findLocationsFiltered(null, "i", null, null, null);
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(location1));
        Assert.assertTrue(locationList.contains(location2));
    }

    @Test
    public void searchByStreet() {
        locationList = locationRepository.findLocationsFiltered(null, null, "0", null, null);
        Assert.assertEquals(2, locationList.size());
        Assert.assertTrue(locationList.contains(location1));
        Assert.assertTrue(locationList.contains(location3));
    }

    @Test
    public void searchByPostalCode() {
        locationList = locationRepository.findLocationsFiltered(null, null, null, "5555", null);
        Assert.assertEquals(1, locationList.size());
        Assert.assertTrue(locationList.contains(location2));
    }

    @Test
    public void searchByDescription() {
        locationList = locationRepository.findLocationsFiltered(null, null, null, null, "oww");
        Assert.assertEquals(1, locationList.size());
        Assert.assertTrue(locationList.contains(location2));
    }

    @Test
    public void getCountriesOrderedByName() {
        List<String> countries = locationRepository.getCountriesOrderedByName();
        Assert.assertTrue(countries.contains("Austria"));
        Assert.assertTrue(countries.contains("Bulgaria"));
        Assert.assertEquals(2, countries.size());
    }
}
