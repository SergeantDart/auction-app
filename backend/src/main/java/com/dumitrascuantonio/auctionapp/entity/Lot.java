package com.dumitrascuantonio.auctionapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(updatable = false)
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private BigDecimal initCost;

    private boolean active;

    private boolean archival;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "lot", orphanRemoval = true)
    private List<Bet> bets = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.startDate = LocalDateTime.now();
    }

}
