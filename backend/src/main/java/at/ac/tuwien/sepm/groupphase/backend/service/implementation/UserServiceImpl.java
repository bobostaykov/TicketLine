package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() throws ServiceException {
        LOGGER.info("Find all users");
        try {
            return userMapper.userToUserDTO(userRepository.findAll());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public UserDTO findOne(Long id) throws NotFoundException {
        LOGGER.info("Find user with id " + id);
        return userMapper.userToUserDTO(userRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws ServiceException {
        LOGGER.info("Create user " + userDTO.getUsername());
        try {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            return userMapper.userToUserDTO(userRepository.save(userMapper.userDTOToUser(userDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(Long id) throws ServiceException {
        LOGGER.info("Remove user with id " + id);
        try {
            userRepository.deleteById(id);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public UserDTO findOneByUsername(String username) {
        return userMapper.userToUserDTO(userRepository.findOneByUsername(username).orElseThrow(NotFoundException::new));
    }
}
