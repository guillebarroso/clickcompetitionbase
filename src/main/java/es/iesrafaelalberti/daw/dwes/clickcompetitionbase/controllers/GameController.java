package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.clicks.Clicks;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
public class GameController {
    @Autowired
    private PlayerRepository playerRepository;

    @PostMapping(value = "/addclicks/{playerid}")
    public ResponseEntity<Object> addClicks(@RequestBody Clicks clicks, @PathVariable("playerid") Long id) {
        Player player = playerRepository.findById(id)
                            .orElseThrow(EntityNotFoundException::new);
        player.addClicks(clicks.getClicks());
        playerRepository.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }
}
