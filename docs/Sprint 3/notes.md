# SEM meeting Week 5 - 13.XII.2022


Present: Matei, Stiliyan, Teodor, David, Kuba, Andrei
Absent: Bodhi
Chairman: David
Notetaker: Stiliyan


Start Time: 13:47
End Time: 14:15


Start of meeting: Talking about the state of our code
We believe we’re doing good on the code we have endpoints and the general structure of the code, we have good coverage. Should start working on the gateway as it’s not currently working. The services are not really connected to each other yet so we should work on that. We are working on that. 
Authentication is done at the gateway and we don’t do it anywhere else in the code. We are using feign for microservice communication. 


Info from Matei:
We’re on track so far. We should do input validation when we’re doing things related to databases.
Matei has taken a look at our architecture. The component diagram looks good. Notes: We should expand more on security. connect it to the persistence storage in the diagrams. The class diagrams are good they don’t have to match the final product 1 to 1.
We should start thinking about the design patterns we’re using for the second part of Assignment 1. We should not mention the chain of responsibility in the report because it was given to us by the course staff. 



Questions And Answers:
Q: Does the gateway count as a facade? 
A: Yes it is. Useful website for design pattern info: https://refactoring.guru/. We should preferably use the design patterns from the lectures. 
Q: Where do we put the full sprint retrospectives and agendas?
A: There should be a doc in the GitLab for this.
Q: Is the readme file important for the project?
A: No.
Q: Should we do end-to-end testing and how complicated is it?
A: The staff is still debating on that. We will probably get more information with Assignment 2. Before the break, we should only do unit tests and mock tests. 
Q: Do we think we’re distributing the work well enough?
A: For most of us, yes, but Bodhi doesn’t have any experience so it’s harder for him. In general we’re putting our best 
Q: Are we allowed to add a ton of libraries?
A: Matei has not heard any rules about that. It’s probably fine as long as they 
are not writing too much code for us.




Deadlines:
* 16.XII.2022 - Assignment 1 UML Diagrams
* 23.XII.2022 - Final code
* 23.XII.2022 - Assignment 1 Design Patterns