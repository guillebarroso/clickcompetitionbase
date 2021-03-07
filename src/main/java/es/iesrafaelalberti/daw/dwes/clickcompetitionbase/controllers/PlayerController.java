package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Location;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Role;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Team;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.LocationRepository;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.PlayerRepository;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.RoleRepository;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.TeamRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.Optional;

@RestController
public class PlayerController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private RoleRepository roleRepository;


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
    public ResponseEntity<Object> playerAdd(@RequestParam("username") String username,
                                            @RequestParam("password") String password,
                                            @RequestParam("nameLocation") String nameLocation){
        Optional<Location> dataLocation = locationRepository.findLocationByName(nameLocation);
        if (!dataLocation.isPresent()){return new ResponseEntity<>("Ese localidad no existe", HttpStatus.CONFLICT);}
        Location location = dataLocation.get();
        Role rol = roleRepository.save(new Role("ROLE_USER"));
        Player nuevoPlayer = new Player(username, password, location, rol);
        Optional<Player> oldPlayer = playerRepository.findPlayerById(nuevoPlayer.getId());
        if (oldPlayer.isPresent()){return new ResponseEntity<>("Ese ya existe", HttpStatus.CONFLICT);}
        playerRepository.save(nuevoPlayer);
        return new ResponseEntity<>(nuevoPlayer, HttpStatus.OK);
    }

    @PutMapping(value = "/edit/player/{id}")
    public ResponseEntity<Object> playerUpdate(@PathVariable("id") Long id, @RequestParam("username") String username,
                                                                             @RequestParam("password") String password,
                                                                             @RequestParam("nameLocation") String nameLocation) throws EntityNotFoundException{
        Optional<Player> dataPlayer = playerRepository.findPlayerById(id);
        if (!dataPlayer.isPresent()){return new ResponseEntity<>("Ese jugador no existe", HttpStatus.CONFLICT);}
        Player player = dataPlayer.get();

        Optional<Location> dataLocation = locationRepository.findLocationByName(nameLocation);
        if (!dataLocation.isPresent()){return new ResponseEntity<>("Ese localidad no existe", HttpStatus.CONFLICT);}
        Location location = dataLocation.get();

        player.setUsername(username);
        player.setPassword(new BCryptPasswordEncoder().encode(password));
        player.setLocation(location);

        playerRepository.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @PutMapping(value = "/add/team/player/{id}")
    public ResponseEntity<Object> addTeamToPlayer(@PathVariable("id") Long id, @RequestParam("nameTeam") String nameTeam) throws EntityNotFoundException{
        Optional<Player> dataPlayer = playerRepository.findPlayerById(id);
        if (!dataPlayer.isPresent()){return new ResponseEntity<>("Ese jugador no existe", HttpStatus.CONFLICT);}
        Player player = dataPlayer.get();

        Optional<Team> dataTeam = teamRepository.findTeamByName(nameTeam);
        if (!dataTeam.isPresent()){return new ResponseEntity<>("Ese equipo no existe", HttpStatus.CONFLICT);}
        Team team = dataTeam.get();

        player.addTeam(team);

        playerRepository.save(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete/player/{id}")
    public ResponseEntity<Object> playerDelete(@PathVariable("id") Long id){
        playerRepository.findPlayerById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        playerRepository.deleteById(id);
        return new ResponseEntity<>("Player con id " + id + " borrado", HttpStatus.OK);
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
