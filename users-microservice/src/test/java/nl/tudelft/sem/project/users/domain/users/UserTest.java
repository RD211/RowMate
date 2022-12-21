package nl.tudelft.sem.project.users.domain.users;

import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.users.UserEmail;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    DateInterval getDateInterval(int year) {
        return new DateInterval(java.sql.Timestamp.valueOf(LocalDateTime.of(year, 12, 1, 1, 1, 1, 1)),
                java.sql.Timestamp.valueOf(
                        LocalDateTime.of(year, 12, 1, 1, 1, 1, 1)));
    }

    @Test
    void addAvailableTime() {

        var firstInterval = getDateInterval(2050);
        var secondInterval = getDateInterval(2060);
        Set<DateInterval> times = new HashSet<>();

        times.add(firstInterval);

        var user = User.builder()
                .email(new UserEmail("user@user.com"))
                .availableTime(times)
                .build();
        user.addAvailableTime(
                secondInterval
        );

        assertTrue(user.getAvailableTime().contains(secondInterval));
    }

    @Test
    void removeAvailableTime() {
        var firstInterval = getDateInterval(2050);
        var secondInterval = getDateInterval(2060);
        Set<DateInterval> times = new HashSet<>();

        times.add(firstInterval);
        times.add(secondInterval);
        var user = User.builder()
                .email(new UserEmail("user@user.com"))
                .availableTime(times)
                .build();
        user.removeAvailableTime(
                secondInterval
        );
        user.removeAvailableTime(
                firstInterval
        );
        assertTrue(user.getAvailableTime().isEmpty());
    }

    @Test
    void addCertificate() {

        var cert1 = new Certificate("cert1");
        var cert2 = new Certificate("cert2");
        Set<Certificate> set = new HashSet<>();
        set.add(cert1);

        var user = User.builder()
                .email(new UserEmail("user@user.com"))
                .certificates(set)
                .build();
        user.addCertificate(
                cert2
        );

        assertTrue(user.getCertificates().contains(cert2));
    }

    @Test
    void removeCertificate() {

        var cert1 = new Certificate("cert1");
        var cert2 = new Certificate("cert2");
        Set<Certificate> set = new HashSet<>();
        set.add(cert1);
        set.add(cert2);

        var user = User.builder()
                .email(new UserEmail("user@user.com"))
                .certificates(set)
                .build();
        user.removeCertificate(
                cert2
        );
        user.removeCertificate(
                cert1
        );
        assertTrue(user.getCertificates().isEmpty());
    }

    @Test
    void addBoatRole() {

        Set<BoatRole> set = new HashSet<>();
        set.add(BoatRole.Coach);

        var user = User.builder()
                .email(new UserEmail("user@user.com"))
                .boatRoles(set)
                .build();
        user.addBoatRole(
                BoatRole.Cox
        );

        assertTrue(user.getBoatRoles().contains(BoatRole.Cox));
    }

    @Test
    void removeBoatRole() {

        Set<BoatRole> set = new HashSet<>();
        set.add(BoatRole.Coach);
        set.add(BoatRole.Other);

        var user = User.builder()
                .email(new UserEmail("user@user.com"))
                .boatRoles(set)
                .build();
        user.removeBoatRole(
                BoatRole.Other
        );
        user.removeBoatRole(
                BoatRole.Coach
        );
        assertTrue(user.getBoatRoles().isEmpty());
    }
}