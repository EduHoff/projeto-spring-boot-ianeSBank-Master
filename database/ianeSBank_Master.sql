-- ============================================================
-- SEN A I B A N K - BANCO DE DADOS DIDATICO
-- Spring Boot + Angular + MariaDB 10.4.32 (XAMPP)
-- ============================================================
-- ATENCAO: este script RECRIA o banco ianeSBank Master.
-- Todos os dados anteriores de ianeSBank Master serao apagados.
-- Execute somente na primeira instalacao ou para reiniciar
-- intencionalmente a base de desenvolvimento.
-- As senhas dos USUARIOS DO SISTEMA abaixo sao demonstrativas.
-- Elas NAO sao as senhas do usuario root do MariaDB.
-- ============================================================

DROP DATABASE IF EXISTS `ianeSBank-Master`;
CREATE DATABASE `ianeSBank-Master`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE `ianeSBank-Master`;

-- 1. CLIENTE
CREATE TABLE cliente (
    id_cliente BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(150) NOT NULL,
    cpf CHAR(11) NOT NULL,
    email VARCHAR(150) NOT NULL,
    telefone VARCHAR(20) NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_cliente),
    UNIQUE KEY uk_cliente_cpf (cpf)
) ENGINE=InnoDB;

-- 2. ENDERECO: um endereco principal por cliente
CREATE TABLE endereco (
    id_endereco BIGINT NOT NULL AUTO_INCREMENT,
    id_cliente BIGINT NOT NULL,
    cep CHAR(8) NOT NULL,
    logradouro VARCHAR(150) NOT NULL,
    numero VARCHAR(15) NOT NULL,
    complemento VARCHAR(100) NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2) NOT NULL,
    PRIMARY KEY (id_endereco),
    UNIQUE KEY uk_endereco_cliente (id_cliente),
    CONSTRAINT fk_endereco_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
        ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB;

-- 3. AGENCIA
CREATE TABLE agencia (
    id_agencia BIGINT NOT NULL AUTO_INCREMENT,
    numero VARCHAR(10) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    uf CHAR(2) NOT NULL,
    ativa TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_agencia),
    UNIQUE KEY uk_agencia_numero (numero)
) ENGINE=InnoDB;

-- 4. TIPO_CONTA
CREATE TABLE tipo_conta (
    id_tipo_conta BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(30) NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (id_tipo_conta),
    UNIQUE KEY uk_tipo_conta_codigo (codigo)
) ENGINE=InnoDB;

-- 5. PERFIL
CREATE TABLE perfil (
    id_perfil BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL,
    descricao VARCHAR(100) NOT NULL,
    PRIMARY KEY (id_perfil),
    UNIQUE KEY uk_perfil_codigo (codigo),
    CONSTRAINT ck_perfil_codigo CHECK (codigo IN ('ADMIN', 'CLIENTE'))
) ENGINE=InnoDB;

-- 6. CONTA
CREATE TABLE conta (
    id_conta BIGINT NOT NULL AUTO_INCREMENT,
    id_cliente BIGINT NOT NULL,
    id_agencia BIGINT NOT NULL,
    id_tipo_conta BIGINT NOT NULL,
    numero VARCHAR(20) NOT NULL,
    saldo DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVA',
    versao BIGINT NOT NULL DEFAULT 0,
    data_abertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_conta),
    UNIQUE KEY uk_conta_numero (numero),
    KEY ix_conta_cliente (id_cliente),
    KEY ix_conta_agencia (id_agencia),
    KEY ix_conta_tipo (id_tipo_conta),
    CONSTRAINT fk_conta_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_conta_agencia FOREIGN KEY (id_agencia)
        REFERENCES agencia (id_agencia)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_conta_tipo FOREIGN KEY (id_tipo_conta)
        REFERENCES tipo_conta (id_tipo_conta)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT ck_conta_saldo CHECK (saldo >= 0),
    CONSTRAINT ck_conta_status CHECK
        (status IN ('ATIVA', 'BLOQUEADA', 'ENCERRADA'))
) ENGINE=InnoDB;

-- 7. TRANSACAO: operacao financeira completa
CREATE TABLE transacao (
    id_transacao BIGINT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(36) NOT NULL,
    id_conta_origem BIGINT NULL,
    id_conta_destino BIGINT NULL,
    tipo VARCHAR(20) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONCLUIDA',
    descricao VARCHAR(255) NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_transacao),
    UNIQUE KEY uk_transacao_codigo (codigo),
    KEY ix_transacao_origem (id_conta_origem),
    KEY ix_transacao_destino (id_conta_destino),
    KEY ix_transacao_data (data_hora),
    CONSTRAINT fk_transacao_origem FOREIGN KEY (id_conta_origem)
        REFERENCES conta (id_conta)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_transacao_destino FOREIGN KEY (id_conta_destino)
        REFERENCES conta (id_conta)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT ck_transacao_valor CHECK (valor > 0),
    CONSTRAINT ck_transacao_status CHECK
        (status IN ('PENDENTE', 'CONCLUIDA', 'FALHA')),
    CONSTRAINT ck_transacao_contas CHECK (
        (tipo = 'DEPOSITO' AND id_conta_origem IS NULL
            AND id_conta_destino IS NOT NULL)
        OR
        (tipo = 'SAQUE' AND id_conta_origem IS NOT NULL
            AND id_conta_destino IS NULL)
        OR
        (tipo = 'TRANSFERENCIA' AND id_conta_origem IS NOT NULL
            AND id_conta_destino IS NOT NULL
            AND id_conta_origem <> id_conta_destino)
    )
) ENGINE=InnoDB;

