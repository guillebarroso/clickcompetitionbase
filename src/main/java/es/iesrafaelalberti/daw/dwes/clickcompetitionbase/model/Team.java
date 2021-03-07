package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;
    private String name;
    private Long clicks;
    private String imageUrl;

    @ManyToMany(mappedBy = "teamsOfPlayer")
    @JsonBackReference
    Set<Player> playersInTeam;

    public Team(){
    }

    public Team(String name){
        this.name = name;
        this.imageUrl = "/download/teams/team_default.png";
    }

    public Team(Long id, String name, Long clicks) {
        this.id = id;
        this.name = name;
        this.clicks = clicks;
    }

}


