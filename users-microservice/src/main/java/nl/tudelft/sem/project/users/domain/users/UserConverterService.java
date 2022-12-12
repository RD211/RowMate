package nl.tudelft.sem.project.users.domain.users;

import nl.tudelft.sem.project.ConverterEntityDTO;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.users.domain.certificate.Certificate;
import nl.tudelft.sem.project.users.domain.certificate.CertificateNotFoundException;
import nl.tudelft.sem.project.users.exceptions.UserNotFoundException;
import nl.tudelft.sem.project.utils.Existing;
import nl.tudelft.sem.project.utils.Fictional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.Null;
import javax.validation.groups.Default;
import java.util.stream.Collectors;


@Service
public class UserConverterService implements ConverterEntityDTO<UserDTO, User> {

    @Autowired
    transient UserService userService;

    @Override
    public UserDTO toDTO(User user) {
        return UserDTO
                .builder()
                .id(user.getId())
                .email(user.getEmail().getEmail())
                .username(user.getUsername().getName())
                .boatRoles(user.getBoatRoles())
                        .gender(user.getGender())
                                .organization(user.getOrganization())
                .availableTime(user.getAvailableTime())
                .certificates(user.getCertificates() == null ? null : user.getCertificates().stream().map(
                        Certificate::toDTO
                ).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public User toEntity(UserDTO dto) throws ValidationException {
        return User.builder().id(dto.getId())
                .username(new Username(dto.getUsername()))
                .email(new UserEmail(dto.getEmail()))
                .gender(dto.getGender())
                .availableTime(dto.getAvailableTime())
                .organization(dto.getOrganization())
                .boatRoles(dto.getBoatRoles())
                .certificates(dto.getCertificates() == null ? null :
                        dto.getCertificates().stream().map(x ->
                                 new Certificate(x, null)
                        ).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public User toDatabaseEntity(UserDTO dto) throws UserNotFoundException {
        return userService.getUserById(dto.getId());
    }
}
