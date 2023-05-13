package com.dumitrascuantonio.auctionapp.repository;

import com.dumitrascuantonio.auctionapp.entity.Bet;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

    Optional<Bet> findByIdAndArchivalIsFalse(Long id);

    List<Bet> findAllByLotAndArchivalIsFalse(Lot lot);

}
