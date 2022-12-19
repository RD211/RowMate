package nl.tudelft.sem.project.activities.database.repository;

import lombok.NonNull;
import nl.tudelft.sem.project.activities.database.entities.Boat;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoatRepository extends PagingAndSortingRepository<Boat, UUID> {

    @NonNull Optional<Boat> findById(@NonNull UUID id);

    @Override
    @NonNull
    List<Boat> findAll();
}
