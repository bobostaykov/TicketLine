package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.show.ShowMapper;
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

import java.time.LocalDateTime;
import java.util.List;

//TODO differentiate between the GET Methods, what are the api-s

@RestController
@RequestMapping(value = "/shows")
@Api(value = "shows")
public class ShowEndpoint {

    private final ShowService showService;
    private final ShowMapper showMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowEndpoint.class);

    ShowEndpoint(ShowService showService, ShowMapper showMapper){
        this.showService = showService;
        this.showMapper = showMapper;
    }

    @RequestMapping(value = "/event{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all shows filtered by eventID", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllByEventID(@PathVariable("id") Integer eventID){
        LOGGER.info("Get all shows which belong to event with id " + eventID);
        try{
            return showMapper.showToShowDTO(showService.findAllByEventID(eventID));
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows belonging to event with id " + eventID, e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows belonging to event with id " + eventID, e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows belonging to event with id " + eventID + "were found: " + e.getMessage(), e);
        }
    }
/*
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all shows filtered by location id", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllByLocationID(@PathVariable("id") Integer locationID){
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

    @RequestMapping(value = "/filter", method = RequestMethod.GET)
    @ApiOperation(value = "Get all shows filtered by specified attributes", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> findAllShowsFiltered(
        @RequestParam(value="dateFrom", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDateTime dateFrom,
        @RequestParam(value="dateTo", required = false) @DateTimeFormat(iso= DateTimeFormat.ISO.DATE) LocalDateTime dateTo,
        @RequestParam(value="timeFrom", required = false) @DateTimeFormat(pattern="HHmm") LocalDateTime timeFrom,
        @RequestParam(value="timeTo", required = false) @DateTimeFormat(pattern="HHmm") LocalDateTime timeTo,
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
        if (filterData) {
            LOGGER.info("Get all shows");
            return showMapper.showToShowDTO(showService.findAll());
        } else {
            LOGGER.info("Get all shows filtered by specified attributes");
            return showMapper.showToShowDTO(showService.findAllShowsFiltered(dateFrom, dateTo, timeFrom, timeTo, priceInEuroFrom, priceInEuroTo, eventName, hallName));
        }
        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows with those parameters: " + e.getMessage(), e);
        }catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows with those parameters", e);
        }catch(NotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows are found for the given parameters:" + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/bylocation", method = RequestMethod.GET)
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
