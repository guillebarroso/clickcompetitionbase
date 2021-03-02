package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Region;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface RegionRepository extends CrudRepository<Region, Long> {

    public Optional<Region> findRegionById(Long id);

    @Query("SELECT new es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Region(r.id, r.name, SUM(p.clicks)) " +
            "FROM Region r " +
            "INNER JOIN Location l " +
            "ON r.id = l.region.id " +
            "INNER JOIN Player p " +
            "ON l.id = p.location.id " +
            "GROUP BY r.id " +
            "ORDER BY SUM(p.clicks) DESC")
    public Collection<Region> orderedByClicksRegion();
}
