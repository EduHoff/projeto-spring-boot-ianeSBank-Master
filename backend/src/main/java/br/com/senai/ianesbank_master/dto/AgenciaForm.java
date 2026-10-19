package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
public record AgenciaForm(@NotBlank String numero, @NotBlank String nome, @NotBlank String cidade, @NotBlank String uf, Boolean ativa) {}
