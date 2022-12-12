package nl.tudelft.sem.project.users.domain.users;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

class UsernameTest {
    @Test
    public void testConstructorValid() {
        var username = "good_username";
        assertDoesNotThrow(() -> new Username(username));
        assertEquals(username, new Username(username).getName());
    }

    @Test
    public void testConstructorInvalid() {
        assertThrows(NullPointerException.class, () -> new Username(null));
    }

    @Test
    public void testConstructorValidation() {
        assertThrows(ConstraintViolationException.class, () -> new Username("ad"));
    }
}