package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/artists")
@Api(value = "artists")
public class ArtistEndpoint {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    public ArtistEndpoint(ArtistService artistService, ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get artists with 'artistName' as part of their name", authorizations = {@Authorization(value = "apiKey")})
    public List<ArtistDTO> findArtistsByName(@RequestParam(value = "artist_name") String artistName) {
        List<ArtistDTO> toReturn = new ArrayList<>();
        for (Artist artist: artistService.findArtistsByName(artistName)) {
            toReturn.add(artistMapper.artistToArtistDTO(artist));
        }
        return toReturn;
    }

}
