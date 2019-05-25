package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.LoginAttempts;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.LoginAttemptsRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoginAttemptsRepository loginAttemptsRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, LoginAttemptsRepository loginAttemptsRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.loginAttemptsRepository = loginAttemptsRepository;
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.userToUserDTO(userRepository.findAll());
    }

    @Override
    public UserDTO findOne(Long id) {
        return userMapper.userToUserDTO(userRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Transactional
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userRepository.save(userMapper.userDTOToUser(userDTO);
        LoginAttempts loginAttempts = LoginAttempts.builder()
            .setUser(user)
            .setAttempts(0)
            .setBlocked(false)
            .build();
        loginAttemptsRepository.save(loginAttempts);
        return userMapper.userToUserDTO(user);
    }
}
