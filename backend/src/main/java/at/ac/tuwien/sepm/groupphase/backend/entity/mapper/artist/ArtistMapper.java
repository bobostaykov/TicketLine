package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    Artist artistDTOToArtist(ArtistDTO artistDTO);

    ArtistDTO artistToArtistDTO(Artist artist);

    List<ArtistDTO> artistToArtistDTO(List<Artist> all);

}
