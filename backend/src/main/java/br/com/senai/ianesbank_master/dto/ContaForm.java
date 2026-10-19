package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
public record ContaForm(@NotNull Long clienteId, @NotNull Long agenciaId, @NotNull Long tipoContaId) {}
