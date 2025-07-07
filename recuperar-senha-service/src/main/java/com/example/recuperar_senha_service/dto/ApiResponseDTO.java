package com.example.recuperar_senha_service.dto;

public record ApiResponseDTO(
        boolean success,
        String message,
        Object data
) {
    public static ApiResponseDTO success(String message) {
        return new ApiResponseDTO(true, message, null);
    }

    public static ApiResponseDTO success(String message, Object data) {
        return new ApiResponseDTO(true, message, data);
    }

    public static ApiResponseDTO error(String message) {
        return new ApiResponseDTO(false, message, null);
    }
}