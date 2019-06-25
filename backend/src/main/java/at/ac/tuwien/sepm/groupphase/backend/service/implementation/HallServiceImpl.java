package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDTO;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.HallRequestParameter;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.searchParameters.HallSearchParametersDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.hall.HallMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.location.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.CustomValidationException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ShowRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final ShowRepository showRepository;
    private final HallMapper hallMapper;
    private final LocationMapper locationMapper;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public HallServiceImpl(HallRepository hallRepository, HallMapper hallMapper, ShowRepository showRepository,
                           LocationMapper locationMapper) {
        this.hallMapper = hallMapper;
        this.hallRepository = hallRepository;
        this.showRepository = showRepository;
        this.locationMapper = locationMapper;
    }

    @Override
    public List<HallDTO> findHalls(List<HallRequestParameter> fields, HallSearchParametersDTO searchParameters) {
        LOGGER.info("Finding halls with " + (isEmpty(fields) ? "all parameters " : "parameters " + fields.toString())
            + (searchParameters != null ? "matching search parameters " + searchParameters.toString() : ""));
        if (searchParameters == null || (searchParameters.getName() == null && searchParameters.getLocation() == null)) {
            return isEmpty(fields) ? hallMapper.hallListToHallDTOs(hallRepository.findAll()) :
                hallMapper.hallListToHallDTOs(hallRepository.findAllWithSpecifiedFields(fields));
        } else {
            return isEmpty(fields) ?
                hallMapper.hallListToHallDTOs(hallRepository.findByNameContainingAndLocation(searchParameters.getName(),
                    locationMapper.locationDTOToLocation(searchParameters.getLocation()))) :
                hallMapper.hallListToHallDTOs(hallRepository.findAllFilteredWithSpecifiedFields(searchParameters, fields));
        }
    }

    @Override
    public HallDTO addHall(HallDTO hallDTO) throws CustomValidationException {
        LOGGER.info("Validating and adding hall: " + hallDTO.toString());
        // map hall dto to entity
        Hall hall = hallMapper.hallDTOToHall(hallDTO);
        // hall should only be saved if it is new or if updating is still enabled for this hall
        if (hall.isNew() || editingEnabled(hallDTO)) {
            //sets child column in ManyToOne relationship and validate seats/sectors;
            if (!isEmpty(hall.getSeats())) {
                for (Seat seat : hall.getSeats()) {
                    seat.setHall(hall);
                }
            } else if (!isEmpty(hall.getSectors())) {
                for (Sector sector : hall.getSectors()) {
                    sector.setHall(hall);
                }
            }
            return hallMapper.hallToHallDTO(hallRepository.save(hall));
        } else {
            throw new CustomValidationException("Hall could not be created because it already exists and is not allowed to be edited");
        }
    }

    @Override
    public HallDTO findHallById(Long hallId, List<HallRequestParameter> include) {
        LOGGER.info("Find hall with id " + hallId);
        HallDTO hallDTO =  hallMapper.hallToHallDTO(hallRepository.findById(hallId).orElseThrow(NotFoundException::new));
        if(include != null && include.contains(HallRequestParameter.EDITING)) {
            hallDTO.setEditingEnabled(! showRepository.existsByHallId(hallId));
        }
        return hallDTO;
    }

    @Override
    public HallDTO updateHall(HallDTO hallDTO) throws CustomValidationException {
        LOGGER.info("Update hall " + hallDTO.toString());
        if (editingEnabled(hallDTO)) {
            return hallMapper.hallToHallDTO(hallRepository.save(setSeatsOrSectorsAndMapToEntity(hallDTO)));
        }
        LOGGER.error("Hall cannot be edited because shows already exist for it");
        throw new CustomValidationException("There are shows that will take place in this hall. It cannot be edited any more!");
    }


    @Override
    public void deleteHall(Long hallId) {
        LOGGER.info("Deleting hall with id " + hallId);
        hallRepository.deleteById(hallId);
    }

    // checks if editing this hall is still enabled and throws a validation exception if this is not the case
    // editing halls is only allowed if no shows have been added to the hall yet
    private boolean editingEnabled(HallDTO hallDTO) {
        return hallDTO.getId() == null || ! showRepository.existsByHallId(hallDTO.getId());
    }

    // maps dto to hall entity and sets child column in ManyToOne relationship correctly
    private Hall setSeatsOrSectorsAndMapToEntity(HallDTO hallDTO) {
        Hall hall = hallMapper.hallDTOToHall(hallDTO);
        if (!isEmpty(hall.getSeats())) {
            for (Seat seat : hall.getSeats()) {
                seat.setHall(hall);
            }
        } else if (!isEmpty(hall.getSectors())) {
            for (Sector sector : hall.getSectors()) {
                sector.setHall(hall);
            }
        }
        return hall;
    }
}
