# Retrospective Sprint 3

## Issues table
Here is the table of issues

|Story|Task name|Task assigned to|Time estimated|Time spent|Done|Notes|
|:----|:----|:----|:----|:----|:----|:----|
|All user actions|Create the user controller|David|6|7|TRUE| |
|The system should allow admins to add new boat types and certificates|Configure the admin user|David|3|5|TRUE| |
|Users must be able to register and authenticate|Create the gateway connection to auth|David|4|5|TRUE| |
|Users must be able to manage boats|Connect boats to gateway|David|3|2|TRUE| |
|Users should be able to change their password|Create a reset password endpoint|David|3|3|TRUE| |
|Users must be able to receive notifications upon doing actions like creating an account or joining an activity|Create the notifications microservice start|Teo|3|6|TRUE| |
|Misc.|Create the notifications microservice tests|Teo|2|3|TRUE| |
|Users must be able to receive notifications upon doing actions like creating an account or joining an activity|Refactor the mail sending service so that it sends out actual activity details once ActivityDTO has been fully implemented|Teo|2|1|FALSE|Work In Progress|
|All boat actions|Create the boat controller|Stiliyan|4|6|TRUE| |
|Users should be able to see a list of activities they can join, request to join an activity or have the system find an activity for them given a strategy. Users should be able to leave activities.|Create the activity finder service and search strategies.|Andrei|5|6|TRUE| |
| |Create the activity finder controller|Andrei|4|3|TRUE| |
| |Create certificates service|Kuba|6|9|TRUE| |
| |Create certificate controller|Kuba|5|5|FALSE|Testing|

# Problems from previous week
We managed to solve most of the problems encountered in the previous week namely:

- We improved our communication for merge request reviews by using discord and our chat bot that informs us of new merge requests!
- We organized in order for everybody to understand our architecture!
- We divided the work more evenly for assignment texts!
- We strugled with created some of the entities but we managed to pull through!
- We had some problems with configuration files but we also solved them!

# Problems encountered

Here is a list of problems we encountered:

- Some issues proved to take longer than expected.
- Some merge request reviews took a long time still even with our better communication.
- Encountered issues with autowiring and custom beans, connecting things to feign.
- Getting stuck with weird spring errors, when trying to set up more complicated integration tests.
- Some of us had issues with feign clients.