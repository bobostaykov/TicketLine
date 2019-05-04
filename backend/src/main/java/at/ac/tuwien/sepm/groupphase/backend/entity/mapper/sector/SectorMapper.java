package at.ac.tuwien.sepm.groupphase.backend.entity.mapper.sector;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.sector.SectorDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SectorMapper {

    /**
     * converts Sector entity to SectorDTO
     * @param sector to be converted to dto
     * @return converted SectorDTO
     */
    SectorDTO sectorToSectorDTO(Sector sector);

    /**
     * converts sectorDTO to sector entity
     * @param sectorDTO to be converted to Sector
     * @return converted Sector entity
     */
    Sector sectorDTOToSector(SectorDTO sectorDTO);
}
