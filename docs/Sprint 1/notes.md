# Notes from SEM meeting Week 3 -  29.XI.2022




Attendants: Everybody
Starting time: 13:47
End time: 14:16
Chairman: David
Notetaker: Stiliyan




From Matei:
We received feedback about the UML diagrams - we generally did pretty good 
There are spring retrospectives on Brightspace that we should look at. Useful when we start having and assigning issues. They are also to reflect on what we did last week. Hands the meeting to the chairman.


From the chairman (David):
Last week we did the draft. We decided to not use postman and instead Swagger-UI for API testing. Hands back to Matei for feedback


Feedback from Matei:


The must haves look good. Activity owners should be able to edit listings and also kick people from listings. Developers are not admins.
We as developers should add the default boat types and certificates. Adding extra ones could either be allowed for anyone to make or just for admins. That would also require an admin distinction from regular users. It is a design choice in the end.
        Activity owners should be able to kick people (copy).
        The bounded context map looks overall pretty good. Notes: Activity owners should be connected to the User context. We could try to group trainings and competitions because there is some copy work. Better display the connection between them.
        Component diagram - The activity owner being a user is not present in our group. We should also keep in mind that the activity owner can also do more things than the regular user. Some confusion about the boat being certificate. The boat shouldn’t be certified since the certificates are specifically for the cox role for users, not for the boats.
        We don’t have to bother too much with security on the diagram because of Spring security (Matei then realises this is the template)
        We could add more detailed explanation to the diagrams. 




Questions from us to Matei:
Q: How many boats need to be available per training.
A: Depends on us. A design choice we can choose whatever we prefer.

Q: Roles for the boat what we can put.
A: It does not have to be one of each role. We can also have multiple of the same row per boat. Either the admin or the activity owner should be able to specify that.


Q: For Assignment 1 are the 4 pages for only the first part or total?
A: Assumedly it’s only for task 1


Q: How exactly should matchmaking and accepting people work that can fill multiple roles. 
A: A lot is up to design. For example users can have preferences or not. When a person is accepted we could immediately specify the role or not. 


Q: If we do both parts of the assignment will we get feedback on both of them on friday?
A: Maybe but priority will be given to the first part that’s actually of importance.


Other notes:
        Would be good to start creating gitlab issues and start assigning and dividing work. We should try to make them as concrete as possible, such as “this issue will be done when we have this and this and this”. We should put a lot of effort into that, also good to use weights, time estimates, milestones etc




Deadlines: 
* Architecture Draft this Friday  (02.XII). Uploaded to Gitlab