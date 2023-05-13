package com.dumitrascuantonio.auctionapp.repository;

import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    Optional<Lot> findByIdAndArchivalIsFalse(Long id);

    List<Lot> findAllByUserAndActiveAndArchivalIsFalseOrderByStartDateDesc(User user, boolean active);

    List<Lot> findAllByActiveAndArchivalIsFalse(boolean active);

    @Query("SELECT DISTINCT b.lot FROM Bet AS b WHERE b.userId = :userId AND b.archival = false")
    List<Lot> findDistinctLotByUserIdAndArchivalIsFalse(@Param("userId") Long userId);

    List<Lot> findFirst10ByActiveIsTrueAndArchivalIsFalseOrderByStartDateDesc();

    List<Lot> findAllByTitleContainsAndArchivalIsFalse(String keyword);

}

