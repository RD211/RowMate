package nl.tudelft.sem.project;

public interface ConverterEntityDTO<Dto extends DTO, Entity> {
    Dto toDTO(Entity entity);
    Entity toEntity(Dto dto);
    Entity toDatabaseEntity(Dto dto);
}
