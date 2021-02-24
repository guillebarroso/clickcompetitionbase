package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    public Optional<Player> findPlayerById(Long id);

    @Query("Select p from Player p Where p.clicks >= ?1")
    Collection<Player> findPlayerByClicks(Integer clicks);

    @Query("SELECT p FROM Player p ORDER BY p.clicks desc")
    Collection<Player> findPlayerOrderByClicks();


}
