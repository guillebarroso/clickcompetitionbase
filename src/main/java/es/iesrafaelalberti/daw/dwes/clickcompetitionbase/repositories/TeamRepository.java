package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Long> {
    public Optional<Team> findTeamById(Long id);
    public Optional<Team> findTeamByName(String name);

    @Query("SELECT new es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Team(t.id, t.name, SUM(p.clicks)) " +
            "FROM Team t " +
            "JOIN t.playersInTeam p " +
            "GROUP BY t.id " +
            "ORDER BY SUM(p.clicks) DESC")
    public Collection<Team> orderedByClicksTeam();
}
