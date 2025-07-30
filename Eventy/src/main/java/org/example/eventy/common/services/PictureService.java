package org.example.eventy.common.services;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.repositories.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;

    private final String uploadDir = "src/main/resources/static";
    private int lastPathNumber = 0;

    public PictureService() {
        File pictureDir = new File(uploadDir);
        lastPathNumber = pictureDir.list().length;
    }

    public List<PicturePath> save(List<String> base64Images) {
        List<PicturePath> picturePaths = new ArrayList<>();

        for(String base64Image : base64Images) {
            try {
                PicturePath picturePath = new PicturePath();
                picturePath.setPath(lastPathNumber + ".jpg");
                this.saveImage(base64Image);
                picturePath = pictureRepository.save(picturePath);
                picturePaths.add(picturePath);
            }
            catch (Exception e) {
                return null;
            }
        }

        return picturePaths;
    }

    public static String getImage(String imagePath) {
        try {
            Path path = Paths.get("src/main/resources/static/" + imagePath);

            byte[] fileBytes = Files.readAllBytes(path);
            String base64Image = Base64.getEncoder().encodeToString(fileBytes);
            return "data:image/jpeg;base64," + base64Image;
        }
        catch (IOException e) {
            return null;
        }
    }

    private void saveImage(String base64Image) throws IOException {

        //Kreiranje putanje direktorijuma
        Path uploadPath = Paths.get(uploadDir);

        //Provjera postojanja direktorijuma
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        //Čitanje sadržaja fajla i njegovo kopiranje datu fajl putanju
        try {
            // Remove the "data:image/jpeg;base64," part if it exists
            if (base64Image.contains("base64,")) {
                base64Image = base64Image.split("base64,")[1];
            }

            // Decode the Base64 string into a byte array
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // Define the file path where the image will be saved
            Path filePath = uploadPath.resolve(lastPathNumber + ".jpg");

            // Write the decoded bytes to the file
            Files.write(filePath, imageBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            // Increment the path number for the next image
            lastPathNumber++;
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + lastPathNumber + ".jpg", ioe);
        }
    }
}
