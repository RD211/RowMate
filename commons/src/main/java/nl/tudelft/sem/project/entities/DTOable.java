package nl.tudelft.sem.project.entities;

public interface DTOable<T extends DTO> {
    T toDTO();
}
