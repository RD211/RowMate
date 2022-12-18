package nl.tudelft.sem.project.authentication.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.tudelft.sem.project.authentication.authentication.JwtTokenGenerator;
import nl.tudelft.sem.project.authentication.domain.user.AppUser;
import nl.tudelft.sem.project.authentication.domain.user.HashedPassword;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.domain.user.PasswordHashingService;
import nl.tudelft.sem.project.authentication.domain.user.UserRepository;
import nl.tudelft.sem.project.authentication.framework.integration.utils.JsonUtil;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.Token;
import nl.tudelft.sem.project.shared.Username;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@ExtendWith(SpringExtension.class)
// activate profiles to have spring use mocks during auto-injection of certain beans.
@ActiveProfiles({"test", "mockPasswordEncoder", "mockTokenGenerator", "mockAuthenticationManager"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UsersTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private transient PasswordHashingService mockPasswordEncoder;

    @Autowired
    private transient JwtTokenGenerator mockJwtTokenGenerator;

    @Autowired
    private transient AuthenticationManager mockAuthenticationManager;

    @Autowired
    private transient UserRepository userRepository;

    @Test
    public void register_withValidData_worksCorrectly() throws Exception {
        // Arrange
        final Username testUser = new Username("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        AppUserModel model = new AppUserModel();
        model.setUsername(testUser);
        model.setPassword(testPassword);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().isOk());

        AppUser savedUser = userRepository.findByUsername(testUser).orElseThrow();

        assertThat(savedUser.getUsername()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(testHashedPassword);
    }

    @Test
    public void register_withExistingUser_throwsException() throws Exception {
        // Arrange
        final Username testUser = new Username("SomeUser");
        final Password newTestPassword = new Password("password456");
        final HashedPassword existingTestPassword = new HashedPassword("password123");

        AppUser existingAppUser = new AppUser(testUser, existingTestPassword, false);
        userRepository.save(existingAppUser);

        AppUserModel model = new AppUserModel();
        model.setUsername(testUser);
        model.setPassword(newTestPassword);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().isBadRequest());

        AppUser savedUser = userRepository.findByUsername(testUser).orElseThrow();

        assertThat(savedUser.getUsername()).isEqualTo(testUser);
        assertThat(savedUser.getPassword()).isEqualTo(existingTestPassword);
    }

    @Test
    public void login_withValidUser_returnsToken() throws Exception {
        // Arrange
        final Username testUser = new Username("SomeUser");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        when(mockAuthenticationManager.authenticate(argThat(authentication ->
                        !testUser.getName().equals(authentication.getPrincipal().toString())
        || !testPassword.getPasswordValue().equals(authentication.getCredentials().toString())

        ))).thenThrow(new UsernameNotFoundException("User not found"));

        final Token testToken = new Token("testJWTToken");
        when(mockJwtTokenGenerator.generateToken(
            argThat(userDetails -> userDetails.getUsername().equals(testUser.getName())))
        ).thenReturn(testToken);

        AppUser appUser = new AppUser(testUser, testHashedPassword, false);
        userRepository.save(appUser);

        AppUserModel model = new AppUserModel();
        model.setUsername(testUser);
        model.setPassword(testPassword);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));


        // Assert
        MvcResult result = resultActions
                .andExpect(status().isOk())
                .andReturn();

        Token responseModel = JsonUtil.deserialize(result.getResponse().getContentAsString(),
                Token.class);

        assertThat(responseModel).isEqualTo(testToken);
    }

    @Test
    public void login_withNonexistentUsername_returns403() throws Exception {
        // Arrange
        final Username testUser = new Username("SomeUser");
        final Password testPassword = new Password("password123");

        when(mockAuthenticationManager.authenticate(argThat(authentication ->
                testUser.equals(authentication.getPrincipal())
                    && testPassword.equals(authentication.getCredentials())
        ))).thenThrow(new UsernameNotFoundException("User not found"));

        AppUserModel model = new AppUserModel();
        model.setUsername(testUser);
        model.setPassword(testPassword);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().is4xxClientError());

        verify(mockJwtTokenGenerator, times(0)).generateToken(any());
    }

    @Test
    public void login_withInvalidPassword() throws Exception {
        // Arrange
        final Username testUser = new Username("SomeUser");
        final Password wrongPassword = new Password("password1234");
        final Password testPassword = new Password("password123");
        final HashedPassword testHashedPassword = new HashedPassword("hashedTestPassword");
        when(mockPasswordEncoder.hash(testPassword)).thenReturn(testHashedPassword);

        when(mockAuthenticationManager.authenticate(argThat(authentication ->
                testUser.getName().equals(authentication.getPrincipal().toString())
                    && wrongPassword.getPasswordValue().equals(authentication.getCredentials().toString())
        ))).thenThrow(new BadCredentialsException("Invalid password"));

        AppUser appUser = new AppUser(testUser, testHashedPassword, false);
        userRepository.save(appUser);

        AppUserModel model = new AppUserModel();
        model.setUsername(testUser);
        model.setPassword(wrongPassword);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.serialize(model)));

        // Assert
        resultActions.andExpect(status().is4xxClientError());

        verify(mockJwtTokenGenerator, times(0)).generateToken(any());
    }
}
