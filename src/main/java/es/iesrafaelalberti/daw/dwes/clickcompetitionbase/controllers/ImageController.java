package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.controllers;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping(value = "/image")
    public ResponseEntity<Object> imageTest(@RequestParam("id") String id,
                                            @RequestParam("name") String name,
                                            @RequestParam("file") MultipartFile file) {
        try {
            imageService.imageStore(file, Long.parseLong(id));
        } catch (IOException e) {
            return new ResponseEntity<>("Error en archivo", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @GetMapping(value = "/download/{repo}/{name}")
    // TODO: this must be in service class
    //No sabia como poner esto en el servicio, asi que lo unico que pude hacer fue quitar
    //al menos el targetPath
    public ResponseEntity<Resource> getImage(@PathVariable("name") String name,
                                             @PathVariable("repo") String repo){
        //Me da a mi que lo que he hecho es inútil pero bueno XD
        Path targetPath = imageService.imageShow(name, repo);
        try {
            Resource resource = new UrlResource(targetPath.toUri());
            if (resource.exists()) {
                String contentType = Files.probeContentType(targetPath);
                return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
