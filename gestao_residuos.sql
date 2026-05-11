-- ============================================================
-- Projeto: Gestao de Residuos - Microsservicos
-- Banco: Oracle Database
-- Arquivo: schema_gestao_residuos.sql
--
-- Este script cria apenas as 3 tabelas principais do projeto:
-- 1. T_PONTO_COLETA  -> ponto-coleta-service
-- 2. T_RESIDUO       -> residuo-service
-- 3. T_COLETA        -> coleta-service
--
-- Nao cria tabelas de usuarios, alertas ou autenticacao.
-- ============================================================

-- ============================================================
-- SEQUENCES
-- ============================================================

CREATE SEQUENCE SEQ_PONTO_COLETA
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE SEQ_RESIDUO
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE SEQUENCE SEQ_COLETA
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- ============================================================
-- TABELA DO MICROSSERVICO: ponto-coleta-service
-- ============================================================

CREATE TABLE T_PONTO_COLETA (
    ID_PONTO_COLETA           NUMBER(19)      NOT NULL,
    NM_PONTO_COLETA           VARCHAR2(120)   NOT NULL,
    DS_ENDERECO               VARCHAR2(200)   NOT NULL,
    NM_CIDADE                 VARCHAR2(100)   NOT NULL,
    SG_ESTADO                 CHAR(2)         NOT NULL,
    NR_CEP                    VARCHAR2(10)    NOT NULL,
    NR_LATITUDE               NUMBER(10, 6),
    NR_LONGITUDE              NUMBER(10, 6),
    NR_CAPACIDADE_MAXIMA      NUMBER(12, 2)   NOT NULL,
    NR_CAPACIDADE_ATUAL       NUMBER(12, 2)   NOT NULL,
    DS_TIPO_RESIDUO_ACEITO    VARCHAR2(80)    NOT NULL,
    ST_PONTO_COLETA           VARCHAR2(20)    NOT NULL,
    DT_CRIACAO                TIMESTAMP       DEFAULT SYSTIMESTAMP NOT NULL,
    DT_ATUALIZACAO            TIMESTAMP,

    CONSTRAINT PK_PONTO_COLETA PRIMARY KEY (ID_PONTO_COLETA),
    CONSTRAINT CK_PONTO_COLETA_CAP_MAX CHECK (NR_CAPACIDADE_MAXIMA > 0),
    CONSTRAINT CK_PONTO_COLETA_CAP_ATUAL CHECK (NR_CAPACIDADE_ATUAL >= 0),
    CONSTRAINT CK_PONTO_COLETA_CAP_LIMITE CHECK (NR_CAPACIDADE_ATUAL <= NR_CAPACIDADE_MAXIMA),
    CONSTRAINT CK_PONTO_COLETA_STATUS CHECK (ST_PONTO_COLETA IN ('ATIVO', 'INATIVO', 'LOTADO'))
);

-- ============================================================
-- TABELA DO MICROSSERVICO: residuo-service
--
-- Relacionamento:
-- Cada residuo fica associado a um ponto de coleta.
-- ============================================================

CREATE TABLE T_RESIDUO (
    ID_RESIDUO                NUMBER(19)      NOT NULL,
    ID_PONTO_COLETA           NUMBER(19)      NOT NULL,
    NM_RESIDUO                VARCHAR2(100)   NOT NULL,
    DS_TIPO_RESIDUO           VARCHAR2(80)    NOT NULL,
    DS_RESIDUO                VARCHAR2(250),
    NR_QUANTIDADE             NUMBER(12, 2)   NOT NULL,
    DS_UNIDADE_MEDIDA         VARCHAR2(20)    DEFAULT 'KG' NOT NULL,
    ST_RESIDUO                VARCHAR2(20)    NOT NULL,
    DT_REGISTRO               TIMESTAMP       DEFAULT SYSTIMESTAMP NOT NULL,
    DT_ATUALIZACAO            TIMESTAMP,

    CONSTRAINT PK_RESIDUO PRIMARY KEY (ID_RESIDUO),
    CONSTRAINT FK_RESIDUO_PONTO_COLETA FOREIGN KEY (ID_PONTO_COLETA)
        REFERENCES T_PONTO_COLETA (ID_PONTO_COLETA),
    CONSTRAINT CK_RESIDUO_QUANTIDADE CHECK (NR_QUANTIDADE >= 0),
    CONSTRAINT CK_RESIDUO_STATUS CHECK (ST_RESIDUO IN ('ATIVO', 'INATIVO'))
);

-- ============================================================
-- TABELA DO MICROSSERVICO: coleta-service
--
-- Relacionamentos:
-- Cada coleta pertence a um ponto de coleta.
-- Cada coleta tambem indica o residuo coletado.
-- ============================================================

