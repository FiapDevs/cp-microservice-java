# residuo-service

Microsservico Java com Spring Boot responsavel pelo cadastro, consulta, atualizacao, listagem e remocao de residuos registrados em pontos de coleta.

Este servico faz parte de uma arquitetura planejada com tres microsservicos:

- ponto-coleta-service
- residuo-service
- coleta-service

Neste projeto, o ponto de coleta e tratado apenas como referencia pelo campo `idPontoColeta`. Nao existe relacionamento JPA com entidades de outros microsservicos.

## Tecnologias

- Java 21
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- Lombok
- Oracle Database
- Oracle JDBC Driver
- Hibernate Oracle Dialect
- Docker

## Configuracao do Oracle Database

O banco relacional utilizado pelo servico deve ser Oracle Database. O Oracle SQL Developer pode ser usado apenas como ferramenta para acessar e administrar o banco.

Configure as variaveis de ambiente:

```bash
DB_URL=jdbc:oracle:thin:@localhost:1521:XE
DB_USERNAME=SEU_USUARIO
DB_PASSWORD=SUA_SENHA
```

No Windows PowerShell:

```powershell
$env:DB_URL="jdbc:oracle:thin:@localhost:1521:XE"
$env:DB_USERNAME="SEU_USUARIO"
$env:DB_PASSWORD="SUA_SENHA"
```

O projeto usa a tabela `T_RESIDUO` e a sequence `SEQ_RESIDUO`.

## Rodando localmente

```bash
mvn spring-boot:run
```

A API ficara disponivel na porta `8082`.

## Gerando o JAR

```bash
mvn clean package
```

O arquivo gerado sera:

```text
target/residuo-service-0.0.1-SNAPSHOT.jar
```

## Rodando via Docker

Gere o JAR antes de criar a imagem:

```bash
mvn clean package
docker build -t residuo-service .
docker run -p 8082:8082 -e DB_URL=jdbc:oracle:thin:@host.docker.internal:1521:XE -e DB_USERNAME=SEU_USUARIO -e DB_PASSWORD=SUA_SENHA residuo-service
```

## Endpoints

Base path: `/api/residuos`

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/api/residuos` | Criar residuo |
| GET | `/api/residuos` | Listar todos |
| GET | `/api/residuos/{id}` | Buscar por ID |
| PUT | `/api/residuos/{id}` | Atualizar residuo |
| DELETE | `/api/residuos/{id}` | Deletar residuo |
| GET | `/api/residuos/ponto-coleta/{idPontoColeta}` | Buscar por ponto de coleta |
| GET | `/api/residuos/tipo/{tipoResiduo}` | Buscar por tipo de residuo |
| GET | `/api/residuos/status/{status}` | Buscar por status |

## Exemplo de cadastro

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

## Exemplo de resposta

```json
{
  "id": 1,
  "idPontoColeta": 1,
  "nome": "Garrafa plastica",
  "tipoResiduo": "PLASTICO",
  "descricao": "Residuo plastico reciclavel",
  "quantidade": 120.0,
  "unidadeMedida": "KG",
  "status": "ATIVO",
  "dataRegistro": "2026-05-11T15:30:00",
  "dataAtualizacao": null
}
```

## Regras principais

- `idPontoColeta`, `nome`, `tipoResiduo`, `quantidade` e `unidadeMedida` sao tratados conforme as validacoes da API.
- `quantidade` nao pode ser negativa.
- `unidadeMedida` recebe `KG` automaticamente quando nao informada.
- `status` aceita apenas `ATIVO` ou `INATIVO`.
- `status` recebe `ATIVO` automaticamente quando nao informado.
- `dataRegistro` e preenchida no cadastro.
- `dataAtualizacao` e preenchida na atualizacao.
