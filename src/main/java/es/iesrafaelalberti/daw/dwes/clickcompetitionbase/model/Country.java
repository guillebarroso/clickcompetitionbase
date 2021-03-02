package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    Long id;
    String name;
    Long clicks;
    @JsonBackReference //TODO: comprobar que esto no de problemas
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    Set<Region> regionCountry = new HashSet<>();

    public Country(){

    }
    public Country(String name){
        this.name = name;
    }

    public Country(Long id, String name, Long clicks) {
        this.id = id;
        this.name = name;
        this.clicks = clicks;
    }
}
