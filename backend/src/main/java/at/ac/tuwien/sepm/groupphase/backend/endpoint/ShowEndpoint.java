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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//TODO Class is unfinished

@RestController
@RequestMapping(value = "/shows")
@Api(value = "shows")
public class ShowEndpoint {

    private final ShowService showService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowEndpoint.class);

    ShowEndpoint(ShowService showService){
        this.showService = showService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of shows for a clicked event/location", authorizations = {@Authorization(value = "apiKey")})
    public void manager(@RequestParam(value = "results_for") String results_for, @RequestParam(value = "name_or_id") String name_or_id){
        LOGGER.info("Manager");
        switch(results_for) {
            case "Location":
                // check parameter name_or_id if Integer
                try {
                    Integer locationID = Integer.parseInt(name_or_id);
                  //  findAllShowsFilteredByLocationID(locationID);
                } catch(NumberFormatException e){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location id is invalid");
                }
                break;
            case "Event":
                findAllShowsFilteredByEventName(name_or_id);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No results for this type");
        }
    }

    @ApiOperation(value = "Get list of all shows filtered by eventName", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFilteredByEventName(@RequestParam(value = "eventName", required = false) String eventName){
        LOGGER.info("Get all shows which belong to event with id " + eventName);
        try{
            return showService.findAllShowsFilteredByEventName(eventName);
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows belonging to event with name " + eventName, e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows belonging to event with name " + eventName, e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows belonging to event with name " + eventName + "were found: " + e.getMessage(), e);
        }
    }
/*
    @ApiOperation(value = "Get list of all shows filtered by location id", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFilteredByLocationID(@PathVariable("id") Integer locationID){
        LOGGER.info("Get all shows filtered by location id");
        try{
            return showMapper.showToShowDTO(showService.findAllByLocationID(locationID));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows for that location", e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows for that location", e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows for that location were found:" + e.getMessage(), e);
        }
    }
*/
    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ApiOperation(value = "Get all shows filtered by specified attributes", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFiltered(
        @RequestParam(value="dateFrom", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate dateFrom,
        @RequestParam(value="dateTo", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDate dateTo,
        @RequestParam(value="timeFrom", required = false) @DateTimeFormat(pattern="HHmm") LocalTime timeFrom,
        @RequestParam(value="timeTo", required = false) @DateTimeFormat(pattern="HHmm") LocalTime timeTo,
        @RequestParam(value="priceInEuroFrom", required = false) Integer priceInEuroFrom,
        @RequestParam(value="priceInEuroTo", required = false) Integer priceInEuroTo,
        @RequestParam(value = "eventName", required = false) String eventName,
        @RequestParam(value = "hallName", required = false) String hallName
    ){
        boolean filterData = (
            dateFrom == null && dateTo == null &&
            timeFrom == null && timeTo == null &&
            priceInEuroFrom == null && priceInEuroTo == null &&
            eventName == null && hallName == null);
        try{
        /*if (filterData) {
            LOGGER.info("Get all shows");
            return showService.findAllShowsFiltered(parameters);
        } else {*/
            LOGGER.info("Get all shows filtered by specified attributes");
            ShowSearchParametersDTO parameters = new ShowSearchParametersDTO(
                dateFrom, dateTo, timeFrom, timeTo, priceInEuroFrom, priceInEuroTo, eventName, hallName);
            return showService.findAllShowsFiltered(parameters);
        //}
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows with those parameters: " + e.getMessage(), e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows with those parameters", e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows are found for the given parameters:" + e.getMessage(), e);
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
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows with those parameters: " + e.getMessage(), e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows with those parameters", e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows are found for the given parameters:" + e.getMessage(), e);
        }
    }
*/
}
