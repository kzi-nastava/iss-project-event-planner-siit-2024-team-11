package org.example.eventy.common.services;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.repositories.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
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

    public List<PicturePath> save(List<MultipartFile> images) {
        List<PicturePath> picturePaths = new ArrayList<>();

        for(MultipartFile image : images) {
            try {
                PicturePath picturePath = new PicturePath();
                picturePath.setPath(lastPathNumber + ".jpg");
                this.saveImage(image);
                picturePath = pictureRepository.save(picturePath);
                picturePaths.add(picturePath);
            }
            catch (Exception e) {
                return null;
            }
        }

        return picturePaths;
    }

    public static String getImage(String path) {
        File file = new File(path); // read from where?

        try {
            return "data:image/jpeg;base64," + Arrays.toString(Files.readAllBytes(file.toPath()));
        }
        catch (IOException e) {
            return null;
        }
    }

    private void saveImage(MultipartFile multipartFile) throws IOException {

        //Kreiranje putanje direktorijuma
        Path uploadPath = Paths.get(uploadDir);

        //Provjera postojanja direktorijuma
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        //Čitanje sadržaja fajla i njegovo kopiranje datu fajl putanju
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(lastPathNumber + ".jpg"); //puna putanja na kojoj treba da se sačuva fajl

            System.out.println("FilePath je:" + uploadPath.toAbsolutePath());

            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            lastPathNumber++;
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + lastPathNumber + ".jpg", ioe);
        }
    }
}
