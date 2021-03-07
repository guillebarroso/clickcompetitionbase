package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Location;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Region;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.LocationRepository;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class LocationController {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private RegionRepository regionRepository;

    @GetMapping(value  = "/locations")
    public ResponseEntity<Object> locationsList() {
        return new ResponseEntity<>(locationRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value  = "/locations/ordered")
    public ResponseEntity<Object> locationsOrdered() {
        return new ResponseEntity<>(locationRepository.orderedByClicksLocation(), HttpStatus.OK);
    }

    @PostMapping(value = "/add/location")
    public ResponseEntity<Object> locationAdd(@RequestParam("name") String name,
                                            @RequestParam("nameRegion") String nameRegion){
        Optional<Region> dataRegion = regionRepository.findRegionByName(nameRegion);
        if (!dataRegion.isPresent()){return new ResponseEntity<>("Esa región no existe", HttpStatus.CONFLICT);}
        Region region = dataRegion.get();
        Location nuevaLocation = new Location(name, region);
        Optional<Location> oldLocation = locationRepository.findLocationById(nuevaLocation.getId());
        if (oldLocation.isPresent()){return new ResponseEntity<>("Esa location ya existe", HttpStatus.CONFLICT);}
        locationRepository.save(nuevaLocation);
        return new ResponseEntity<>(nuevaLocation, HttpStatus.OK);
    }

    @PutMapping(value = "/edit/location/{id}")
    public ResponseEntity<Object> locationUpdate(@PathVariable("id") Long id, @RequestParam("name") String name,
                                               @RequestParam("nameRegion") String nameRegion) throws EntityNotFoundException{
        Optional<Location> dataLocation = locationRepository.findLocationById(id);
        if (!dataLocation.isPresent()){return new ResponseEntity<>("Esa location no existe", HttpStatus.CONFLICT);}
        Location location = dataLocation.get();

        Optional<Region> dataRegion = regionRepository.findRegionByName(nameRegion);
        if (!dataRegion.isPresent()){return new ResponseEntity<>("Esa region no existe", HttpStatus.CONFLICT);}
        Region region = dataRegion.get();

        location.setName(name);
        location.setRegion(region);

        locationRepository.save(location);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/location/{id}")
    public ResponseEntity<Object> locationDelete(@PathVariable("id") Long id){
        locationRepository.findLocationById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        locationRepository.deleteById(id);
        return new ResponseEntity<>("Location con id " + id + " borrado", HttpStatus.OK);
    }


}
