package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(value = "/artists")
@Api(value = "artists")
public class ArtistEndpoint {

    private final ArtistService artistService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public ArtistEndpoint(ArtistService artistService) {
        this.artistService = artistService;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get artists with 'artistName' as part of their name", authorizations = {@Authorization(value = "apiKey")})
    public Page<ArtistDTO> findArtistsByName(@RequestParam(value = "artist_name") String artistName,
                                             @RequestParam(value = "page", required = false) Integer page,
                                             @RequestParam(value = "pageSize", required = false) @Positive Integer pageSize) {
        LOGGER.info("ArtistEndpoint: findArtistsByName");
        return artistService.findArtistsByName(artistName, page, pageSize);
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Add an artist by id", authorizations = {@Authorization(value = "apiKey")})
    public ArtistDTO updateArtist(@Valid @RequestBody ArtistDTO artistDTO) {
        LOGGER.info("ArtistEndpoint: Add an artist " + artistDTO.toString());
        try {
            return artistService.addArtist(artistDTO);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during adding a artist: " + e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error when reading artist: " + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Update an artist by id", authorizations = {@Authorization(value = "apiKey")})
    public ArtistDTO updateArtist(@Valid @RequestBody ArtistDTO artistDTO, @PathVariable("id") Long id) {
        LOGGER.info("ArtistEndpoint: updateArtist");
        artistDTO.setId(id);
        try {
            return artistService.updateArtist(artistDTO);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error during adding a artist: " + e.getMessage(), e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error when reading artist: " + e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Delete an artist by id", authorizations = {@Authorization(value = "apiKey")})
    public void deleteById(@PathVariable Long id) {
        LOGGER.info("ArtistEndpoint: deleteById " + id);
        try {
            artistService.deleteById(id);
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

}
