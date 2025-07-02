package com.controlegastos.authservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String nome;
    private String email;
    private String senha;
}