CREATE TABLE T_COLETA (
    ID_COLETA                 NUMBER(19)      NOT NULL,
    ID_PONTO_COLETA           NUMBER(19)      NOT NULL,
    ID_RESIDUO                NUMBER(19)      NOT NULL,
    DT_AGENDAMENTO            TIMESTAMP       NOT NULL,
    DT_COLETA                 TIMESTAMP,
    NR_QUANTIDADE_COLETADA    NUMBER(12, 2),
    ST_COLETA                 VARCHAR2(20)    NOT NULL,
    DS_OBSERVACAO             VARCHAR2(250),
    DT_CRIACAO                TIMESTAMP       DEFAULT SYSTIMESTAMP NOT NULL,
    DT_ATUALIZACAO            TIMESTAMP,

    CONSTRAINT PK_COLETA PRIMARY KEY (ID_COLETA),
    CONSTRAINT FK_COLETA_PONTO_COLETA FOREIGN KEY (ID_PONTO_COLETA)
        REFERENCES T_PONTO_COLETA (ID_PONTO_COLETA),
    CONSTRAINT FK_COLETA_RESIDUO FOREIGN KEY (ID_RESIDUO)
        REFERENCES T_RESIDUO (ID_RESIDUO),
    CONSTRAINT CK_COLETA_QTD CHECK (NR_QUANTIDADE_COLETADA IS NULL OR NR_QUANTIDADE_COLETADA >= 0),
    CONSTRAINT CK_COLETA_STATUS CHECK (ST_COLETA IN ('AGENDADA', 'EM_ANDAMENTO', 'REALIZADA', 'CANCELADA'))
);

-- ============================================================
-- INDICES PARA CONSULTAS E RELACIONAMENTOS
-- ============================================================

CREATE INDEX IDX_PONTO_COLETA_CIDADE
    ON T_PONTO_COLETA (NM_CIDADE);

CREATE INDEX IDX_PONTO_COLETA_STATUS
    ON T_PONTO_COLETA (ST_PONTO_COLETA);

CREATE INDEX IDX_PONTO_COLETA_TIPO_RESIDUO
    ON T_PONTO_COLETA (DS_TIPO_RESIDUO_ACEITO);

CREATE INDEX IDX_RESIDUO_PONTO_COLETA
    ON T_RESIDUO (ID_PONTO_COLETA);

CREATE INDEX IDX_RESIDUO_TIPO
    ON T_RESIDUO (DS_TIPO_RESIDUO);

CREATE INDEX IDX_RESIDUO_STATUS
    ON T_RESIDUO (ST_RESIDUO);

CREATE INDEX IDX_COLETA_PONTO_COLETA
    ON T_COLETA (ID_PONTO_COLETA);

CREATE INDEX IDX_COLETA_RESIDUO
    ON T_COLETA (ID_RESIDUO);

CREATE INDEX IDX_COLETA_STATUS
    ON T_COLETA (ST_COLETA);

-- ============================================================
-- DADOS INICIAIS OPCIONAIS PARA TESTE
-- ============================================================

INSERT INTO T_PONTO_COLETA (
    ID_PONTO_COLETA,
    NM_PONTO_COLETA,
    DS_ENDERECO,
    NM_CIDADE,
    SG_ESTADO,
    NR_CEP,
    NR_LATITUDE,
    NR_LONGITUDE,
    NR_CAPACIDADE_MAXIMA,
    NR_CAPACIDADE_ATUAL,
    DS_TIPO_RESIDUO_ACEITO,
    ST_PONTO_COLETA,
    DT_CRIACAO,
    DT_ATUALIZACAO
) VALUES (
    SEQ_PONTO_COLETA.NEXTVAL,
    'Ecoponto Vila Mariana',
    'Rua Domingos de Morais, 1000',
    'Sao Paulo',
    'SP',
    '04010-100',
    -23.589200,
    -46.634500,
    500.00,
    120.00,
    'PLASTICO',
    'ATIVO',
    SYSTIMESTAMP,
    NULL
);

INSERT INTO T_RESIDUO (
    ID_RESIDUO,
    ID_PONTO_COLETA,
    NM_RESIDUO,
    DS_TIPO_RESIDUO,
    DS_RESIDUO,
    NR_QUANTIDADE,
    DS_UNIDADE_MEDIDA,
    ST_RESIDUO,
    DT_REGISTRO,
    DT_ATUALIZACAO
) VALUES (
    SEQ_RESIDUO.NEXTVAL,
    1,
    'Garrafa plastica',
    'PLASTICO',
    'Residuo plastico reciclavel',
    120.00,
    'KG',
    'ATIVO',
    SYSTIMESTAMP,
    NULL
);

INSERT INTO T_COLETA (
    ID_COLETA,
    ID_PONTO_COLETA,
    ID_RESIDUO,
    DT_AGENDAMENTO,
    DT_COLETA,
    NR_QUANTIDADE_COLETADA,
    ST_COLETA,
    DS_OBSERVACAO,
    DT_CRIACAO,
    DT_ATUALIZACAO
) VALUES (
    SEQ_COLETA.NEXTVAL,
    1,
    1,
    SYSTIMESTAMP + INTERVAL '1' DAY,
    NULL,
    NULL,
    'AGENDADA',
    'Coleta inicial de teste',
    SYSTIMESTAMP,
    NULL
);

COMMIT;
