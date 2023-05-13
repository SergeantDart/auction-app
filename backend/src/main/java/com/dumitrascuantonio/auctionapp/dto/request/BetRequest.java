package com.dumitrascuantonio.auctionapp.dto.request;

import java.math.BigDecimal;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BetRequest {

    @PositiveOrZero
    private BigDecimal amount;

}
