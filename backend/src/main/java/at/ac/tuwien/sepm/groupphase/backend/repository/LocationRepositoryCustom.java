package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Location;

import java.util.List;

public interface LocationRepositoryCustom {

    List<Location> findLocationsFiltered(String country, String city, String street, String postalCode, String description);
}
