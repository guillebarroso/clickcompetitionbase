package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegionController {

    @Autowired
    RegionRepository regionRepository;

    @GetMapping(value  = "/region/ordered")
    public ResponseEntity<Object> regionOrdered(){
        return new ResponseEntity<>(regionRepository.orderedByClicksRegion(), HttpStatus.OK);
    }
}
