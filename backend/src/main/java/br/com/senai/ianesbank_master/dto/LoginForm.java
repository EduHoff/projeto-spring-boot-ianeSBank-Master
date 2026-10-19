package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
public record LoginForm(@NotBlank String login, @NotBlank String senha) {}
