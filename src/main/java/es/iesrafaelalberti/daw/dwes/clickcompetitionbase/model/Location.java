package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    Long id;
    String name;
    //@JsonIgnore //Esto sirve para ignorar clicks cuando se muestre en el json
    Long clicks;
    @JsonBackReference //TODO: comprobar que esto no de problemas
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    Set<Player> playerLocation = new HashSet<>();

    @ManyToOne
    @JoinColumn()
    private Region region;

    public Location() {
    }

    public Location(String name, Region region) {
        this.name = name;
        this.region = region;
    }

    public Location(Long id, String name, Long clicks) {
        this.id = id;
        this.name = name;
        this.clicks = clicks;
    }
}
