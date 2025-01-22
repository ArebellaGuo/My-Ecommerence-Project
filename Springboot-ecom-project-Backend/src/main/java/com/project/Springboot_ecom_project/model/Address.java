package com.project.Springboot_ecom_project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *  @Data :
 *      A convenience annotation that combines several annotations:
 *      @Getter, @Setter, @ToString, @EqualsAndHashCode, and
 *      @RequiredArgsConstructor:Generates a constructor for fields that are final or marked with @NonNull
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "addresses")
@EqualsAndHashCode(exclude = {"user"})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    private String addressName;
    private String eirCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
