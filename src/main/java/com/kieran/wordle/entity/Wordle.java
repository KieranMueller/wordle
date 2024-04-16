package com.kieran.wordle.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(hidden = true)
    private Long id;
    @Schema(hidden = true)
    private Long ownerId;
    private String word;
    @Builder.Default
    private Integer attempts = 6;
    @Builder.Default
    private String timeLimit = "none";
    @Builder.Default
    @Schema(hidden = true)
    private UUID uuidLink = UUID.randomUUID();
}
