package com.example.gastos_service.dto;

public class ErrorDTO {
    private String codigo;
    private String mensagem;

    public ErrorDTO(String codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
