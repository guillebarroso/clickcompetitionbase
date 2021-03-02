package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    @Column(length = 300)
    private String password;
    @Column(length = 300)
    private String token;
    private int clicks;
    private String imageUrl;

    @ManyToOne
    @JoinColumn()
    private Location location;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(
            name = "playersAndTeams",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    Set<Team> teamsOfPlayer = new HashSet<Team>();


    @ManyToOne
    @JoinColumn()
    private Role roles;


    public Player() {
    }

    public Player(String username, String password, int clicks, Role roles, Location location, Team teamsofplayer) {
        this.username = username;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.clicks = clicks;
        this.roles = roles;
        this.location = location;
        this.teamsOfPlayer.add(teamsofplayer);
    }

    public void addClicks(int clicks) {
        this.clicks += clicks;
    }
}
