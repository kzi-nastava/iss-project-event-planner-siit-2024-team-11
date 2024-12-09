package org.example.eventy.common.services;

import org.example.eventy.common.models.PicturePath;
import org.example.eventy.common.repositories.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;

    public List<PicturePath> save(List<String> paths) {
        List<PicturePath> picturePaths = new ArrayList<>();

        for(String path : paths) {
            PicturePath picturePath = pictureRepository.findByPath(path);

            if(picturePath == null) {
                picturePath = new PicturePath();
                picturePath.setPath(path);
                picturePath = pictureRepository.save(picturePath);
            }

            if(picturePath != null) {
                picturePaths.add(picturePath);
            }
        }

        return picturePaths;
    }
}
