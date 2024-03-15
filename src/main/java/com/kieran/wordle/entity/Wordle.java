package com.kieran.wordle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "wordle_table")
public class Wordle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long ownerId;
    private String word;
    private Integer attempts;
    private Integer winners;
    private Integer timeLimit;
    private List<String> hints;
}
