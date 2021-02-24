package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    Long id;
    String name;
    @JsonBackReference //TODO: comprobar que esto no de problemas
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    Set<Location> locationRegion = new HashSet<>();

    @ManyToOne
    @JoinColumn()
    private Country country;

    public Region(){

    }

    public Region(String name, Country country){
        this.name = name;
        this.country = country;
    }
}