-- 8. MOVIMENTACAO: um credito ou debito em uma conta
CREATE TABLE movimentacao (
    id_movimentacao BIGINT NOT NULL AUTO_INCREMENT,
    id_transacao BIGINT NOT NULL,
    id_conta BIGINT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    valor DECIMAL(15,2) NOT NULL,
    saldo_anterior DECIMAL(15,2) NOT NULL,
    saldo_posterior DECIMAL(15,2) NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_movimentacao),
    UNIQUE KEY uk_movimentacao_transacao_conta (id_transacao, id_conta),
    KEY ix_movimentacao_conta_data (id_conta, data_hora),
    CONSTRAINT fk_movimentacao_transacao FOREIGN KEY (id_transacao)
        REFERENCES transacao (id_transacao)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_movimentacao_conta FOREIGN KEY (id_conta)
        REFERENCES conta (id_conta)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT ck_movimentacao_valores CHECK (
        valor > 0 AND saldo_anterior >= 0 AND saldo_posterior >= 0
    ),
    CONSTRAINT ck_movimentacao_tipo_saldo CHECK (
        (tipo = 'CREDITO' AND saldo_posterior = saldo_anterior + valor)
        OR
        (tipo = 'DEBITO' AND saldo_posterior = saldo_anterior - valor)
    )
) ENGINE=InnoDB;

