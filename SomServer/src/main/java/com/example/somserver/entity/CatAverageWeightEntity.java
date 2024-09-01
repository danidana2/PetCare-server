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
@Table(name = "cat_average_weights")
public class CatAverageWeightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_average_weight_id", nullable = false)
    private int catAverageWeightId; //cat_average_weights table -> cat_average_weight_id : pk, not null, auto increment, INT

    @Column(name = "cat_breed", nullable = false, length = 6)
    private String catBreed; //cat_average_weights table -> cat_breed : not null, VARCHAR(6)

    @Column(name = "cat_average_weight_min", nullable = false, precision = 3, scale = 1)
    private BigDecimal catAverageWeightMin; //cat_average_weights table -> cat_average_weight_min : not null, DECIMAL(3,1)

    @Column(name = "cat_average_weight_max", nullable = false, precision = 3, scale = 1)
    private BigDecimal catAverageWeightMax; //cat_average_weights table -> cat_average_weight_max : not null, DECIMAL(3,1)
}
