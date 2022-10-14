package com.readingisgood.sale.api.domain.book;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewBookRequest {

    @NotEmpty
    private String name;
    @NotEmpty
    private String author;
    @NotEmpty
    private String genre;
    @org.hibernate.validator.constraints.ISBN
    private String ISBN;
    @PositiveOrZero
    private Long stockCount;
    @Positive
    private BigDecimal price;
}
