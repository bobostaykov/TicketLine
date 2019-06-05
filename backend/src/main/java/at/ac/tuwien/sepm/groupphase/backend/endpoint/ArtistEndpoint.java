package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/artists")
@Api(value = "artists")
public class ArtistEndpoint {

    private final ArtistService artistService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public ArtistEndpoint(ArtistService artistService) {
        this.artistService = artistService;
    }

    // OK
    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get artists with 'artistName' as part of their name", authorizations = {@Authorization(value = "apiKey")})
    public List<ArtistDTO> findArtistsByName(@RequestParam(value = "artist_name") String artistName) {
        LOGGER.info("findArtistsByName() called");
        return artistService.findArtistsByName(artistName);
    }

}
