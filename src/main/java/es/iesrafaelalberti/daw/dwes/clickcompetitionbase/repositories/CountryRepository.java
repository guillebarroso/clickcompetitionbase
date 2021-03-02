package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Country;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface CountryRepository extends CrudRepository<Country, Long> {

    public Optional<Country> findCountryById(Long id);

    @Query("SELECT new es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Country(c.id, c.name, SUM(p.clicks)) " +
            "FROM Country c " +
            "INNER JOIN Region r " +
            "ON c.id = r.country.id " +
            "INNER JOIN Location l " +
            "ON r.id = l.region.id " +
            "INNER JOIN Player p " +
            "ON l.id = p.location.id " +
            "GROUP BY c.id " +
            "ORDER BY SUM(p.clicks) DESC")
    public Collection<Country> orderedByClicksCountry();
}
