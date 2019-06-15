package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.conversion.CaseInsensitiveEnumConverter;
import at.ac.tuwien.sepm.groupphase.backend.datatype.HallRequestParameters;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
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
import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

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
        binder.registerCustomEditor(HallRequestParameters.class, new CaseInsensitiveEnumConverter<>(HallRequestParameters.class));
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get specific hall by its id", authorizations = {@Authorization(value = "apiKey")})
    public HallDTO getHallById(@PathVariable("id") Long hallId) {
        LOGGER.info("GET hall with id " + hallId);
        return hallService.findHallById(hallId);
    }

    @GetMapping
    @ApiOperation(value = "Get all saved halls", authorizations = {@Authorization(value = "apiKey")})
    public List<HallDTO> getHalls(@RequestParam(value = "fields", required = false) List<HallRequestParameters> fields){
        LOGGER.info("GET Halls: all Halls with " + (isEmpty(fields) ? "all parameters" : "parameters " +  fields.toString()));
        return hallService.findAllHalls(fields);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new hall with seats or sectors", authorizations = {@Authorization(value = "apiKey")})
    public HallDTO postHall(@RequestBody @Valid HallDTO hallDto) throws ServiceException, CustomValidationException {
        LOGGER.info("POST Halls: " + hallDto.toString());
        return hallService.addHall(hallDto);


    }
}
