package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
public record TipoContaForm(@NotBlank String codigo, @NotBlank String descricao, Boolean ativo) {}
