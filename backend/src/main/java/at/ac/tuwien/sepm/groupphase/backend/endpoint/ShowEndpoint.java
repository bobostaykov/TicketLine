package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.conversion.CaseInsensitiveEnumConverter;
import at.ac.tuwien.sepm.groupphase.backend.datatype.EventType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.ShowRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.ShowSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.show.ShowDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ShowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(value = "/shows")
@Api(value = "shows")
public class ShowEndpoint {

    private final ShowService showService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    ShowEndpoint(ShowService showService) {
        this.showService = showService;
    }

    // needed to correctly deserialize request parameter enums
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ShowRequestParameter.class, new CaseInsensitiveEnumConverter<>(ShowRequestParameter.class));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get one show by its id", authorizations = {@Authorization(value = "apiKey")})
    public ShowDTO findOneById(@PathVariable("id") Long id,
                               @RequestParam(value = "include", required = false) List<ShowRequestParameter> include) {
        LOGGER.info("Show Endpoint: Find one show by id " + id);
        return showService.findOneById(id, include);
    }

    @RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of all shows filtered by location id", authorizations = {@Authorization(value = "apiKey")})
    public Page<ShowDTO> findAllShowsFilteredByLocationID(@PathVariable("id") Long locationID,
                                                          @RequestParam(value = "page", required = false) Integer page,
                                                          @RequestParam(value = "pageSize", required = false) @Positive Integer pageSize){
        LOGGER.info("Show Endpoint:  Get all shows filtered by location with id " + locationID);
        try{
            return showService.findAllShowsFilteredByLocationID(locationID, page, pageSize);
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
    public Page<ShowDTO> findShowsFilteredByShowAttributes(@RequestParam(value = "eventId", required = false) Long eventId,
                                                           @RequestParam(value = "artistName", required = false) String artistName,
                                                           @RequestParam(value = "eventName", required = false) String eventName,
                                                           @RequestParam(value = "eventType", required = false) String eventTypeString,
                                                           @RequestParam(value = "hallName", required = false) String hallName,
                                                           @RequestParam(value="minPrice", required = false)@PositiveOrZero Integer minPrice,
                                                           @RequestParam(value="maxPrice", required = false)@PositiveOrZero Integer maxPrice,
                                                           @RequestParam(value="dateFrom", required = false) String dateFrom,
                                                           @RequestParam(value="dateTo", required = false) String dateTo,
                                                           @RequestParam(value="timeFrom", required = false) String timeFrom,
                                                           @RequestParam(value="timeTo", required = false) String timeTo,
                                                           @RequestParam(value="duration", required = false)@Positive Integer duration,
                                                           @RequestParam(value = "locationName", required = false) String locationName,
                                                           @RequestParam(value = "country", required = false) String country,
                                                           @RequestParam(value = "city", required = false) String city,
                                                           @RequestParam(value = "postalCode", required = false) String postalCode,
                                                           @RequestParam(value = "street", required = false) String street,
                                                           @RequestParam(value = "page", required = false) Integer page,
                                                           @RequestParam(value = "pageSize", required = false) @Positive Integer pageSize)
    {

        EventType eventType = null;
        for(EventType type : EventType.values()){
            if(type.toString().equals(eventTypeString)){
                eventType = type;
            }
        }
        try {
            LOGGER.debug("\neventName: " + eventName + "\nhallName: " + hallName + "\nminPrice: " + minPrice + "\nmaxPrice: " + maxPrice +
                "\ndateFrom: " + dateFrom + "\ndateTo: " + dateTo + "\ntimeFrom: " + timeFrom + "\ntimeTo: " + timeTo + "\nduration: " + duration +
                "\ncountry: " + country + "\ncity: " + city + "\nstreet: " + street + "\npostalCode: " + postalCode + "\npage: " + page);

                ShowSearchParametersDTO parameters = new ShowSearchParametersDTO.builder()
                    .priceInEuroFrom(minPrice)
                    .priceInEuroTo(maxPrice)
                    .eventName(eventName)
                    .hallName(hallName)
                    .dateFrom(dateFrom == null ? null : LocalDate.parse(dateFrom, dateFormatter))
                    .dateTo(dateTo == null ? null : LocalDate.parse(dateTo, dateFormatter))
                    .timeFrom(timeFrom == null ? null : LocalTime.parse(timeFrom, timeFormatter))
                    .timeTo(timeTo == null ? null : LocalTime.parse(timeTo, timeFormatter))
                    .durationInMinutes(duration)
                    .country(country)
                    .city(city)
                    .street(street)
                    .locationName(locationName)
                    .eventId(eventId)
                    .postalcode(postalCode)
                    .artistName(artistName)
                    .eventType(eventType)
                    .build();

                LOGGER.info("Get all shows filtered by specified attributes: " + parameters.toString());
                return showService.findAllShowsFiltered(parameters, page, pageSize);

        }catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while looking for shows with those parameters: " + e.getMessage(), e);
        }catch (at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while looking for shows with those parameters", e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No shows are found for the given parameters:" + e.getMessage(), e);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/suggestions")
    @ApiOperation(value = "Get a list of search suggestions for shows filtered by event name, date and time", authorizations = {@Authorization(value = "apiKey")})
    public List<ShowDTO> getSearchSuggestions(@RequestParam(required = false) String eventName,
                                              @RequestParam(required = false) String date,
                                              @RequestParam(required = false) String time) {
        LOGGER.info("GET search suggestions for show with parameters eventName = " + eventName +
            ", date = " + date + ", time = " + time);
        return showService.findSearchResultSuggestions(eventName, date, time);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Add a show by id", authorizations = {@Authorization(value = "apiKey")})
    public ShowDTO updateShow(@RequestBody ShowDTO showDTO) {
        LOGGER.info("Add a show " + showDTO.toString());
        try {
            return showService.addShow(showDTO);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during adding a show: " + e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error when reading show: " + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update a show by id", authorizations = {@Authorization(value = "apiKey")})
    public ShowDTO updateShow(@RequestBody ShowDTO showDTO, @PathVariable("id") Long id) {
        LOGGER.info("Update show " + showDTO.toString());
        showDTO.setId(id);
        try {
            return showService.updateShow(showDTO);
        } catch (IllegalArgumentException | ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during updating show: " + e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error when reading show: " + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete a show by id", authorizations = {@Authorization(value = "apiKey")})
    public void deleteById(@PathVariable Long id) {
        LOGGER.info("ShowEndpoint: deleteById " + id);
        try {
            showService.deleteById(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
