package org.example.eventy.common.repositories;

import org.example.eventy.common.models.PicturePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<PicturePath, Long> {

}
