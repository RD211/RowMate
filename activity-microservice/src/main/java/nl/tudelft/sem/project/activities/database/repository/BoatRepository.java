package nl.tudelft.sem.project.activities.database.repository;

import nl.tudelft.sem.project.activities.database.entities.Boat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BoatRepository extends PagingAndSortingRepository<Boat, UUID> {

}
