package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.PlayerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

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

//    @GetMapping(value = "/player/morethan/{clicks}")
//    public ResponseEntity<Object> bestPlayers(@PathVariable("clicks") Integer clicks) {
//        return new ResponseEntity<>(playerRepository.findPlayerByClicks(clicks), HttpStatus.OK);
//    }

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





    @GetMapping("/logout")
    public ResponseEntity<Object> logout() {
        Player player = (Player) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        player.setToken(null);
        playerRepository.save(player);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        String token = null;
        // Test user/password
        Player player = playerRepository.findPlayerByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException());
        if(!new BCryptPasswordEncoder().matches(password, player.getPassword()))
//            return new ResponseEntity<>("Incorrect password", HttpStatus.FORBIDDEN);
            throw new EntityNotFoundException();
        // Compruebo que el usuario tenga token generado y no esté caducado...
        if(player.getToken() != null) {
            try {
                Jwts.parser().parse(player.getToken()).getBody();
                return new ResponseEntity<>("", HttpStatus.CONFLICT);
            } catch (Exception e) {
                player.setToken(null);
            }
        }
        // Generate token
//        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//                .commaSeparatedStringToAuthorityList("ROLE_USER");
        String secretKey = "pestillo";
        // TODO: Investigar todos los parámetros
        token = Jwts
                .builder()
                .setId("AlbertIES")
                .setSubject(username)
                .claim("authorities",
                        player.getRoles())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 6000000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        player.setToken(token);
        playerRepository.save(player);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
