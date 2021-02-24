package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.bootstrap;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.*;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Seeder implements CommandLineRunner {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private TeamRepository teamRepository;


    @Override
    public void run(String... args) throws Exception {
        Country c1 = countryRepository.save(new Country("España"));
        Region r1 = regionRepository.save(new Region("Cádiz", c1));
        Location l1 = locationRepository.save(new Location("San Fernando", r1));
        Location l2 = locationRepository.save(new Location("Chiclana", r1));
        Team t1 = teamRepository.save(new Team("EquipoPrueba1"));
        Team t2 = teamRepository.save(new Team("EquipoPrueba2"));
        playerRepository.save(new Player("prueba1", 0, l1, t1));
        playerRepository.save(new Player("prueba2", 10, l1, t1));
        playerRepository.save(new Player("prueba3", 22, l2, t1));
        playerRepository.save(new Player("prueba4", 7, l2, t2));
        playerRepository.save(new Player("prueba5", 13, l2, t2));
        playerRepository.save(new Player("prueba6", 2, l1, t1));
    }
}
