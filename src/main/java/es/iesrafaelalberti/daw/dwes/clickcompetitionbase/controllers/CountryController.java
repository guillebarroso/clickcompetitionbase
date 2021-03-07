package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Country;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@RestController
public class CountryController {

    @Autowired
    public CountryRepository countryRepository;

    @GetMapping(value  = "/countries")
    public ResponseEntity<Object> countriesList() {
        return new ResponseEntity<>(countryRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping(value  = "/country/ordered")
    public ResponseEntity<Object> CountriesList() {
        return new ResponseEntity<>(countryRepository.orderedByClicksCountry(), HttpStatus.OK);
    }

    @PostMapping(value = "/add/country")
    public ResponseEntity<Object> countryAdd(@RequestParam("name") String name){
        Optional<Country> dataCountry = countryRepository.findCountryByName(name);
        if (dataCountry.isPresent()){return new ResponseEntity<>("Ese país ya existe", HttpStatus.CONFLICT);}
        Country newCountry = new Country(name);
        countryRepository.save(newCountry);
        return new ResponseEntity<>(newCountry, HttpStatus.OK);
    }

    @PutMapping(value = "/edit/country/{id}")
    public ResponseEntity<Object> countryUpdate(@PathVariable("id") Long id, @RequestParam("name") String name) throws EntityNotFoundException {
        Optional<Country> dataCountry = countryRepository.findCountryById(id);
        if (!dataCountry.isPresent()){return new ResponseEntity<>("Ese país no existe", HttpStatus.CONFLICT);}
        Country country = dataCountry.get();

        country.setName(name);

        countryRepository.save(country);
        return new ResponseEntity<>(country, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/country/{id}")
    public ResponseEntity<Object> countryDelete(@PathVariable("id") Long id){
        countryRepository.findCountryById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
        countryRepository.deleteById(id);
        return new ResponseEntity<>("País con id " + id + " borrado", HttpStatus.OK);
    }


}
