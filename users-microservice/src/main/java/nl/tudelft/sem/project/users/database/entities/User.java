package nl.tudelft.sem.project.users.database.entities;

import nl.tudelft.sem.project.entities.DTOable;
import nl.tudelft.sem.project.entities.users.UserDTO;

public class User implements DTOable<UserDTO> {
    @Override
    public UserDTO toDTO() {
        return null;
    }
}
