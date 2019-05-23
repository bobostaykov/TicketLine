package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public List<HallDTO> getHalls(){
        LOGGER.info("GET Halls: all Halls");
        return hallService.findAllHalls();
    }

    //TODO: add authorization
    @PostMapping
    public HallDTO postHall(@RequestBody HallDTO hallDto) {
        LOGGER.info("POST Halls: " + hallDto.toString());
        try {
            return hallService.addHall(hallDto);
        } catch (ServiceException e) {
            LOGGER.error("Hall " + hallDto.toString() + " could not be added to the system");
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Hall " + hallDto.toString() + " could not be added to the system",
                e
            );
        }
    }
}
