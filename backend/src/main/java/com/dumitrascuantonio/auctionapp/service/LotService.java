package com.dumitrascuantonio.auctionapp.service;

import com.dumitrascuantonio.auctionapp.dto.request.LotRequest;
import com.dumitrascuantonio.auctionapp.entity.Bet;
import com.dumitrascuantonio.auctionapp.entity.Lot;
import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.entity.enumeration.Role;
import com.dumitrascuantonio.auctionapp.exception.LotCreateException;
import com.dumitrascuantonio.auctionapp.exception.LotDeleteException;
import com.dumitrascuantonio.auctionapp.exception.LotNotFoundException;
import com.dumitrascuantonio.auctionapp.repository.BetRepository;
import com.dumitrascuantonio.auctionapp.repository.LotRepository;
import com.dumitrascuantonio.auctionapp.repository.UserRepository;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LotService {

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private BetRepository betRepository;

    @Autowired
    private UserRepository userRepository;

    public Lot createLot(LotRequest lotRequest, Principal principal) {
        User user = getUserByPrincipal(principal);
        Lot lot = new Lot();
        lot.setTitle(lotRequest.getTitle());
        lot.setDescription(lotRequest.getDescription());
        lot.setInitCost(lotRequest.getInitCost());

        if (LocalDateTime.now().isBefore(lotRequest.getEndDate())) {
           lot.setStartDate(LocalDateTime.now());
           lot.setEndDate(lotRequest.getEndDate());
        } else {
            throw new LotCreateException("Start date must be after end date.");
        }

        lot.setBets(null);
        lot.setUser(user);
        lot.setActive(true);
        lot.setArchival(false);

        return lotRepository.save(lot);
    }

    public List<Lot> saveLots(List<Lot> lots) {
        return lotRepository.saveAll(lots);
    }

    public List<Lot> getAllLots(boolean isActive) {
        return lotRepository.findAllByActiveAndArchivalIsFalse(isActive);
    }

    public Lot getLotById(Long lotId) {
        return lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot cannot be found for id: " + lotId));
    }

    public List<Lot> getAllLotsForUser(Principal principal, boolean active) {
        User user = getUserByPrincipal(principal);
        return lotRepository.findAllByUserAndActiveAndArchivalIsFalseOrderByStartDateDesc(user, active);
    }

    public List<Lot> getLotsUserParticipatedIn(Principal principal, boolean active) {
        User user = getUserByPrincipal(principal);
        return lotRepository.findDistinctLotByUserIdAndArchivalIsFalse(user.getId())
                .stream()
                .filter(lot -> lot.isActive() == active)
                .collect(Collectors.toList());
    }

    public List<Lot> getFirst10Lots() {
        return lotRepository.findFirst10ByActiveIsTrueAndArchivalIsFalseOrderByStartDateDesc();
    }

    public void deleteLot (Principal principal, Long lotId) {
        User currentUser = getUserByPrincipal(principal);
        Lot lot = lotRepository.findByIdAndArchivalIsFalse(lotId)
                .orElseThrow(() -> new LotNotFoundException("Lot with id: " + lotId +  " cannot be found."));

        if (!lot.isActive()) {
           throw new LotDeleteException("Lot is not active.");
        }

        if (lot.getUser().equals(currentUser) || currentUser.getRoles().contains(Role.ROLE_ADMIN)) {
            lot.setArchival(true);
            List<Bet> modifiedBets = new ArrayList<>();
            lot.getBets()
                    .forEach(bet -> {
                        bet.setArchival(true);
                        modifiedBets.add(bet);
                    });
            betRepository.saveAll(modifiedBets);
            lotRepository.save(lot);
        } else {
            throw new AccessDeniedException("Only ADMIN user role can delete data that is not his/her own.");
        }
    }

    public List<Lot> getLotsByKeyword(String keyword) {
        return lotRepository.findAllByTitleContainsAndArchivalIsFalse(keyword);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
    }

}
