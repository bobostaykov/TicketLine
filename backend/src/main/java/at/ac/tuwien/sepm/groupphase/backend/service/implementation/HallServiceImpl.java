package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.HallRequestParameters;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.hall.HallMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public HallServiceImpl(HallRepository hallRepository, HallMapper hallMapper) {
        this.hallMapper = hallMapper;
        this.hallRepository = hallRepository;
    }

    @Override
    public List<HallDTO> findAllHalls(List<HallRequestParameters> fields) {
        LOGGER.info("Finding all halls with " + (isEmpty(fields) ?  "all parameters" : "parameters " + fields.toString()));
        return isEmpty(fields) ? hallMapper.hallListToHallDTOs(hallRepository.findAll()) :
            hallMapper.hallListToHallDTOs(hallRepository.findAllWithSpecifiedFields(fields));
    }

    @Override
    public HallDTO addHall(HallDTO hallDTO) {
        LOGGER.info("Validating and adding hall: " + hallDTO.toString());
        // map hall dto to entity
        Hall hall = hallMapper.hallDTOToHall(hallDTO);
        //sets child column in ManyToOne relationship and validate seats/sectors;
        if (!isEmpty(hall.getSeats())) {
            for(Seat seat : hall.getSeats()){
                seat.setHall(hall);
            }
        } else if (!isEmpty(hall.getSectors())) {
            for(Sector sector : hall.getSectors()){
                sector.setHall(hall);
            }
        }
        return hallMapper.hallToHallDTO(hallRepository.save(hall));
    }

    @Override
    public HallDTO findHallById(Long hallId) {
        LOGGER.info("Find hall with id " + hallId);
        return hallMapper.hallToHallDTO(hallRepository.findById(hallId).orElseThrow(NotFoundException::new));
    }
}