-- 9. USUARIO: login separado dos dados de cliente
CREATE TABLE usuario (
    id_usuario BIGINT NOT NULL AUTO_INCREMENT,
    id_cliente BIGINT NULL,
    id_perfil BIGINT NOT NULL,
    login VARCHAR(80) NOT NULL,
    senha_hash VARCHAR(100) NOT NULL,
    ativo TINYINT(1) NOT NULL DEFAULT 1,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultimo_acesso DATETIME NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_login (login),
    UNIQUE KEY uk_usuario_cliente (id_cliente),
    KEY ix_usuario_perfil (id_perfil),
    CONSTRAINT fk_usuario_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente (id_cliente)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_usuario_perfil FOREIGN KEY (id_perfil)
        REFERENCES perfil (id_perfil)
        ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB;

-- ============================================================
-- DADOS INICIAIS FICTICIOS (AMBIENTE DIDATICO)
-- ============================================================

INSERT INTO agencia (id_agencia, numero, nome, cidade, uf, ativa) VALUES
(1, '0001', 'Agencia SENAI Londrina', 'Londrina', 'PR', 1),
(2, '0002', 'Agencia SENAI Maringa', 'Maringa', 'PR', 1);

INSERT INTO tipo_conta (id_tipo_conta, codigo, descricao, ativo) VALUES
(1, 'CORRENTE', 'Conta corrente', 1),
(2, 'POUPANCA', 'Conta poupanca', 1);

INSERT INTO perfil (id_perfil, codigo, descricao) VALUES
(1, 'ADMIN', 'Administrador do banco'),
(2, 'CLIENTE', 'Cliente do banco');

-- CPFs apenas para ilustrar o exercicio, sem identificar pessoas reais.
INSERT INTO cliente
(id_cliente, nome, cpf, email, telefone, ativo) VALUES
(1, 'Ana Exemplo', '12345678909', 'ana@example.com', '43999990001', 1),
(2, 'Bruno Exemplo', '23456789092', 'bruno@example.com', '43999990002', 1),
(3, 'Carla Exemplo', '34567890175', 'carla@example.com', '43999990003', 1),
(4, 'Diego Exemplo', '45678901249', 'diego@example.com', '43999990004', 1);

INSERT INTO endereco
(id_endereco, id_cliente, cep, logradouro, numero, complemento, bairro, cidade, uf) VALUES
(1, 1, '86010000', 'Rua Exemplo A', '100', NULL, 'Centro', 'Londrina', 'PR'),
(2, 2, '86020000', 'Rua Exemplo B', '200', 'Casa', 'Centro', 'Londrina', 'PR'),
(3, 3, '87010000', 'Rua Exemplo C', '300', NULL, 'Centro', 'Maringa', 'PR'),
(4, 4, '86030000', 'Rua Exemplo D', '400', NULL, 'Centro', 'Londrina', 'PR');

-- Saldos abaixo correspondem exatamente as movimentacoes iniciais.
-- Ana: +1500 -200 = 1300
-- Bruno: +800 +200 -100 = 900
-- Carla: +500 = 500
-- Diego: sem movimentacao e conta bloqueada = 0
INSERT INTO conta
(id_conta, id_cliente, id_agencia, id_tipo_conta, numero, saldo, status) VALUES
(1, 1, 1, 1, '100001', 1300.00, 'ATIVA'),
(2, 2, 1, 1, '100002', 900.00, 'ATIVA'),
(3, 3, 2, 2, '100003', 500.00, 'ATIVA'),
(4, 4, 1, 1, '100004', 0.00, 'BLOQUEADA');

-- Cada transacao financeira e um evento de negocio.
INSERT INTO transacao
(id_transacao, codigo, id_conta_origem, id_conta_destino,
 tipo, valor, status, descricao, data_hora) VALUES
(1, 'DEMO-000001', NULL, 1, 'DEPOSITO', 1500.00, 'CONCLUIDA',
 'Deposito inicial da Ana', '2026-10-01 09:00:00'),
(2, 'DEMO-000002', NULL, 2, 'DEPOSITO', 800.00, 'CONCLUIDA',
 'Deposito inicial do Bruno', '2026-10-01 09:30:00'),
(3, 'DEMO-000003', 1, 2, 'TRANSFERENCIA', 200.00, 'CONCLUIDA',
 'Transferencia Ana para Bruno', '2026-10-02 14:00:00'),
(4, 'DEMO-000004', 2, NULL, 'SAQUE', 100.00, 'CONCLUIDA',
 'Saque realizado por Bruno', '2026-10-03 10:00:00'),
(5, 'DEMO-000005', NULL, 3, 'DEPOSITO', 500.00, 'CONCLUIDA',
 'Deposito inicial da Carla', '2026-10-03 11:00:00');

-- A transferencia 3 gera DUAS movimentacoes vinculadas.
INSERT INTO movimentacao
(id_movimentacao, id_transacao, id_conta, tipo, valor,
 saldo_anterior, saldo_posterior, data_hora) VALUES
(1, 1, 1, 'CREDITO', 1500.00, 0.00, 1500.00, '2026-10-01 09:00:00'),
(2, 2, 2, 'CREDITO', 800.00, 0.00, 800.00, '2026-10-01 09:30:00'),
(3, 3, 1, 'DEBITO', 200.00, 1500.00, 1300.00, '2026-10-02 14:00:00'),
(4, 3, 2, 'CREDITO', 200.00, 800.00, 1000.00, '2026-10-02 14:00:00'),
(5, 4, 2, 'DEBITO', 100.00, 1000.00, 900.00, '2026-10-03 10:00:00'),
(6, 5, 3, 'CREDITO', 500.00, 0.00, 500.00, '2026-10-03 11:00:00');

-- Login e senhas DO SISTEMA IaneSBank Master (somente desenvolvimento):
-- admin / Admin@1234
-- ana   / Cliente@1234
-- bruno / Cliente@1234
-- Hashes BCrypt reais, compativeis com BCryptPasswordEncoder.
-- Altere estas credenciais antes de qualquer ambiente compartilhado.
INSERT INTO usuario
(id_usuario, id_cliente, id_perfil, login, senha_hash, ativo) VALUES
(1, NULL, 1, 'admin',
 '$2y$12$786DSvJ35awXAFaRfup4XeTdzgG6sv/3xUGvq/3cFkIFxSZXivRwm', 1),
(2, 1, 2, 'ana',
 '$2y$12$55TTXq.2lZeoxoIWLH3qw.fDmmaH9l3/RuoKX/vMhy8ujrNDXBrhe', 1),
(3, 2, 2, 'bruno',
 '$2y$12$55TTXq.2lZeoxoIWLH3qw.fDmmaH9l3/RuoKX/vMhy8ujrNDXBrhe', 1);

-- ============================================================
-- CONSULTAS DE CONFERENCIA (NAO MODIFICAM DADOS)
-- ============================================================
SELECT 'CLIENTES' AS tabela, COUNT(*) AS quantidade FROM cliente
UNION ALL SELECT 'ENDERECOS', COUNT(*) FROM endereco
UNION ALL SELECT 'AGENCIAS', COUNT(*) FROM agencia
UNION ALL SELECT 'TIPOS_CONTA', COUNT(*) FROM tipo_conta
UNION ALL SELECT 'CONTAS', COUNT(*) FROM conta
UNION ALL SELECT 'TRANSACOES', COUNT(*) FROM transacao
UNION ALL SELECT 'MOVIMENTACOES', COUNT(*) FROM movimentacao
UNION ALL SELECT 'USUARIOS', COUNT(*) FROM usuario
UNION ALL SELECT 'PERFIS', COUNT(*) FROM perfil;

SELECT c.numero AS conta, cl.nome AS titular, c.status, c.saldo
FROM conta c
INNER JOIN cliente cl ON cl.id_cliente = c.id_cliente
ORDER BY c.numero;
