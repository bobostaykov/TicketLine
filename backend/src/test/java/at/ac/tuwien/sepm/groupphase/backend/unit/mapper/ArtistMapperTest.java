package at.ac.tuwien.sepm.groupphase.backend.unit.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.artist.ArtistDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.artist.ArtistMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ArtistMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.sepm.groupphase.backend.entity.mapper")
    public static class ArtistMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private ArtistMapper artistMapper;

    private static final Long ID = 1L;
    private static final String NAME = "Name";

    @Test
    public void shouldMapArtistToArtistDTO(){
        Artist artist = Artist.builder()
        .id(ID)
        .name(NAME)
        .build();

        ArtistDTO artistDTO = artistMapper.artistToArtistDTO(artist);
        assertThat(artistDTO).isNotNull();
        assertThat(artistDTO.getId()).isEqualTo(ID);
        assertThat(artistDTO.getName()).isEqualTo(NAME);
    }

    @Test
    public void shouldMapArtistDTOToArtist(){
        ArtistDTO artistDTO = ArtistDTO.builder()
            .id(ID)
            .name(NAME)
            .build();

        Artist artist = artistMapper.artistDTOToArtist(artistDTO);
        assertThat(artist).isNotNull();
        assertThat(artist.getId()).isEqualTo(ID);
        assertThat(artist.getName()).isEqualTo(NAME);
    }

}
