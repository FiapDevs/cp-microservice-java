# CP Eco Microservice

Projeto desenvolvido em Java com Spring Boot para o tema ESG **Gestao de residuos e reciclagem**. A solucao organiza o rastreamento de pontos de coleta, residuos cadastrados e agendamentos de coleta seletiva.

## Tema Escolhido

**Tema 2 - Gestao de residuos e reciclagem**

O produto minimo viavel atende aos seguintes pontos do enunciado:

- Rastreamento da coleta seletiva e descarte correto de residuos.
- Alertas automaticos para coleta de materiais reciclaveis quando o limite de capacidade e atingido ou esta proximo.
- Orientacoes para usuarios sobre a destinacao correta dos residuos.

## Arquitetura

O projeto foi dividido em tres microsservicos:

| Servico | Porta | Responsabilidade |
| --- | --- | --- |
| `ponto-coleta-service` | `8081` | Cadastro, consulta, atualizacao, remocao e alertas de capacidade dos pontos de coleta. |
| `residuo-service` | `8082` | Cadastro, consulta, atualizacao, remocao e orientacao de descarte de residuos. |
| `coleta-service` | `8083` | Agendamento, consulta, atualizacao, cancelamento e resumo operacional das coletas. |

Cada microsservico possui sua propria aplicacao Spring Boot, sua migration Flyway e sua tabela de historico do Flyway. As referencias entre servicos sao feitas por IDs, mantendo o baixo acoplamento entre os dominios.

## Tecnologias

- Java 21
- Spring Boot 3.3.5
- Spring Web
- Spring Data JPA
- Bean Validation
- Spring Security
- Flyway
- Oracle Database
- Docker e Docker Compose
- Maven

## Banco De Dados

O banco obrigatorio utilizado e Oracle Database. O projeto esta configurado para usar o Oracle da FIAP, com os dados definidos no `docker-compose.yml`.

Credenciais configuradas no compose:

```text
Usuario: rm565537
Senha: 050607
Host: oracle.fiap.com.br
Porta: 1521
SID: ORCL
JDBC URL: jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
```

## Acesso Pelo Oracle SQL Developer

Para acessar pelo Oracle SQL Developer, crie uma nova conexao com os seguintes dados:

```text
Nome da conexao: Fiap_vitor
Tipo de banco de dados: Oracle
Tipo de autenticacao: Padrao
Usuario: rm565537
Senha: 050607
Tipo de conexao: Basico
Hostname: oracle.fiap.com.br
Porta: 1521
SID: ORCL
```

No SQL Developer, selecione a opcao **SID** e preencha `ORCL`. Nao use **Nome do Servico** para essa conexao.

Cada microsservico usa uma tabela propria de controle do Flyway:

| Servico | Tabela Flyway |
| --- | --- |
| `ponto-coleta-service` | `FLYWAY_PONTO_COLETA_SCHEMA_HISTORY` |
| `residuo-service` | `FLYWAY_RESIDUO_SCHEMA_HISTORY` |
| `coleta-service` | `FLYWAY_COLETA_SCHEMA_HISTORY` |

## Como Executar Com Docker Compose

Na raiz do projeto, execute:

```bash
docker compose up --build
```

Para parar os containers:

```bash
docker compose down
```

## Seguranca

Os endpoints de consulta `GET` sao publicos. Os endpoints de escrita `POST`, `PUT` e `DELETE` exigem autenticacao HTTP Basic.

Credenciais padrao:

```text
Usuario: admin
Senha: admin123
```

Exemplo:

```bash
curl -u admin:admin123 -X POST http://localhost:8081/api/pontos-coleta \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ecoponto Vila Mariana","endereco":"Rua Domingos de Morais, 1000","cidade":"Sao Paulo","estado":"SP","cep":"04010-100","latitude":-23.5892,"longitude":-46.6345,"capacidadeMaxima":500.0,"capacidadeAtual":120.0,"tipoResiduoAceito":"PLASTICO","status":"ATIVO"}'
```

## Endpoints Principais

### Ponto De Coleta

