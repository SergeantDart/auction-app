package com.dumitrascuantonio.auctionapp.service;

import com.dumitrascuantonio.auctionapp.entity.User;
import com.dumitrascuantonio.auctionapp.entity.WinningBet;
import com.dumitrascuantonio.auctionapp.repository.UserRepository;
import com.dumitrascuantonio.auctionapp.repository.WinningBetRepository;
import java.security.Principal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WinningBetService {

    @Autowired
    private WinningBetRepository winningBetRepository;

    @Autowired
    private UserRepository userRepository;


    public List<WinningBet> saveWinningBets(List<WinningBet> winningBets) {
        return winningBetRepository.saveAll(winningBets);
    }

    public List<WinningBet> getAllWinningBetsForUser(Principal principal) {
        User user = getUserByPrincipal(principal);
        return winningBetRepository.findAllByBetUserId(user.getId());
    }

    public User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name: " + username));
    }

}
