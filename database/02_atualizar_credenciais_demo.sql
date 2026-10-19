-- Use uma vez se voce ja importou a versao anterior do SQL.
-- NAO apaga nenhum cliente, conta, movimento ou transacao.
-- Reposiciona apenas as senhas iniciais DEMONSTRATIVAS.
-- IaneSBank Master didatico local. Altere senhas antes de publicar.
USE ianeSBank_Master;
UPDATE usuario SET senha_hash = '$2y$12$786DSvJ35awXAFaRfup4XeTdzgG6sv/3xUGvq/3cFkIFxSZXivRwm' WHERE login = 'admin';
UPDATE usuario SET senha_hash = '$2y$12$55TTXq.2lZeoxoIWLH3qw.fDmmaH9l3/RuoKX/vMhy8ujrNDXBrhe' WHERE login IN ('ana','bruno');
