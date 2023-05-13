package com.dumitrascuantonio.auctionapp.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "winning_bet")
@Getter
@Setter
@NoArgsConstructor
public class WinningBet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @OneToOne
    private Bet bet;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}
