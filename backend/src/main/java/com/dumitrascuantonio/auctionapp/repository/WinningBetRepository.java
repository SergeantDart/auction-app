package com.dumitrascuantonio.auctionapp.repository;

import com.dumitrascuantonio.auctionapp.entity.WinningBet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WinningBetRepository extends JpaRepository<WinningBet, Long> {

    List<WinningBet> findAllByBetUserId(Long id);

}
