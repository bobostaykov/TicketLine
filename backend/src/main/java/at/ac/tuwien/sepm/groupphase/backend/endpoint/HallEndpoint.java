package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.conversion.CaseInsensitiveEnumConverter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.location.LocationDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.HallRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.HallSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(value = "/halls")
@Api(value = "halls")
public class HallEndpoint {

    private final HallService hallService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public HallEndpoint(HallService hallService) {
        this.hallService = hallService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.registerCustomEditor(HallRequestParameter.class, new CaseInsensitiveEnumConverter<>(HallRequestParameter.class));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get specific hall by its id", authorizations = {@Authorization(value = "apiKey")})
    public HallDTO getHallById(@Valid @Positive(message = "Hall id must be greater than zero!")
                                   @PathVariable("id") Long hallId,
                               @RequestParam(required = false) List<HallRequestParameter> include) {
        LOGGER.info("GET hall with id " + hallId);
        return hallService.findHallById(hallId, include);
    }


    @GetMapping
    @ApiOperation(value = "Get all saved halls", authorizations = {@Authorization(value = "apiKey")})
    public List<HallDTO> getHalls(@RequestParam(value = "fields", required = false) List<HallRequestParameter> fields,
                                  @RequestParam(required = false) String name,
                                  @RequestParam(required = false) LocationDTO location){
        LOGGER.info("Gets a list of halls with search parameters name = " + (name == null ? "all" : name) + " and location = "
            + (location == null ? "all" : location.toString()) + " with requested fields = " + (fields == null ? "all" : fields.toString()));
        HallSearchParametersDTO searchParametersDTO = HallSearchParametersDTO.builder()
            .name(name)
            .location(location)
            .build();
        return hallService.findHalls(fields, searchParametersDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new hall with seats or sectors", authorizations = {@Authorization(value = "apiKey")})
    public HallDTO postHall(@RequestBody @Valid HallDTO hallDto) throws ServiceException, CustomValidationException {
        LOGGER.info("POST Halls: " + hallDto.toString());
        return hallService.addHall(hallDto);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Updates an already existing hall and its seats or sectors", authorizations = {@Authorization(value = "apiKey")})
    public HallDTO updateHall(@RequestBody @Valid HallDTO hallDTO) throws CustomValidationException {
        LOGGER.info("UPDATE Hall with id " + hallDTO.getId() + " with parameters " + hallDTO.toString());
        return hallService.updateHall(hallDTO);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Deletes a hall and its seats or sectors by id", authorizations = {@Authorization(value = "apiKey")})
    public @ResponseBody void deleteHall(@Valid @Positive(message = "Hall id must be greater than zero!")
                                             @PathVariable Long id) {
        LOGGER.info("Delete request for hall with id " + id);
        hallService.deleteHall(id);
    }
}