Base URL: `http://localhost:8081/api/pontos-coleta`

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `POST` | `/api/pontos-coleta` | Criar ponto de coleta. |
| `GET` | `/api/pontos-coleta` | Listar todos os pontos de coleta. |
| `GET` | `/api/pontos-coleta/{id}` | Buscar ponto de coleta por ID. |
| `PUT` | `/api/pontos-coleta/{id}` | Atualizar ponto de coleta. |
| `DELETE` | `/api/pontos-coleta/{id}` | Remover ponto de coleta. |
| `GET` | `/api/pontos-coleta/cidade/{cidade}` | Buscar pontos por cidade. |
| `GET` | `/api/pontos-coleta/status/{status}` | Buscar pontos por status. |
| `GET` | `/api/pontos-coleta/tipo-residuo/{tipoResiduo}` | Buscar pontos pelo tipo de residuo aceito. |
| `GET` | `/api/pontos-coleta/alertas-capacidade?percentualMinimo=80` | Listar pontos proximos do limite de capacidade. |

### Residuo

Base URL: `http://localhost:8082/api/residuos`

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `POST` | `/api/residuos` | Criar residuo. |
| `GET` | `/api/residuos` | Listar todos os residuos. |
| `GET` | `/api/residuos/{id}` | Buscar residuo por ID. |
| `PUT` | `/api/residuos/{id}` | Atualizar residuo. |
| `DELETE` | `/api/residuos/{id}` | Remover residuo. |
| `GET` | `/api/residuos/ponto-coleta/{idPontoColeta}` | Buscar residuos por ponto de coleta. |
| `GET` | `/api/residuos/tipo/{tipoResiduo}` | Buscar residuos por tipo. |
| `GET` | `/api/residuos/status/{status}` | Buscar residuos por status. |
| `GET` | `/api/residuos/orientacao-descarte/{tipoResiduo}` | Consultar orientacao de descarte correto. |

### Coleta

Base URL: `http://localhost:8083/api/coletas`

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| `POST` | `/api/coletas` | Criar agendamento de coleta. |
| `GET` | `/api/coletas` | Listar todas as coletas. |
| `GET` | `/api/coletas/{id}` | Buscar coleta por ID. |
| `PUT` | `/api/coletas/{id}` | Atualizar coleta. |
| `DELETE` | `/api/coletas/{id}` | Remover ou cancelar coleta. |
| `GET` | `/api/coletas/ponto-coleta/{idPontoColeta}` | Buscar coletas por ponto de coleta. |
| `GET` | `/api/coletas/residuo/{idResiduo}` | Buscar coletas por residuo. |
| `GET` | `/api/coletas/status/{status}` | Buscar coletas por status. |
| `GET` | `/api/coletas/resumo` | Gerar resumo operacional das coletas. |

## Exemplos De Payload

### Criar Ponto De Coleta

```json
{
  "nome": "Ecoponto Vila Mariana",
  "endereco": "Rua Domingos de Morais, 1000",
  "cidade": "Sao Paulo",
  "estado": "SP",
  "cep": "04010-100",
  "latitude": -23.5892,
  "longitude": -46.6345,
  "capacidadeMaxima": 500.0,
  "capacidadeAtual": 120.0,
  "tipoResiduoAceito": "PLASTICO",
  "status": "ATIVO"
}
```

### Criar Residuo

```json
{
  "idPontoColeta": 1,
  "nome": "Garrafa plastica",
  "tipoResiduo": "PLASTICO",
  "descricao": "Residuo plastico reciclavel",
  "quantidade": 120.0,
  "unidadeMedida": "KG",
  "status": "ATIVO"
}
```

### Criar Coleta

```json
{
  "idPontoColeta": 1,
  "idResiduo": 1,
  "dataAgendamento": "2026-05-20T10:00:00",
  "dataColeta": null,
  "quantidadeColetada": null,
  "status": "AGENDADA",
  "observacao": "Coleta seletiva semanal"
}
```

## Validacoes E Tratamento De Erros

Os servicos usam Bean Validation para validar campos obrigatorios, valores numericos, datas e status permitidos. Tambem existe tratamento global de excecoes para retornar respostas padronizadas em casos de erro de validacao, regra de negocio, recurso nao encontrado e erro interno.

## Relacao Com O Enunciado

O projeto entrega:

- Mais de 4 endpoints RESTful relevantes ao tema escolhido.
- Integracao com Oracle Database.
- Migrations com Flyway.
- Dockerfiles para os microsservicos.
- Docker Compose para execucao do banco e dos services.
- Validacoes de entrada.
- Tratamento global de excecoes.
- Regras de negocio relacionadas a coleta seletiva, descarte correto e alerta por capacidade.
- Seguranca com Spring Security nos endpoints de escrita.
