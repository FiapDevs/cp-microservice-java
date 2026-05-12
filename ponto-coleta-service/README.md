# ponto-coleta-service

Microsservico do projeto de Gestao de Residuos responsavel exclusivamente pelo gerenciamento de pontos de coleta.

Este e o primeiro microsservico do projeto. Ele contem regras de pontos de coleta, alertas de capacidade e seguranca basica para operacoes de escrita.

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
- Flyway
- Spring Security
- Docker

## Banco de dados Oracle

O servico usa Oracle Database como banco relacional. O Oracle SQL Developer pode ser usado para administrar e consultar o banco, mas ele nao substitui o Oracle Database.

Configure as variaveis de ambiente antes de iniciar a aplicacao:

```bash
DB_URL=jdbc:oracle:thin:@localhost:1521:XE
DB_USERNAME=SEU_USUARIO
DB_PASSWORD=SUA_SENHA
```

No PowerShell:

```powershell
$env:DB_URL="jdbc:oracle:thin:@localhost:1521:XE"
$env:DB_USERNAME="SEU_USUARIO"
$env:DB_PASSWORD="SUA_SENHA"
$env:APP_USERNAME="admin"
$env:APP_PASSWORD="admin123"
```

A aplicacao usa Flyway para criar a sequence `SEQ_PONTO_COLETA`, a tabela `T_PONTO_COLETA` e os indices iniciais. As migrations ficam em:

```text
src/main/resources/db/migration
```

O Hibernate fica em modo `validate`, entao ele valida se o schema criado pelo Flyway esta compativel com as entidades JPA.

## Seguranca

Os endpoints `GET` sao publicos para consulta. Os endpoints `POST`, `PUT` e `DELETE` exigem autenticacao HTTP Basic.

Usuario e senha padrao:

```text
admin / admin123
```

Em ambiente real, sobrescreva pelas variaveis `APP_USERNAME` e `APP_PASSWORD`.

## Como rodar localmente

```bash
mvn spring-boot:run
```

O servico sobe na porta `8081`.

## Como gerar o jar

```bash
mvn clean package
```

O jar sera gerado em:

```text
target/ponto-coleta-service-0.0.1-SNAPSHOT.jar
```

## Como rodar via Docker

Gere o jar primeiro:

```bash
mvn clean package
```

Crie a imagem:

```bash
docker build -t ponto-coleta-service .
```

Execute o container:

```bash
docker run -p 8081:8081 \
  -e DB_URL="jdbc:oracle:thin:@host.docker.internal:1521:XE" \
  -e DB_USERNAME="SEU_USUARIO" \
  -e DB_PASSWORD="SUA_SENHA" \
  -e APP_USERNAME="admin" \
  -e APP_PASSWORD="admin123" \
  ponto-coleta-service
```

## Endpoints

Base path: `/api/pontos-coleta`

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/api/pontos-coleta` | Criar ponto de coleta |
| GET | `/api/pontos-coleta` | Listar todos |
| GET | `/api/pontos-coleta/{id}` | Buscar por ID |
| PUT | `/api/pontos-coleta/{id}` | Atualizar |
| DELETE | `/api/pontos-coleta/{id}` | Deletar |
| GET | `/api/pontos-coleta/cidade/{cidade}` | Buscar por cidade |
| GET | `/api/pontos-coleta/status/{status}` | Buscar por status |
| GET | `/api/pontos-coleta/tipo-residuo/{tipoResiduo}` | Buscar por tipo de residuo aceito |
| GET | `/api/pontos-coleta/alertas-capacidade?percentualMinimo=80` | Listar pontos proximos do limite de capacidade |

## Exemplo com autenticacao

```bash
curl -u admin:admin123 -X POST http://localhost:8081/api/pontos-coleta \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ecoponto Vila Mariana","endereco":"Rua Domingos de Morais, 1000","cidade":"Sao Paulo","estado":"SP","cep":"04010-100","latitude":-23.5892,"longitude":-46.6345,"capacidadeMaxima":500.0,"capacidadeAtual":120.0,"tipoResiduoAceito":"PLASTICO","status":"ATIVO"}'
```

## Exemplo de cadastro

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

## Exemplo de resposta

```json
{
  "id": 1,
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
  "status": "ATIVO",
  "dataCriacao": "2026-05-11T15:30:00",
  "dataAtualizacao": null
}
```
