package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.apache.juli.logging.Log;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptsRepository loginAttemptsRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, LoginAttemptsRepository loginAttemptsRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptsRepository = loginAttemptsRepository;
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.userToUserDTO(userRepository.findAll());
    }

    @Override
    public UserDTO findOne(Long id) throws NotFoundException {
        return userMapper.userToUserDTO(userRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws ServiceException {
        LOGGER.info("creating user with name: " + userDTO.getUsername());
        try {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            LOGGER.info("Setting password");
            LOGGER.info("User = " +userDTO.toString());
            userDTO = userMapper.userToUserDTO(userRepository.save(userMapper.userDTOToUser(userDTO)));
            initAttemptsTable(userDTO);
            return userDTO;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Problems while creating user" + userDTO.toString());
            throw new ServiceException(e.getMessage(), e);
        }
    }

    private void initAttemptsTable (UserDTO userDTO){
        loginAttemptsRepository.save(LoginAttempts.builder()
            .setUser(userMapper.userDTOToUser(userDTO))
            .setBlocked(false)
            .setAttempts(0)
            .build());
    }

    public UserDTO findUserByName(String userName) throws NotFoundException{
        LOGGER.info("finding user with username: " + userName);
        Optional<User> found = userRepository.findOneByUsername(userName);
        if (found.isPresent()){
            return userMapper.userToUserDTO(found.get());
        }else {
            throw new NotFoundException("could not find User with username: " + userName);
        }

    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO findOneByUsername(String username) {
        return userMapper.userToUserDTO(userRepository.findOneByUsername(username).orElseThrow(NotFoundException::new));
    }

    @Override
    public boolean unblockUser(Long userId) throws NotFoundException {
        LOGGER.info("Unblocking User with id: "+ userId);
        Optional<LoginAttempts> attemptsFound = loginAttemptsRepository.findById(userId);
        if(attemptsFound.isPresent()){
            LoginAttempts loginAttempts = attemptsFound.get();
            loginAttempts.setNumberOfAttempts(0);
            loginAttempts.setBlocked(false);
            loginAttemptsRepository.save(loginAttempts);
            LOGGER.info("unblocked User with id: " + userId);
            return true;
        }else{
            throw new NotFoundException("could not find user with id: "+ userId);
        }
    }


}
