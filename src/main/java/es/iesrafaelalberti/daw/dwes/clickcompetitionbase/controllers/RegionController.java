package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.*;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.CountryRepository;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class RegionController {

    @Autowired
    RegionRepository regionRepository;
    @Autowired
    CountryRepository countryRepository;

    @GetMapping(value  = "/regions")
    public ResponseEntity<Object> regionList() {
        return new ResponseEntity<>(regionRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value  = "/region/ordered")
    public ResponseEntity<Object> regionOrdered(){
        return new ResponseEntity<>(regionRepository.orderedByClicksRegion(), HttpStatus.OK);
    }

    @PostMapping(value = "/add/region")
    public ResponseEntity<Object> regionAdd(@RequestParam("name") String name,
                                            @RequestParam("nameCountry") String nameCountry){
        Optional<Country> dataCountry = countryRepository.findCountryByName(nameCountry);
        if (!dataCountry.isPresent()){return new ResponseEntity<>("Ese país no existe", HttpStatus.CONFLICT);}
        Country country = dataCountry.get();
        Region nuevaRegion = new Region(name, country);
        Optional<Region> oldRegion = regionRepository.findRegionById(nuevaRegion.getId());
        if (oldRegion.isPresent()){return new ResponseEntity<>("Esa región ya existe", HttpStatus.CONFLICT);}
        regionRepository.save(nuevaRegion);
        return new ResponseEntity<>(nuevaRegion, HttpStatus.OK);
    }

    @PutMapping(value = "/edit/region/{id}")
    public ResponseEntity<Object> regionUpdate(@PathVariable("id") Long id, @RequestParam("name") String name,
                                                @RequestParam("nameCountry") String nameCountry) throws EntityNotFoundException{
        Optional<Region> dataRegion = regionRepository.findRegionById(id);
        if (!dataRegion.isPresent()){return new ResponseEntity<>("Esa región no existe", HttpStatus.CONFLICT);}
        Region region = dataRegion.get();

        Optional<Country> dataCountry = countryRepository.findCountryByName(nameCountry);
        if (!dataCountry.isPresent()){return new ResponseEntity<>("Ese país no existe", HttpStatus.CONFLICT);}
        Country country = dataCountry.get();

        region.setName(name);
        region.setCountry(country);

        regionRepository.save(region);
        return new ResponseEntity<>(region, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/region/{id}")
    public ResponseEntity<Object> regionDelete(@PathVariable("id") Long id){
        regionRepository.findRegionById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        regionRepository.deleteById(id);
        return new ResponseEntity<>("Region con id " + id + " borrado", HttpStatus.OK);
    }
}
