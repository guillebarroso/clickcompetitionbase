package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Location;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Role;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Team;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class TeamController {
    @Autowired
    TeamRepository teamRepository;

    @GetMapping(value  = "/team/ordered")
    public ResponseEntity<Object> teamOrdered(){
        return new ResponseEntity<>(teamRepository.orderedByClicksTeam(), HttpStatus.OK);
    }

    @GetMapping(value  = "/teams")
    public ResponseEntity<Object> locationsList() {
        return new ResponseEntity<>(teamRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/add/team")
    public ResponseEntity<Object> playerAdd(@RequestParam("name") String name){
        Optional<Team> dataTeam = teamRepository.findTeamByName(name);
        if (dataTeam.isPresent()){return new ResponseEntity<>("Ese equipo ya existe", HttpStatus.CONFLICT);}
        Team newTeam = new Team(name);
        teamRepository.save(newTeam);
        return new ResponseEntity<>(newTeam, HttpStatus.OK);
    }

    @PutMapping(value = "/edit/team/{id}")
    public ResponseEntity<Object> studentUpdate(@PathVariable("id") Long id, @RequestParam("name") String name) throws EntityNotFoundException {
        Optional<Team> dataTeam = teamRepository.findTeamById(id);
        if (!dataTeam.isPresent()){return new ResponseEntity<>("Ese equipo no existe", HttpStatus.CONFLICT);}
        Team team = dataTeam.get();

        team.setName(name);

        teamRepository.save(team);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/team/{id}")
    public ResponseEntity<Object> playerDelete(@PathVariable("id") Long id){
        teamRepository.findTeamById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        teamRepository.deleteById(id);
        return new ResponseEntity<>("Team con id " + id + " borrado", HttpStatus.OK);
    }

}
