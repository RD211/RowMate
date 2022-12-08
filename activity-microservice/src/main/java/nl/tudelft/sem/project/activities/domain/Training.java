package nl.tudelft.sem.project.activities.domain;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

public class Training {
    /**
     * Attributes
     */
    Pair<DateTime, DateTime> dateInterval;
    User owner;
    List<Pair<User, Boat, BoatRole>> participants;
    List<Boat> boats;

    /**
     * Constructor
     */
    public Training() {
        dateInterval = new Pair<>(DateTime, DateTime);
        owner = new User();     // What are the arguments of User?
        participants = new List<Pair<User, Boat, BoatRole>>();  // What arguments?
        boats = new ArrayList<>();
    }

    /**
     * Methods
     */
    public Boat hasPlace(User user) {       // How does one get two return types? I see "Boat, BoatRole"...
        return User.getBoat();              //
    }

    public Time getTimeUntilStart() {       // I didn't know a type Time exists
        return Time;
    }

    public void placeUser(User) {
        participants.add(User, Boat, BoatRole);     // Does this work?
//        participants.add(Pair<User, Boat, BoatRole>)    // Else something like this should work right?
    }

    public void removeUser(User) {
        participants.remove(User, Boat, BoatRole);     // Does this work?
//        participants.remove(Pair<User, Boat, BoatRole>)    // Else something like this should work right?
    }

    public void closeActivity() {
        // How Should I implement this?
    }

    public int placesLeft() {       // Should Boat be an input argument? Or do we mean for the whole training?
        // should be something like: getBoats.available() - getBoats.occupied()
    }

    public List<Boat> getBoats() {
        return boats;
    }

    public void moveActivity(Pair<DateTime, DateTime> ) {
        // something with setDateTime but idk how DateTime works
    }

    public boolean hasStarted() {
        // Should it look at the starting DateTime?
    }

    public boolean isLocked() {
        // I think it should automatically Lock when the Training has started
        // OR when the admin says so
    }

}
