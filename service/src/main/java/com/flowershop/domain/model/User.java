
package com.flowershop.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class User {

    @EqualsAndHashCode.Include
    @Id
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String bio;
    private String image;
    private Boolean is_owner;

}
