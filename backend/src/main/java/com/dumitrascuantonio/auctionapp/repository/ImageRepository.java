package com.dumitrascuantonio.auctionapp.repository;

import com.dumitrascuantonio.auctionapp.entity.Image;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByLotId(Long id);

}
