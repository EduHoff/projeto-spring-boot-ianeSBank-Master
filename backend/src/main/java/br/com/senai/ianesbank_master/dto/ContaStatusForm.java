package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
public record ContaStatusForm(@NotBlank String status) {}
