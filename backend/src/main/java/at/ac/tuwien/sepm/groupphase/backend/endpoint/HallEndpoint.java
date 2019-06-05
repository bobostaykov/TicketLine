package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping
    @ApiOperation(value = "Get all saved halls", authorizations = {@Authorization(value = "apiKey")})
    public List<HallDTO> getHalls(){
        LOGGER.info("GET Halls: all Halls");
        return hallService.findAllHalls();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Add a new hall with seats or sectors", authorizations = {@Authorization(value = "apiKey")})
    public HallDTO postHall(@RequestBody HallDTO hallDto) throws ServiceException, CustomValidationException {
        LOGGER.info("POST Halls: " + hallDto.toString());
        return hallService.addHall(hallDto);


    }
}
