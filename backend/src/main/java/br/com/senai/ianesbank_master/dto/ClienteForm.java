package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
public record ClienteForm(@NotBlank String nome, @NotBlank @Pattern(regexp="\\d{11}",message="CPF deve conter 11 digitos") String cpf, @NotBlank @Email String email, String telefone, Boolean ativo, @Valid EnderecoForm endereco) {}
