package nl.tudelft.sem.project.activities.database.repository;

import nl.tudelft.sem.project.activities.database.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

}
