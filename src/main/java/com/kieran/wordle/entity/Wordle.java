package com.kieran.wordle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

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
    @Builder.Default
    private Integer attempts = 6;
    @Builder.Default
    private Integer winners = 0;
    @Builder.Default
    private String timeLimit = "none";
    @Builder.Default
    boolean greenTilesOnly = false;
    private List<String> hints;
    @Builder.Default
    private UUID uuidLink = UUID.randomUUID();
}
