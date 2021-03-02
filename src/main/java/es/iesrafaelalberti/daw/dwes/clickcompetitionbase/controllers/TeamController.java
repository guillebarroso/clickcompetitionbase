package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamController {
    @Autowired
    TeamRepository teamRepository;

    @GetMapping(value  = "/team/ordered")
    public ResponseEntity<Object> teamOrdered(){
        return new ResponseEntity<>(teamRepository.orderedByClicksTeam(), HttpStatus.OK);
    }

}
