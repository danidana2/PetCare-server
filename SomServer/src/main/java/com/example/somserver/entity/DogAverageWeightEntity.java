package com.example.somserver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dog_average_weights")
public class DogAverageWeightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_average_weight_id", nullable = false)
    private int dogAverageWeightId; //dog_average_weights table -> dog_average_weight_id : pk, not null, auto increment, INT

    @Column(name = "dog_breed", nullable = false, length = 6)
    private String dogBreed; //dog_average_weights table -> dog_breed : not null, VARCHAR(6)

    @Column(name = "dog_gender", nullable = false, length = 1)
    private Character dogGender; //dog_average_weights table -> dog_gender : not null, CHAR(1)

    @Column(name = "dog_average_weight_min", nullable = false, precision = 3, scale = 1)
    private BigDecimal dogAverageWeightMin; //dog_average_weights table -> dog_average_weight_min : not null, DECIMAL(3,1)

    @Column(name = "dog_average_weight_max", nullable = false, precision = 3, scale = 1)
    private BigDecimal dogAverageWeightMax; //dog_average_weights table -> dog_average_weight_max : not null, DECIMAL(3,1)
}
