package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private int clicks;

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

    //TODO: crear un constructor sin equipo, y otro solo con equipo

    public Player() {
    }

    public Player(String name, int clicks, Location location, Team teamsofplayer) {
        this.name = name;
        this.clicks = clicks;
        this.location = location;
        this.teamsOfPlayer.add(teamsofplayer);
    }

    public void addClicks(int clicks) {
        this.clicks += clicks;
    }
}
