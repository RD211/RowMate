package nl.tudelft.sem.project.users.domain.users;

import lombok.*;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.shared.OrganizationAttributeConverter;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;

import javax.persistence.*;
import java.util.*;


/**
 * The user entity.
 * Stores the details that are required for a user.
 * Extends DTO-able since it will need to be able to be transformed into
 * a dto.
 */
@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class User {

    @Column(nullable = false, unique = true)
    @EmbeddedId
    protected Username username;

    @Column(nullable = false, unique = true)
    @Convert(converter = UserEmailAttributeConverter.class)
    protected UserEmail email;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    protected Gender gender;

    @Column(nullable = false)
    @Builder.Default
    protected boolean isAmateur = true;

    @Column(nullable = true)
    @Convert(converter = OrganizationAttributeConverter.class)
    protected Organization organization;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ElementCollection
    @Builder.Default
    protected Set<BoatRole> boatRoles = new HashSet<>();

    @ElementCollection
    @Column(nullable = false)
    @Builder.Default
    protected Set<DateInterval> availableTime = new HashSet<>();

    @OneToMany
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Column(nullable = false)
    @Builder.Default
    protected Set<Certificate> certificates = new HashSet<>();

    /**
     * Adds an available time interval to the user set.
     *
     * @param dateInterval the new interval to be added.
     */
    public void addAvailableTime(DateInterval dateInterval) {
        if (this.availableTime == null) {
            this.availableTime = new HashSet<>();
        }
        this.availableTime.add(dateInterval);
    }

    /**
     * Removes an interval from the user interval collection.
     *
     * @param dateInterval the interval to be removed.
     */
    public void removeAvailableTime(DateInterval dateInterval) {
        if (this.availableTime == null) {
            this.availableTime = new HashSet<>();
        }
        this.availableTime.remove(dateInterval);
    }

    /**
     * Adds a certificate to the user collection.
     *
     * @param certificate the certificate to be added.
     */
    public void addCertificate(Certificate certificate) {
        if (this.certificates == null) {
            this.certificates = new HashSet<>();
        }
        this.certificates.add(certificate);
    }


    /**
     * Removes a certificate from the user collection.
     *
     * @param certificate the certificate to be removed.
     */
    public void removeCertificate(Certificate certificate) {
        if (this.certificates == null) {
            this.certificates = new HashSet<>();
        }
        this.certificates.remove(certificate);
    }

    /**
     * Adds a boat role to the user collection.
     *
     * @param boatRole the role to be added.
     */
    public void addBoatRole(BoatRole boatRole) {
        if (this.boatRoles == null) {
            this.boatRoles = new HashSet<>();
        }
        this.boatRoles.add(boatRole);
    }

    /**
     * Removes a boat role from the user.
     *
     * @param boatRole the role to be removed.
     */
    public void removeBoatRole(BoatRole boatRole) {
        if (this.boatRoles == null) {
            this.boatRoles = new HashSet<>();
        }
        this.boatRoles.remove(boatRole);
    }
}
