package com.resitplatform.domain.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
@Entity
public class Resit {

    @Id
    private UUID id;
    private String slug;
    private String name;
    private String description;
    private String image;

    private Boolean startDate;
    private Boolean hasEnded;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User responsibleTeacher;

    @Singular
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "resit_participants",
            joinColumns = @JoinColumn(name = "resit_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants;

}