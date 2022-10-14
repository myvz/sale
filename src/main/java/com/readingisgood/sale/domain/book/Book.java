package com.readingisgood.sale.domain.book;

import com.readingisgood.sale.domain.entity.BaseAuditingEntity;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book extends BaseAuditingEntity {

    @Basic(optional = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String author;

    @NotEmpty
    private String genre;

    @org.hibernate.validator.constraints.ISBN
    @Column(name = "isbn")
    private String ISBN;

    private Long stockCount;

    @Version
    private Integer version;

    @Column(name = "price")
    private BigDecimal price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return ISBN.equals(book.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }
}
