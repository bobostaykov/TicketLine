package at.ac.tuwien.sepm.groupphase.backend.integrationtest.base;

import at.ac.tuwien.sepm.groupphase.backend.configuration.JacksonConfiguration;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationConstants;
import at.ac.tuwien.sepm.groupphase.backend.security.AuthenticationEventListener;
import at.ac.tuwien.sepm.groupphase.backend.service.HeaderTokenAuthenticationService;
import at.ac.tuwien.sepm.groupphase.backend.service.implementation.AppUserDetailsService;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import org.assertj.core.util.Strings;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public abstract class BaseIntegrationTestWithMockedUserCredentials {

    private static final String SERVER_HOST = "http://localhost";
    private static final String USER_USERNAME = "user";
    private static final String USER_PASSWORD = "password";
    private static final String ADMIN_PASSWORD = "password";
    private static final String ADMIN_USERNAME = "admin";

    @Value("${server.context-path}")
    private String contextPath;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @MockBean
    public AppUserDetailsService userDetailsService;
    @MockBean
    public AuthenticationEventListener authenticationEventListener;
    @LocalServerPort
    private int port;

    @Autowired
    private HeaderTokenAuthenticationService simpleHeaderTokenAuthenticationService;

    @Autowired
    private JacksonConfiguration jacksonConfiguration;

    protected String validUserTokenWithPrefix;
    protected String validAdminTokenWithPrefix;

    @Before
    public void beforeBase() {

        org.springframework.security.core.userdetails.User.UserBuilder userBuilder = null;
        UserDetails userAdmin = org.springframework.security.core.userdetails.User.withUsername("admin").password(passwordEncoder.encode("password")).authorities("ADMIN", "USER").build();
        UserDetails userUser = org.springframework.security.core.userdetails.User.withUsername("user").password(passwordEncoder.encode("password")).authorities("USER").build();

  /*
        BDDMockito.given(userRepository.findOneByUsername("user"))
            .willReturn(Optional.of(User.builder().username("user").password(passwordEncoder.encode("password")).type(UserType.SELLER).id(1L).build()));
        BDDMockito.given(userRepository.findOneByUsername("admin"))
            .willReturn(Optional.of(User.builder().username("admin").password(passwordEncoder.encode("password")).type(UserType.ADMIN).id(2L).build()));
   */
        BDDMockito.given(userDetailsService.loadUserByUsername("user")).willReturn(userUser);
        BDDMockito.given(userDetailsService.loadUserByUsername("admin")).willReturn(userAdmin);

        RestAssured.baseURI = SERVER_HOST;
        RestAssured.basePath = contextPath;
        RestAssured.port = port;
        RestAssured.config = RestAssuredConfig.config().
            objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory((aClass, s) ->
                jacksonConfiguration.jackson2ObjectMapperBuilder().build()));
        validUserTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(USER_USERNAME, USER_PASSWORD).getCurrentToken())
            .with(" ");
        validAdminTokenWithPrefix = Strings
            .join(
                AuthenticationConstants.TOKEN_PREFIX,
                simpleHeaderTokenAuthenticationService.authenticate(ADMIN_USERNAME, ADMIN_PASSWORD).getCurrentToken())
            .with(" ");
    }
}

