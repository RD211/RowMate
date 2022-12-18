package nl.tudelft.sem.project.activities.database.entities;

import nl.tudelft.sem.project.activities.database.repository.BoatRepository;
import nl.tudelft.sem.project.activities.exceptions.BoatNotFoundException;
import nl.tudelft.sem.project.activities.exceptions.RoleNotFoundException;
import nl.tudelft.sem.project.enums.BoatRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class BoatService {

    @Autowired
    private transient BoatRepository boatRepository;

    /**
     * Saves a boat entity into the repository.
     *
     * @param boat Boat to save.
     * @return The boat which was added with the id filled in.
     */
    public Boat addBoat(Boat boat) {
        return boatRepository.save(boat);
    }

    /**
     * Gets a boat by a given id.
     *
     * @param id The id of the boat to find.
     * @return Boat object.
     * @throws BoatNotFoundException Thrown if there is no such boat.
     */
    public Boat getBoatById(UUID id) throws BoatNotFoundException {
        var result = boatRepository.findById(id);
        if (result.isEmpty()) {
            throw new BoatNotFoundException("Boat could not be found by id.");
        }
        return result.get();
    }

    /**
     * Deletes a boat by a given id.
     *
     * @param id Id for the boat to check.
     * @throws BoatNotFoundException Thrown if there is no such boat.
     */
    public void deleteBoatById(UUID id) throws BoatNotFoundException {
        boatRepository.delete(this.getBoatById(id));
    }

    /**
     * Renames a boat by a given id.
     *
     * @param id The id of the boat to rename.
     * @param newName New name for the boat.
     * @return The boat with its name changed.
     * @throws BoatNotFoundException Thrown if there is no such boat.
     */
    public Boat renameBoat(UUID id, String newName) throws BoatNotFoundException {
        var boat = this.getBoatById(id);
        boat.setName(newName);
        return boatRepository.save(boat);
    }

    /**
     * Removes a position that is now no longer available for the boat. Best used after
     * a user has been accepted into the activity.
     *
     * @param id The id of the boat to change.
     * @param role Role to remove.
     * @return The boat with a position removed.
     * @throws BoatNotFoundException Thrown if there is no such boat.
     * @throws RoleNotFoundException Thrown if there is no role available in the boat.
     */
    public Boat removeAvailablePositionFromBoat(UUID id, BoatRole role)
            throws BoatNotFoundException, RoleNotFoundException {
        var boat = this.getBoatById(id);
        if (boat.getAvailablePositions().stream().noneMatch(x -> x == role)) {
            throw new RoleNotFoundException("There is no available position for this role in the boat.");
        }
        List<BoatRole> rolesMutable = new ArrayList<>(boat.getAvailablePositions());
        rolesMutable.remove(role);
        boat.setAvailablePositions(rolesMutable);
        return boatRepository.save(boat);
    }

    /**
     * Adds an available position to the boat. Best used after a user has been removed
     * from an activity and the role they occupied has to be "refunded".
     *
     * @param id The id of the boat to change.
     * @param role Role to add.
     * @return The boat with a position added.
     * @throws BoatNotFoundException Thrown if there is no such boat.
     */
    public Boat addAvailablePositionToBoat(UUID id, BoatRole role) throws BoatNotFoundException {
        var boat = this.getBoatById(id);
        List<BoatRole> rolesMutable = new ArrayList<>(boat.getAvailablePositions());
        rolesMutable.add(role);
        boat.setAvailablePositions(rolesMutable);
        return boatRepository.save(boat);
    }

    /**
     * Changes the required certificate for the boat.
     *
     * @param boatID The id of the boat to edit.
     * @param newCertificateID The id of the new certificate to require.
     * @return The boat with the certificate changed.
     * @throws BoatNotFoundException Thrown if there is no such boat.
     */
    public Boat changeCoxCertificate(UUID boatID, UUID newCertificateID) throws BoatNotFoundException {
        var boat = this.getBoatById(boatID);
        boat.setCoxCertificateId(newCertificateID);
        return boatRepository.save(boat);
    }
}
