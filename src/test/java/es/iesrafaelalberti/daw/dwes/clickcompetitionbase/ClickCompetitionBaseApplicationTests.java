package es.iesrafaelalberti.daw.dwes.clickcompetitionbase;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.*;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ClickCompetitionBaseApplicationTests {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    CountryRepository countryRepository;
    @Autowired
    RoleRepository roleRepository;

    @Test
    void playerAdd() {
        Country c1 = countryRepository.save(new Country("España"));
        Region r1 = regionRepository.save(new Region("Cádiz", c1));
        Location l1 = locationRepository.save(new Location("San Fernando", r1));
        Team t1 = teamRepository.save(new Team("EquipoPrueba1"));
        Role role2 = roleRepository.save(new Role("ROLE_USER"));

        playerRepository.save(new Player("pruebaTest", "pestillo", 0, role2, l1, t1));

        assert playerRepository.findPlayerByUsername("pruebaTest").isPresent();
    }

    @Test
    public void findAllPlayers() {
        Country c1 = countryRepository.save(new Country("España"));
        Region r1 = regionRepository.save(new Region("Cádiz", c1));
        Location l1 = locationRepository.save(new Location("San Fernando", r1));
        Team t1 = teamRepository.save(new Team("EquipoPrueba1"));
        Role role2 = roleRepository.save(new Role("ROLE_USER"));

        playerRepository.save(new Player("pruebaTest", "pestillo", 0, role2, l1, t1));
        assertNotNull(playerRepository.findAll());
    }

    @Test
    public void deletePlayers() {
        Country c1 = countryRepository.save(new Country("España"));
        Region r1 = regionRepository.save(new Region("Cádiz", c1));
        Location l1 = locationRepository.save(new Location("San Fernando", r1));
        Team t1 = teamRepository.save(new Team("EquipoPrueba1"));
        Role role2 = roleRepository.save(new Role("ROLE_USER"));

        Player playerTest = playerRepository.save(new Player("pruebaTest", "pestillo", 0, role2, l1, t1));
        playerRepository.deleteById(playerTest.getId());
    }

}
