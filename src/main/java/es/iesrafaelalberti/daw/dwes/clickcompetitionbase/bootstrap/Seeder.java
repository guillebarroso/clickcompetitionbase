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
    private RoleRepository roleRepository;
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
        Country c2 = countryRepository.save(new Country("Italia"));
        Region r1 = regionRepository.save(new Region("Cádiz", c1));
        Region r2 = regionRepository.save(new Region("Toscana", c2));
        Location l1 = locationRepository.save(new Location("San Fernando", r1));
        Location l2 = locationRepository.save(new Location("Chiclana", r1));
        Location l3 = locationRepository.save(new Location("Florencia", r2));
        Team t1 = teamRepository.save(new Team("EquipoPrueba1"));
        Team t2 = teamRepository.save(new Team("EquipoPrueba2"));
        Role role1 = roleRepository.save(new Role("ROLE_ADMIN"));
        Role role2 = roleRepository.save(new Role("ROLE_USER"));

        playerRepository.save(new Player("prueba1", "pestillo", 0, role1, l1, t1));
        playerRepository.save(new Player("prueba2", "pestillo", 10, role2, l1, t1));
        playerRepository.save(new Player("prueba3", "pestillo", 22, role2, l2, t1));
        playerRepository.save(new Player("prueba4", "pestillo", 7, role2, l2, t2));
        playerRepository.save(new Player("prueba5", "pestillo", 13, role2, l2, t2));
        playerRepository.save(new Player("prueba6", "pestillo", 2, role2, l1, t1));
        playerRepository.save(new Player("prueba7", "pestillo", 23, role2, l3, t2));
        playerRepository.save(new Player("prueba8", "pestillo", 20, role2, l3, t1));


    }
}
