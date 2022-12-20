package nl.tudelft.sem.project.gateway.controllers;

import nl.tudelft.sem.project.activities.BoatsClient;
import nl.tudelft.sem.project.authentication.AppUserModel;
import nl.tudelft.sem.project.authentication.AuthClient;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.Token;
import nl.tudelft.sem.project.gateway.CreateUserModel;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.UsersClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AdminControllerTest {

    @Mock
    private transient UsersClient usersClient;

    @Mock
    private transient BoatsClient boatsClient;

    @InjectMocks
    AdminController adminController;

    @Test
    void deleteUserByUsername() {
        adminController.deleteUserByUsername("tester");
        verify(usersClient, times(1)).deleteUserByUsername(new Username("tester"));
        verifyNoMoreInteractions(usersClient);
    }

    @Test
    void deleteUserByEmail() {
        adminController.deleteUserByEmail("tester@test.com");
        verify(usersClient, times(1)).deleteUserByEmail(new UserEmail("tester@test.com"));
        verifyNoMoreInteractions(usersClient);
    }
}