package com.flowershop.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
public class Flower {

    @Id
    private UUID id;
    private String slug;
    private String name;
    private String description;
    private Double price;
    private String image;

    @Singular
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "flower_owners",
            joinColumns = @JoinColumn(name = "flower_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> owners;

}