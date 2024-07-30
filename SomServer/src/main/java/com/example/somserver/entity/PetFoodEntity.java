package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pet_foods")
public class PetFoodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_food_id", nullable = false)
    private int petFoodId; //pet_foods table -> pet_food_id : pk, not null, auto increment, INT

    @Column(name = "product_name", nullable = false, length = 40)
    private String productName; //pet_foods table -> product_name : not null, VARCHAR(40)

    @Column(name = "has_calcium", nullable = false)
    private Boolean hasCalcium; //pet_foods table -> has_calcium : not null, TINYINT(1)

    @Column(name = "has_protein", nullable = false)
    private Boolean hasProtein; //pet_foods table -> has_protein : not null, TINYINT(1)

    @Column(name = "has_lactobacilli", nullable = false)
    private Boolean hasLactobacilli; //pet_foods table -> has_lactobacilli : not null, TINYINT(1)

    @Column(name = "price", nullable = false)
    private Integer price; //pet_foods table -> price : not null, INT

    @Column(name = "brand", nullable = false, length = 10)
    private String brand; //pet_foods table -> brand : not null, VARCHAR(10)
}
