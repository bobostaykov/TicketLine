package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    /**
     * Maps ArtistDTO object to Artist
     * @param artistDTO to map
     * @return the mapped Artist object
     */
    Artist artistDTOToArtist(ArtistDTO artistDTO);

    /**
     * Maps Artist object to ArtistDTO
     * @param artist to map
     * @return the mapped ArtistDTO object
     */
    ArtistDTO artistToArtistDTO(Artist artist);
}
