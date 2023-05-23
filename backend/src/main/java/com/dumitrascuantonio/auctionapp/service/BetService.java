package com.dumitrascuantonio.auctionapp.service;

import com.dumitrascuantonio.auctionapp.dto.request.BetRequest;
import com.dumitrascuantonio.auctionapp.entity.Bet;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.entity.enumeration.Role;
import com.dumitrascuantonio.auctionapp.exception.BetCreateException;
import com.dumitrascuantonio.auctionapp.exception.BetDeleteException;
import com.dumitrascuantonio.auctionapp.exception.BetNotFoundException;
import com.dumitrascuantonio.auctionapp.exception.LotNotFoundException;
import com.dumitrascuantonio.auctionapp.repository.BetRepository;
import com.dumitrascuantonio.auctionapp.repository.LotRepository;
import com.dumitrascuantonio.auctionapp.repository.UserRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BetService {

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private UserRepository userRepository;


    public Bet createBet(Long lotId, BetRequest betRequest, Principal principal) {
        User user = getUserByPrincipal(principal);
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found for id: " + lotId));
        boolean isHighestBet = !lot.getBets()
                .stream()
                .filter(bet -> !bet.isArchival())
                .anyMatch(bet -> bet.getAmount().compareTo(betRequest.getAmount()) >= 0);
        boolean isHigherThanInitialCost = betRequest.getAmount().compareTo(lot.getInitCost()) >= 0;
        if (!lot.isActive()) {
            throw new BetCreateException("You cannot bet on an inactive lot.");
        } else if (!isHigherThanInitialCost || !isHighestBet) {
            throw new BetCreateException("You must bet higher.");
        } else if (lot.getUser().equals(user)) {
            throw new BetCreateException("You can't bid on your own lot.");
        }

        Bet bet = new Bet();
        bet.setAmount(betRequest.getAmount());
        bet.setCreatedDate(LocalDateTime.now());
        bet.setLot(lot);
        bet.setUserId(user.getId());
        return betRepository.save(bet);
    }

    public List<Bet> getAllBetsForLot(Long lotId) {
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found for id: " + lotId));
        return betRepository.findAllByLotAndArchivalIsFalse(lot);
    }

    public void deleteBet(Long betId, Principal principal) {
        User currentUser = getUserByPrincipal(principal);
        Bet bet = betRepository.findByIdAndArchivalIsFalse(betId)
                .orElseThrow(() -> new BetNotFoundException("Bet with id: " + betId + " cannot be found."));
        if (!bet.getLot().isActive()) {
           throw new BetDeleteException("Lot for bet with id: " + betId + " is not active.");
        }

        if (bet.getUserId() == currentUser.getId() || currentUser.getRoles().contains(Role.ROLE_ADMIN)) {
            bet.setArchival(true);
            betRepository.save(bet);
        } else {
            throw new AccessDeniedException("Only ADMIN user role can delete data that is not his/her own.");
        }
    }

    public Bet getBetById(Long betId) {
        return betRepository.findByIdAndArchivalIsFalse(betId)
                .orElseThrow(() -> new BetNotFoundException("Bet with id: " + betId + " cannot be found."));
    }


    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
    }

}
