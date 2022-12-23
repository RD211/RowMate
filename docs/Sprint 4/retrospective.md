# Retrospective Sprint 4

## Issues table
Here is the table of issues

|Story|Task name|Task assigned to|Time estimated|Time spent|Done|Notes|
|:----|:----|:----|:----|:----|:----|:----|
|Users must be able to post activities|Create an activity endpoint|David|4|4|TRUE| |
|Users must be able to get boats|Connect boats to gateway|David|3|3|TRUE| |
|Users must be able authenticate and register|Finish up activity and users|David|5|4|TRUE| |
|Tests|Bump up the testing percentage|David|2|2|TRUE| |
|Activity owners must be able to manipulate the activity|Activity Management Endpoints|Stiliyan|6|8|TRUE| |
| |Test certificates functionality for full coverage|Kuba|3|5|TRUE| |
| |Make functional test for certificates|Kuba|2|2|TRUE| |
| |Redo functional tests framework|Kuba|1|1|TRUE| |
| |Add certificate checks in matchmaking service|Kuba|1|1|TRUE| |
|Competition Owners must be able to choose if amateurs are allowed |Finish up the activity matchmaking |Andrei|4|4|TRUE| |
| |Implement the joining, leaving, accept, decline endpoints for activities |Andrei|5|6|TRUE| |
| |Add functional tests for notifications|Teo|3|4|TRUE| |
| |Refactor the mail sending service so that it sends out actual activity details once ActivityDTO has been fully implemented|Teo|2|4|TRUE| |
|Docs|Add docs|David|-|-|TRUE| |


# Problems from previous week
We managed to solve most of the problems encountered in the previous week namely:

- We improved our communication for merge request reviews by using discord and our chat bot that informs us of new merge requests!
- We organized in order for everybody to understand our architecture!
- We divided the work more evenly for assignment texts!
- We helped each other understand the libraries we are using like Feign, Swagger and such.


# Problems encountered

Here is a list of problems we encountered:

- Difficulty with calling notifications from other microservices.
- Working with code not written by ourselves sometimes proved to be a challenge because of lock of documentation.
- Making the matchmaker able to choose competitions according to their conditions was a bit trickier than expected.
- Some issues proved to take longer than expected.
- Some merge request reviews took a long time still even with our better communication.