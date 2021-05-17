package com.flowershop.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity(name = "flowerProduct")
public class FlowerProduct {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "flower_id")
    private Flower flower;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private ShoppingCart shoppingCart;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User holder;

    private Integer amount;

    public Double getTotal() {
        return flower.getPrice() * amount;
    }
}
