package at.ac.tuwien.sepm.groupphase.backend.service.implementation;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ServiceException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.Utils;
import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.userToUserDTO(userRepository.findAll());
    }

    @Override
    public UserDTO findOne(Long id) {
        return userMapper.userToUserDTO(userRepository.findById(id).orElseThrow(NotFoundException::new));
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) throws ServiceException {
        try {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            return userMapper.userToUserDTO(userRepository.save(userMapper.userDTOToUser(userDTO)));
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User findOneByName(String username) {
        return userRepository.findOneByName(username).orElseThrow(NotFoundException::new);
    }
}
