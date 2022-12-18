package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.ConverterEntityDTO;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.database.repository.BoatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoatConverterService implements ConverterEntityDTO<BoatDTO, Boat> {

    @Autowired
    transient BoatService boatService;

    /**
     * Creates a dto from the boat entity object.
     *
     * @param boat the boat entity.
     * @return the dto of the boat.
     */
    @Override
    public BoatDTO toDTO(Boat boat) {
        return new BoatDTO(
                boat.getId(),
                boat.getName(),
                boat.getAvailablePositions(),
                boat.getCoxCertificateId());
    }

    /**
     * Creates an entity from the dto.
     *
     * @param dto the dto.
     * @return the entity.
     */
    @Override
    public Boat toEntity(BoatDTO dto) {
        return Boat.builder()
                .id(dto.getBoatId())
                .name(dto.getName())
                .availablePositions(dto.getAvailablePositions())
                .coxCertificateId(dto.getCoxCertificateId()).build();
    }

    /**
     * Creates a database entity from the dto.
     *
     * @param dto the dto.
     * @return the database entity.
     */
    @Override
    public Boat toDatabaseEntity(BoatDTO dto) {
        return boatService.getBoatById(dto.getBoatId());
    }
}
