package es.iesrafaelalberti.daw.dwes.clickcompetitionbase.services;

import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Player;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.model.Team;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.PlayerRepository;
import es.iesrafaelalberti.daw.dwes.clickcompetitionbase.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private TeamRepository teamRepository;

    public boolean imageStore(MultipartFile file, Long id) throws IOException {
        String myFileName = id.toString() + "_" + file.getOriginalFilename();

        if(playerRepository.existsById(id)){
            File directory = new File("./images/players/");
//            if(!directory.exists()){
//                directory.mkdir();
//            }
            Path targetPath = Paths.get(directory + "/" + myFileName).normalize();
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            Player player = playerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(id.toString()));
            player.setImageUrl("/download/players/" + myFileName);
            playerRepository.save(player);
            return true;
        }
        else if (teamRepository.existsById(id)){
            File directory = new File("./images/teams/");

            Path targetPath = Paths.get(directory + "/" + myFileName).normalize();
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            Team team = teamRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException(id.toString()));
            team.setImageUrl("/download/teams/" + myFileName);
            teamRepository.save(team);
            return true;
        }
        return true;
    }

    public Path imageShow(String name, String repo){
        Path targetPath = Paths.get("./images/" + repo + "/" + name).normalize();
        return targetPath;
    }
}
