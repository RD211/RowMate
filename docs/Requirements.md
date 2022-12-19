# Requirements - Rowing

## Must
* The system shall allow users to register with a username and password.
* The user shall be able to log in with the username and password specified at registration.
* Users must be able to specify their available time ranges.
* Users must be able to specify and change which roles they can fill: cox, coach, port side rower, starboard side rower or sculling rower.
* A training must have an activity owner, a time during which it lasts, number of boats available per training.
* Users must be able to introduce their certificates for being a cox
* There must be different boat types, each type requiring a different type of certificate.
* Certificates must have a hierarchy
* Users must be able to post trainings and competitions, becoming activity owners for them.
* Users must be able to see all trainings and competitions they can compete in
* Activity Owners must be able to deny or accept participants to activities
* Users must be able to leave activities if they desire.
* Competitions must be able to choose to allow amateur rowers or not.
* Users must be able to join activities.


## Should
* Users should have the ability to change their passwords
* Activity Owners should be able to close a listing whenever they choose to.
* Activity owners should be able to specify if their competition will require everyone to be part of the same organization or have the same gender
* Users should be able to specify and change their organization and gender in their profile
* Users should not be able to respond to an activity announcement less than half an hour before it starts
* The system should allow admins to add new boat types and certificates to the database.
* Activity Owners should be allowed to remove users from their activity 
* User accounts should include an email address that will be used for notifications.

## Could
* Activity Owners could be allowed to lock activities and not allow any other person to join the listing.

## Won't
* We won’t create a graphical user interface
* We won’t not allow users to change each other’s activities

## Non-functional requirements
* The implementation shall be written using Spring Boot with Java 11. It should use Gradle for package management.
* The system should use a microservices architecture.
* The users should communicate with the system through an API, without the use of a Graphical User Interface.
* The security part of the system should be implemented using Spring Security.