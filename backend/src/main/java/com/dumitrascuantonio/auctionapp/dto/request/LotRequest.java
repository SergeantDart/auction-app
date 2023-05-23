package com.dumitrascuantonio.auctionapp.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class LotRequest {

    @NotBlank(message = "Please, enter title.")
    private String title;

    @NotBlank(message = "Please, enter description.")
    private String description;

    @Future(message = "It must be a date in the future.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    @PositiveOrZero(message = "Cost cannot be less than zero.")
    private BigDecimal initCost;

}
