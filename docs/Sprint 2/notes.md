# SEM meeting Week 4 - 06.XII.2022


Present: Matei, Stiliyan, Teodor, David, Kuba, Andrei, Bodhi (online)
Chairman: Andrei
Notetaker: Stiliyan


Start Time: 13:46
End Time: 14:18
Info from Matei:
We should divide the work evenly and have a consensus on that. \


Matei: How is the state of our code?
Andrei: We have started work on the code mostly with 
Matei: From next week it would be nice if we can show a demo at the start of meetings.


Matei: We should have tests, both unit testing, mocking etc. Functional testing is looking at the requirements in the document and manually test if the code is working, then we write about the testing.




Feedback from the architecture:
Things that Matei likes have a checkmark. The rubric we use is a bit outdated and if we have any questions about that we can discuss that. Most of the things we did are good. The diagrams look good, though we should make them bigger next time. Our approach to security is good for the gateway vs security check per microservice. Good distinction between admin and user


We need to do some explanation of the architecture within the microservices. More explanation on how our system is synchronous.




Start on the agenda:

We should start working on the issues more this week. We’re doing well with the issue and the work division. We should do the entities and DTOs. Discuss what we will work on this week:
Andrei: Start working on the activity matchmaking.
Kuba: Haven’t entirely decided yet
David: Working on Users
Bodhi: Work on the issues
Stiliyan: Work on the issues
Teodor: Work on the issues


Questions And Answers:
Q: How to handle boats and certificates referring to each other.
A: Our current approach is good. 
Q: Difference between the main and the dev branches
A: Main should always be working and more be used for the working prototype, while dev should also generally work it can break.
Q: What’s expected of us by next week to be done?
A: As much as possible. If we have a working prototype then it’s good. So if we’re close to having all the must-haves then we’re good.




Deadlines:
* 16.XII.2022 (Next Friday) - Assignment 1
* 20.XII.2022 - optional meeting 
* 23.XII.2022 - working prototype