package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    @Autowired
    public CountryRepository countryRepository;

    @GetMapping(value  = "/country/ordered")
    public ResponseEntity<Object> CountriesList() {
        return new ResponseEntity<>(countryRepository.orderedByClicksCountry(), HttpStatus.OK);
    }


}
