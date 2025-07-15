package com.example.setor_service.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "setores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String usuarioId;
}
