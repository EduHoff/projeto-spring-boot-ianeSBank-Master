package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
public record EnderecoForm(@NotBlank String cep, @NotBlank String logradouro, @NotBlank String numero, String complemento, @NotBlank String bairro, @NotBlank String cidade, @NotBlank String uf) {}
