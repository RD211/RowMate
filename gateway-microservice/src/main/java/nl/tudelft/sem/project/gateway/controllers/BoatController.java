package nl.tudelft.sem.project.gateway.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import nl.tudelft.sem.project.activities.BoatDTO;
import nl.tudelft.sem.project.activities.BoatsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api/boats")
public class BoatController {

    @Autowired
    private transient BoatsClient boatsClient;

    @GetMapping("/get_boat")
    public ResponseEntity<BoatDTO> getBoatById(@RequestParam("boatId") @Valid UUID boatId) {
        var boat = boatsClient.getBoat(boatId);
        return ResponseEntity.ok(boat);
    }

    @GetMapping("/get_boats")
    public ResponseEntity<List<BoatDTO>> getAllBoats() {
        var boats = boatsClient.getAllBoats();
        return ResponseEntity.ok(boats);
    }
}
