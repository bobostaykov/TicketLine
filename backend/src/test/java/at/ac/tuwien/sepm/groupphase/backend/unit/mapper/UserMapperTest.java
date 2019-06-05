package at.ac.tuwien.sepm.groupphase.backend.unit.mapper;

import at.ac.tuwien.sepm.groupphase.backend.datatype.UserType;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.user.UserDTO;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.user.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.sepm.groupphase.backend.entity.mapper")
    public static class UserMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private UserMapper userMapper;

    private static final long USER_ID = 1L;
    private static final String USER_NAME = "Ken";
    private static final UserType USER_TYPE = UserType.SELLER;
    private static final LocalDateTime USER_SINCE = LocalDateTime.of(2019, 1, 1, 12, 0, 0, 0);
    private static final LocalDateTime USER_LAST_LOGIN = LocalDateTime.of(2030, 6, 15, 3, 0, 0, 0);
    private static final String PASSWORD = "Barbie <3";

    @Test
    public void shouldMapUserToUserDTO() {
        User user = User.builder()
            .id(USER_ID)
            .username(USER_NAME)
            .password(PASSWORD)
            .type(USER_TYPE)
            .userSince(USER_SINCE)
            .lastLogin(USER_LAST_LOGIN)
            .build();
        UserDTO userDTO = userMapper.userToUserDTO(user);
        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getId()).isEqualTo(1L);
        assertThat(userDTO.getUsername()).isEqualTo(USER_NAME);
        assertThat(userDTO.getType()).isEqualTo(USER_TYPE);
        assertThat(userDTO.getUserSince()).isEqualTo(USER_SINCE);
        assertThat(userDTO.getLastLogin()).isEqualTo(USER_LAST_LOGIN);
        assertThat(userDTO.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    public void shouldMapUserDTOToUser() {
        UserDTO userDTO = UserDTO.builder()
            .id(1L)
            .username(USER_NAME)
            .type(USER_TYPE)
            .userSince(USER_SINCE)
            .lastLogin(USER_LAST_LOGIN)
            .password(PASSWORD)
            .build();
        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo(USER_NAME);
        assertThat(user.getType()).isEqualTo(USER_TYPE);
        assertThat(user.getUserSince()).isEqualTo(USER_SINCE);
        assertThat(user.getLastLogin()).isEqualTo(USER_LAST_LOGIN);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }

}
