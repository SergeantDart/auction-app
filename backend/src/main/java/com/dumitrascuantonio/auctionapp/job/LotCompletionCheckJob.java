package com.dumitrascuantonio.auctionapp.job;

import com.dumitrascuantonio.auctionapp.entity.Bet;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.WinningBet;
import com.dumitrascuantonio.auctionapp.service.LotService;
import com.dumitrascuantonio.auctionapp.service.WinningBetService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LotCompletionCheckJob implements Job {

    @Autowired
    private LotService lotService;

    @Autowired
    private WinningBetService winningBetService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Lot> activeLots = lotService.getAllLots(true);

        LocalDateTime currentTime = LocalDateTime.now();

        List<Lot> modifiedLots = new ArrayList<>();
        List<WinningBet> winningBets = new ArrayList<>();

        for (Lot lot : activeLots) {
            if (currentTime.isAfter(lot.getEndDate())) {
                log.info("Lot with id: {} was changed.", lot.getId());
                lot.setActive(false);
                modifiedLots.add(lot);

                Optional<Bet> maxBet = lot.getBets()
                        .stream()
                        .filter(bet -> !bet.isArchival())
                        .max(Comparator.comparing(Bet::getAmount));

                if (maxBet.isPresent()) {
                   WinningBet winningBet = new WinningBet();
                   winningBet.setBet(maxBet.get());
                   winningBet.setCreatedDate(currentTime);
                }
            }
        }

        lotService.saveLots(modifiedLots);
        winningBetService.saveWinningBets(winningBets);

        log.info("Lot completion job check is done.");
    }
}
