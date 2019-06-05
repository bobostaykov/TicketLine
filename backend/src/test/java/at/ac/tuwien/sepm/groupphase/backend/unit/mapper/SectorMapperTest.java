package at.ac.tuwien.sepm.groupphase.backend.unit.mapper;

import at.ac.tuwien.sepm.groupphase.backend.datatype.PriceCategory;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector.SectorDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.sector.SectorMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class SectorMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.sepm.groupphase.backend.entity.mapper")
    public static class SectorMapperTestContextConfiguration{

    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private SectorMapper sectorMapper;
    private static final Long SECTOR_ID = 1L;
    private static final Integer SECTOR_NUMBER = 10;
    private static final PriceCategory PRICE_CATEGORY = PriceCategory.CHEAP;

    @Test
    public void shouldMapSectorDTOToSector(){
        SectorDTO sectorDTO = SectorDTO.builder()
            .id(SECTOR_ID)
            .sectorNumber(SECTOR_NUMBER)
            .priceCategory(PRICE_CATEGORY)
            .build();
        Sector sector = sectorMapper.sectorDTOToSector(sectorDTO);
        assertThat(sector).isNotNull();
        assertThat(sector.getId()).isEqualTo(SECTOR_ID);
        assertThat(sector.getSectorNumber()).isEqualTo(SECTOR_NUMBER);
        assertThat(sector.getPriceCategory()).isEqualTo(PRICE_CATEGORY);
    }

    @Test
    public void shouldMapSectorToSectorDTO(){
        Sector sector = Sector.builder()
            .id(SECTOR_ID)
            .sectorNumber(SECTOR_NUMBER)
            .priceCategory(PRICE_CATEGORY)
            .build();
        SectorDTO sectorDTO = sectorMapper.sectorToSectorDTO(sector);
        assertThat(sectorDTO).isNotNull();
        assertThat(sectorDTO.getId()).isEqualTo(SECTOR_ID);
        assertThat(sectorDTO.getSectorNumber()).isEqualTo(SECTOR_NUMBER);
        assertThat(sectorDTO.getPriceCategory()).isEqualTo(PRICE_CATEGORY);
    }
}
