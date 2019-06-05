package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

//TODO Class is unfinished

@RestController
@RequestMapping(value = "/shows")
@Api(value = "shows")
public class ShowEndpoint {

    private final ShowService showService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    ShowEndpoint(ShowService showService){
        this.showService = showService;
    }

    // OK
    @RequestMapping(value = "/event", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all shows filtered by eventName", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFilteredByEventName(@RequestParam(value = "eventName") String eventName){
        LOGGER.info("Show Endpoint: Get all shows which belong to event \"" + eventName + " \"");
        try{
            // TODO
            //return showService.findAllShowsFilteredByEventName(eventName);
            return showService.findAll(); // replace this line
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows belonging to event with name " + eventName, e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows belonging to event with name " + eventName, e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows belonging to event with name " + eventName + "were found: " + e.getMessage(), e);
        }
    }

    // OK
    @RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all shows filtered by location id", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFilteredByLocationID(@PathVariable("id") Integer locationID){
        LOGGER.info("Show Endpoint:  Get all shows filtered by location with id " + locationID);
        try{
            // TODO
            //return showMapper.showToShowDTO(showService.findAllShowsFilteredByLocationID(locationID));
            return showService.findAll(); // replace this line
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows for that location", e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows for that location", e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows for that location were found:" + e.getMessage(), e);
        }
    }


    // OK
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ApiOperation(value = "Get all shows filtered by specified attributes", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findShowsFilteredByShowAttributes(
                                                                                                                                        @RequestParam(value = "eventId", required = false) Long eventId,
                                                                                                                                        @RequestParam(value = "eventName", required = false) String eventName,
                                                                                                                                        @RequestParam(value = "hallName", required = false) String hallName,
                                                                                                                                        @RequestParam(value="minPrice", required = false) Integer minPrice,
                                                                                                                                        @RequestParam(value="maxPrice", required = false) Integer maxPrice,
                                                                                                                                        @RequestParam(value="dateFrom", required = false) String dateFrom,
                                                                                                                                        @RequestParam(value="dateTo", required = false) String dateTo,
                                                                                                                                        @RequestParam(value="timeFrom", required = false) String timeFrom,
                                                                                                                                        @RequestParam(value="timeTo", required = false) String timeTo,
                                                                                                                                        @RequestParam(value="duration", required = false) Integer duration,
                                                                                                                                        @RequestParam(value = "locationName", required = false) String locationName,
                                                                                                                                        @RequestParam(value = "country", required = false) String country,
                                                                                                                                        @RequestParam(value = "city", required = false) String city,
                                                                                                                                        @RequestParam(value = "postalCode", required = false) String postalCode,
                                                                                                                                        @RequestParam(value = "street", required = false) String street,
                                                                                                                                        @RequestParam(value = "houseNr", required = false) Integer houseNr)
    {
        boolean filterData = (
            dateFrom == null && dateTo == null &&
                timeFrom == null && timeTo == null &&
                minPrice == null && maxPrice == null &&
                eventName == null && hallName == null &&
                duration == null && locationName == null &&
                country == null && city == null &&
                postalCode == null && street == null &&
                eventId == null && houseNr == null);
        try {
            LOGGER.debug("eventName: " + eventName + "\nhallName: " + hallName + "\nminPrice: " + minPrice + "\nmaxPrice: " + maxPrice +
                "\ndateFrom: " + dateFrom + "\ndateTo: " + dateTo+ "\ntimeFrom: " + timeFrom
                + "\ntimeTo: " + timeTo + "\nduration: " +duration);

            if (filterData) {
                LOGGER.info("Get all shows");
                return showService.findAllShows();
            } else {

                ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder()
                    .eventId(eventId)
                    .priceInEuroFrom(minPrice)
                    .priceInEuroTo(maxPrice)
                    .eventName(eventName)
                    .hallName(hallName)
                    .dateFrom(dateFrom == null ? null : LocalDate.parse(dateFrom, dateFormatter))
                    .dateTo(dateTo == null ? null : LocalDate.parse(dateTo, dateFormatter))
                    .timeFrom(timeFrom == null ? null : LocalTime.parse(timeFrom, timeFormatter))
                    .timeTo(timeTo == null ? null : LocalTime.parse(timeTo, timeFormatter))
                    .durationInMinutes(duration)
                    .locationName(locationName)
                    .country(country)
                    .city(city)
                    .street(street)
                    .houseNr(houseNr)
                    .postalcode(postalCode)
                    .build();

                LOGGER.info("Get all shows filtered by specified attributes: " + parameters.toString());

                List<ShowDTO> shows = showService.findAllShowsFiltered(parameters);

                return shows;
            }
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows with those parameters: " + e.getMessage(), e);
        }catch (at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows with those parameters", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows are found for the given parameters:" + e.getMessage(), e);
        }
    }
}
/*
    @RequestMapping(value = "/location", method = RequestMethod.GET)
    @ApiOperation(value = "Get all shows filtered by location parameters", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFilteredByLocation(
        @RequestParam(value="country", required = false) String country,
        @RequestParam(value="city", required = false)  String city,
        @RequestParam(value="postalcode", required = false)  String postalcode,
        @RequestParam(value="street", required = false)  String street
    ){
        boolean filterData = country == null && city == null && postalcode == null && street == null;
        try{
            if (filterData) {
                throw new IllegalArgumentException("No parameters are specified.");
            } else {
                LOGGER.info("Get all shows filtered by specified attributes");
                return showMapper.showToShowDTO(showService.findAllShowsFilteredByLocation(country, city, postalcode, street));
            }
                dateFrom == null && dateTo == null &&
                timeFrom == null && timeTo == null &&
                minPrice == null && maxPrice == null &&
                eventName == null && hallName == null &&
                duration == null
        );
        try{
        if (filterData) {
            LOGGER.info("Show Endpoint: Get all shows");
            // TODO
            return showService.findAll();
        } else {
            LOGGER.info("Show Endpoint: Get all shows filtered by specified attributes");
            // TODO return showService.findShowsFilteredByShowAttributes(dateFrom, dateTo, timeFrom, timeTo, minPrice, maxPrice, eventName, hallName));
            //  Example for parsing String to Date or Time
            //  String sDate1 = "31/12/1998";
            //  Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            return showService.findAll(); // replace this line
        }
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows with those parameters: " + e.getMessage(), e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows with those parameters", e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows are found for the given parameters:" + e.getMessage(), e);
        }
    }
}

 */
