package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.requestparameter.PasswordChangeRequest;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final Integer MAX_NUMBER_OF_ATTEMPTS = 4;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final LoginAttemptsRepository loginAttemptsRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, LoginAttemptsRepository loginAttemptsRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptsRepository = loginAttemptsRepository;
    }

    @Override
    public Page<UserDTO> getUsers(String username, Integer page, Integer pageSize) throws ServiceException {
        if (username == null)
            LOGGER.info("Get all users");
        else
            LOGGER.info("Search users with " + username + " as a part of their username");

        try {
            if(pageSize == null){
                pageSize = 10;
            }
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            if (username == null)
                return userRepository.findAll(pageable).map(userMapper::userToUserDTO);
            else
                return userRepository.findByUsernameContainingIgnoreCase(username, pageable).map(userMapper::userToUserDTO);
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
        try {

            if(userRepository.findOneByUsername(userDTO.getUsername()).isEmpty()){
                LOGGER.info("Create user with name: " + userDTO.getUsername());
                userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                LOGGER.info("Setting password");
                return userMapper.userToUserDTO(userRepository.createUser(userMapper.userDTOToUser(userDTO)));
            }else {
                return UserDTO.builder().id(-1L).build();
            }
        }catch (DataIntegrityViolationException e) {
            LOGGER.error("Problems while creating user" + userDTO.toString());
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public UserDTO findUserByName(String userName) throws NotFoundException{
        LOGGER.info("Finding user with username: " + userName);
        Optional<User> found = userRepository.findOneByUsername(userName);
        if (found.isPresent()){
            return userMapper.userToUserDTO(found.get());
        }else {
            throw new NotFoundException("Could not find User with username: " + userName);
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

    @Override
    public boolean unblockUser(Long userId) throws NotFoundException {
        LOGGER.info("Unblocking User with id: "+ userId);
        Optional<LoginAttempts> attemptsFound = loginAttemptsRepository.findById(userId);
        if(attemptsFound.isPresent()){
            LoginAttempts loginAttempts = attemptsFound.get();
            loginAttempts.setNumberOfAttempts(0);
            loginAttempts.setBlocked(false);
            loginAttempts.setNumberOfAttempts(0);
            loginAttemptsRepository.save(loginAttempts);
            LOGGER.info("unblocked User with id: " + userId);
            return true;
        }else{
            throw new NotFoundException("could not find user with id: "+ userId);
        }
    }

    @Override
    public boolean blockUser(Long userId) throws ServiceException {
        LOGGER.info("Blocking user with id: " + userId);
        Optional<LoginAttempts> loginAttemptsFound = loginAttemptsRepository.findById(userId);
        if(loginAttemptsFound.isPresent()){
            if(loginAttemptsFound.get().getUser().getType().equals(UserType.ADMIN)){
                throw new ServiceException("Admin can't be blocked");
            }
            LoginAttempts loginAttempts = loginAttemptsFound.get();
            if (loginAttempts.isBlocked()) {
                throw new ServiceException("User already blocked");
            }
            loginAttempts.setBlocked(true);
            loginAttemptsRepository.save(loginAttempts);
            LOGGER.info("Blocked user with id: " + userId);
            return true;
        }else
            throw new NotFoundException("Could not find user with id "+ userId);
    }

    @Override
    public Page<UserDTO> getBlockedUsers(String username, Integer page, Integer pageSize) throws ServiceException {
        if (username == null)
            LOGGER.info("Getting all blocked users");
        else
            LOGGER.info("Search blocked users with " + username + " as a part of their username");

        try {
            if(pageSize == null){
                pageSize = 10;
            }
            if(page < 0) {
                throw new IllegalArgumentException("Not a valid page.");
            }
            Pageable pageable = PageRequest.of(page, pageSize);
            List<LoginAttempts> blockedUserAttempts = loginAttemptsRepository.getAllByBlockedTrue();
            List<UserDTO> users = new ArrayList<>();
            Comparator<LoginAttempts> comparator = Comparator.comparing(la -> la.getUser().getId());
            blockedUserAttempts.stream()
                .sorted(comparator)
                .forEach(loginAttempts -> {
                    if (username == null) {
                        users.add(userMapper.userToUserDTO(loginAttempts.getUser()));
                    } else {
                        if (loginAttempts.getUser().getUsername().contains(username))
                            users.add(userMapper.userToUserDTO(loginAttempts.getUser()));
                    }
                });
            int totalElements = users.size();
            int from = page * pageSize;
            int offset = page * pageSize + pageSize > totalElements ? (totalElements - page * pageSize) : pageSize;
            List<UserDTO> sublist = users.subList(from, from + offset);
            Page<UserDTO> result = new PageImpl<>(sublist, pageable, totalElements);
            LOGGER.debug(result.getContent().toString());
            LOGGER.debug("totalElem: " + result.getTotalElements());
            LOGGER.debug("totalPages: " + result.getTotalPages());
            return result;
        } catch (PersistenceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void changePassword(PasswordChangeRequest passwordChangeRequest) throws ServiceException {
        LOGGER.info("changing password for user " + passwordChangeRequest.getId());
        Optional<User> userOptional = userRepository.findById(passwordChangeRequest.getId());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getType().equals(UserType.ADMIN)){
                throw new ServiceException("admins cant be blocked");
            }else{
                user.setPassword(passwordEncoder.encode(passwordChangeRequest.getPassword()));
                user = userRepository.save(user);
                unblockUser(user.getId());
            }
        }else{
            throw new NotFoundException("could not find user with id: " + passwordChangeRequest.getId());
        }
    }

}
