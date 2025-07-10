package com.example.recuperar_senha_service.dto;

public class ErroDTO {
    private String codigo;
    private String mensagem;

    public ErroDTO(String codigo, String mensagem) {
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
