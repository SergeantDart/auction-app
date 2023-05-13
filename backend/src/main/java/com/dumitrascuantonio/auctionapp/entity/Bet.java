package com.dumitrascuantonio.auctionapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.TimeZone;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Lot lot;

    @Column(nullable = false)
    private Long userId;

    private BigDecimal amount;

    private LocalDateTime createdDate;

    private boolean archival;

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
    }

}
