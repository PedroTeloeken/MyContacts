# MyContacts

API REST simples para agenda de contatos, construída com Java 22, Spring Boot, Maven e SQLite.

## Integrantes

- Helena da Silva
- Jaíne Andrade
- Pedro Teloeken
- Pedro Hennig
- Ralf Domingues

## Stack utilizada

- Java 22
- Spring Boot
- Maven Wrapper
- Spring Web
- Spring Data JPA
- SQLite
- JUnit 5
- Mockito

## Como rodar localmente

Pré-requisitos:

- Java 22 instalado
- `JAVA_HOME` apontando para o Java 22

Com Linux/macOS:

```bash
./mvnw spring-boot:run
```

Com Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação usa um banco SQLite local chamado `mycontacts.db`, criado automaticamente na raiz do projeto.

## Como executar os testes

Com Linux/macOS:

```bash
./mvnw test
./mvnw clean package
```

Com Windows:

```bash
mvnw.cmd test
mvnw.cmd clean package
```

## Postman Collection

A collection do Postman está disponível em:

```text
/postman/MyContacts.postman_collection.json
```

Para importar:

1. Abrir o Postman
2. Clique em Import
3. Selecionar o arquivo `.json`

## Endpoints disponíveis

- `GET /health`
- `POST /contacts`
- `GET /contacts`
- `GET /contacts/{id}`
- `PUT /contacts/{id}`
- `DELETE /contacts/{id}`

## Exemplo de payload

### Criar ou atualizar contato

```json
{
  "name": "Ana Silva",
  "phone": "11999999999",
  "email": "ana@email.com"
}
```

## Observação sobre SQLite

- O arquivo `mycontacts.db` é gerado localmente e não deve ser versionado.
- A tabela `contacts` é criada automaticamente pela aplicação via JPA/Hibernate.
