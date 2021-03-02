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

    Long id;
    String name;
    Long clicks;

    @ManyToMany(mappedBy = "teamsOfPlayer")
    @JsonBackReference
    Set<Player> playersInTeam;

    public Team(){
    }

    public Team(String name){
        this.name = name;
    }

    public Team(Long id, String name, Long clicks) {
        this.id = id;
        this.name = name;
        this.clicks = clicks;
    }

}


