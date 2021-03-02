package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;

    @GetMapping(value  = "/locations")
    public ResponseEntity<Object> locationsList() {
        return new ResponseEntity<>(locationRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value  = "/locations/ordered")
    public ResponseEntity<Object> locationsOrdered() {
        return new ResponseEntity<>(locationRepository.orderedByClicksLocation(), HttpStatus.OK);
    }


}
