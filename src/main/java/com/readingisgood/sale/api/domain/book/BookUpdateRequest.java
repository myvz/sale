package com.readingisgood.sale.api.domain.book;


import lombok.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateRequest {

    @PositiveOrZero
    private Long newStockCount;

    @Positive
    private BigDecimal price;
}
