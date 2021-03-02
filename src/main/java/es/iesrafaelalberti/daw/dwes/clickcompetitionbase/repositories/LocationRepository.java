package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.clicks.Clicks;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Location;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LocationRepository extends CrudRepository<Location, Long> {
    public Optional<Location> findLocationById(Long id);

    @Query("SELECT new es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Location(l.id, l.name, SUM(p.clicks)) " +
            "FROM Location l " +
            "INNER JOIN Player p " +
            "ON l.id = p.location.id " +
            "GROUP BY l.id " +
            "ORDER BY SUM(p.clicks) DESC")
    public Collection<Location> orderedByClicksLocation();
}
