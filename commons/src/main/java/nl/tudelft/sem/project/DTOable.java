package nl.tudelft.sem.project;

public interface DTOable<T extends DTO> {
    T toDTO();
}
