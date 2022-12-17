package nl.tudelft.sem.project.users.domain.users;

import nl.tudelft.sem.project.ConverterEntityDTO;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.domain.certificate.CertificateConverterService;
import nl.tudelft.sem.project.users.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.stream.Collectors;


@Service
public class UserConverterService implements ConverterEntityDTO<UserDTO, User> {

    @Autowired
    transient UserService userService;
    @Autowired
    transient CertificateConverterService certificateConverterService;

    @Override
    public UserDTO toDTO(User user) {
        return UserDTO
                .builder()
                .email(user.getEmail().getEmail())
                .username(user.getUsername().getName())
                .isAmateur(user.isAmateur())
                .boatRoles(user.getBoatRoles())
                        .gender(user.getGender())
                                .organization(user.getOrganization())
                .availableTime(user.getAvailableTime())
                .certificates(user.getCertificates() == null ? null : user.getCertificates().stream()
                        .map(c -> certificateConverterService.toDTO(c))
                        .collect(Collectors.toSet())
                )
                .build();
    }

    @Override
    public User toEntity(UserDTO dto) throws ValidationException {
        return User.builder()
                .username(new Username(dto.getUsername()))
                .email(new UserEmail(dto.getEmail()))
                .gender(dto.getGender())
                .availableTime(dto.getAvailableTime())
                .organization(dto.getOrganization())
                .boatRoles(dto.getBoatRoles())
                .isAmateur(dto.isAmateur())
                .certificates(dto.getCertificates() == null ? null :
                        dto.getCertificates().stream()
                                .map(x -> certificateConverterService.toEntity(x))
                                .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public User toDatabaseEntity(UserDTO dto) throws UserNotFoundException {
        return userService.getUserByUsername(new Username(dto.getUsername()));
    }
}
