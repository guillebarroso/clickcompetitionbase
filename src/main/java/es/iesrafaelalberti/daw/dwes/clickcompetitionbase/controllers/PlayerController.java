package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Location;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.LocationRepository;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;

    //TODO: crear un response entity que me lo ordene por equipos, provincias...

    @GetMapping(value  = "/players")
    public ResponseEntity<Object> playersList() {
        return new ResponseEntity<>(playerRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value = "/player/{id}")
    public ResponseEntity<Object> playerDetail(@PathVariable("id") Long id) {
        return new ResponseEntity<>(playerRepository.findById(id).orElseThrow(EntityNotFoundException::new),
                                    HttpStatus.OK);
    }

    @GetMapping(value = "/player/morethan/{clicks}")
    public ResponseEntity<Object> bestPlayers(@PathVariable("clicks") Integer clicks) {
        return new ResponseEntity<>(playerRepository.findPlayerByClicks(clicks), HttpStatus.OK);
    }

    @GetMapping(value = "/player/ordered")
    public ResponseEntity<Object> orderBestPlayers() {
        return new ResponseEntity<>(playerRepository.findPlayerOrderByClicks(), HttpStatus.OK);
    }

    @PostMapping(value = "/add/players")
    public ResponseEntity<Object> playerAdd(@RequestBody Player player){
        Optional<Player> oldPlayer = playerRepository.findPlayerById(player.getId());
        if (oldPlayer.isPresent()){return new ResponseEntity<>("Ese ya existe", HttpStatus.CONFLICT);}
        playerRepository.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }
}
