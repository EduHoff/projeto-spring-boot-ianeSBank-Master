package br.com.senai.ianesbank_master.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record OperacaoForm(@NotNull Long contaId, @NotNull @DecimalMin(value="0.01") BigDecimal valor, String descricao) {}
