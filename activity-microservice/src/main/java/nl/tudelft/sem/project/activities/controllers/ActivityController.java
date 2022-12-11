package nl.tudelft.sem.project.activities.controllers;

import nl.tudelft.sem.project.activities.services.ActivityService;
import nl.tudelft.sem.project.entities.activities.ActivityDTO;
import nl.tudelft.sem.project.entities.utils.ActivityFilterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    transient ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/find")
    public ResponseEntity<List<ActivityDTO>> findActivitiesFromFilter(@RequestBody ActivityFilterDTO dto) {
        return new ResponseEntity<List<ActivityDTO>>(activityService.findActivitiesFromFilter(dto), HttpStatus.OK);
    }
}
