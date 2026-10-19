package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
public record UsuarioForm(Long clienteId, @NotBlank String perfil, @NotBlank String login, String senha, Boolean ativo) {}
