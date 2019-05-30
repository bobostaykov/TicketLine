package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.Hall.HallMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
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

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public HallServiceImpl(HallRepository hallRepository, HallMapper hallMapper) {
        this.hallMapper = hallMapper;
        this.hallRepository = hallRepository;
    }

    @Override
    public List<HallDTO> findAllHalls() {
        LOGGER.info("Finding all halls saved in the system");
        return hallMapper.hallListToHallDTOs(hallRepository.findAll());
    }

    @Override
    public HallDTO addHall(HallDTO hallDTO) throws ServiceException {
        LOGGER.info("Validating and adding hall: " + hallDTO.toString());
        //validate hallDTO
        validateHallDTO(hallDTO);
        // map hall dto to entity
        Hall hall = hallMapper.hallDTOToHall(hallDTO);
        // set child column in ManyToOne relationship and validate seats/sectors;
        if (!isEmpty(hall.getSeats())) {
            for(Seat seat : hall.getSeats()){
                validateSeat(seat);
                seat.setHall(hall);
            }
        } else if (!isEmpty(hall.getSectors())) {
            for(Sector sector : hall.getSectors()){
                validateSector(sector);
                sector.setHall(hall);
            }
        }
        return hallMapper.hallToHallDTO(hallRepository.save(hall));
    }

    //helper method to validate hall dto and improve code readability
    private void validateHallDTO(HallDTO hallDTO) throws ServiceException {
        if (hallDTO.getName() == null || hallDTO.getName().isBlank()) {
            LOGGER.error("Hall " + hallDTO.toString() + " could not be added: Name was empty");
            throw new ServiceException("Hall " + hallDTO.toString() + " could not be added: Name must not be empty");
        }
        if (hallDTO.getLocation() == null) {
            LOGGER.error("Hall " + hallDTO.toString() + " could not be added: Hall Location was not set");
            throw new ServiceException("Hall " + hallDTO.toString() + " could not be added: Hall Location must be set");
        }
        if (!isEmpty(hallDTO.getSeats()) && !isEmpty(hallDTO.getSectors())) {
            LOGGER.error("Hall " + hallDTO.toString() + " could not be added because it contains both seats and sectors");
            throw new ServiceException("Hall " + hallDTO.toString() + " could not be added: Hall cannot contain both seats and sectors");
        }
    }
    //helper method to validate seat and improve code readability
    private void validateSeat(Seat seat) throws ServiceException {
        if (seat.getSeatNumber() == null || seat.getSeatNumber() < 1) {
            LOGGER.error("Hall could not be added due to seat " + seat.toString() +
                ". Seat Number was " + (seat.getSeatNumber() == null ? "not set." : "less than 1."));
            throw new ServiceException("Hall could not be added due to seat " + seat.toString() +
                ". Seat Number must be set and greater than 0");
        }
        if(seat.getSeatRow() == null || seat.getSeatRow() < 1){
            LOGGER.error("Hall could not be added due to seat " + seat.toString() +
                ". Seat Row was " + (seat.getSeatRow() == null ? "not set." : "less than 1."));
            throw new ServiceException("Hall could not be added due to seat " + seat.toString() +
                ". Seat Row must be set and greater than 0");
        }
    }
    //helper method to validate sector and improve code readability
    private void validateSector(Sector sector) throws ServiceException{
        if(sector.getSectorNumber() == null || sector.getSectorNumber() < 1){
            LOGGER.error("Hall could not be added due to sector " + sector.toString()
                + ". Sector number was " + (sector.getSectorNumber() == null ? "not set-" : "less than 1."));
            throw new ServiceException("Hall could not be added due to sector " + sector.toString()
                + ". Sector number must be set and greater than 0");
        }
    }
}
