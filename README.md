# MyContacts API

API REST de gerenciamento de contatos desenvolvida como projeto da disciplina de **Infraestrutura de Tecnologia da Informação e Comunicação** (FURB), com foco em colocar em prática um pipeline completo de **CI/CD** utilizando **GitHub Actions**.

Imagem Docker pública: **[hub.docker.com/r/jainea/my_contacts](https://hub.docker.com/r/jainea/my_contacts)**

---

## Integrantes

- Pedro Teloeken, 

---

## Stack Tecnológica

| Camada | Tecnologia |
|--------|------------|
| Linguagem | Java 22 |
| Framework | Spring Boot 4.0.6 |
| Persistência | Spring Data JPA |
| Banco de Dados | MySQL 8.4 |
| Build | Maven 3.9.9 (via wrapper `./mvnw`) |
| Testes | JUnit 5 + Mockito + MockMvc + AssertJ |
| Lint | Maven Checkstyle Plugin (Google Checks) |
| Containerização | Docker (multi-stage) + Docker Compose |
| CI/CD | GitHub Actions |

---

## Funcionalidades

A aplicação expõe um CRUD básico de contatos sob o path base `/contacts`.

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/contacts` | Cria um novo contato |
| `GET` | `/contacts` | Lista todos os contatos |
| `DELETE` | `/contacts/{id}` | Remove o contato pelo ID |

### Exemplo de payload (POST /contacts)

```json
{
  "name": "Pedro Teloeken",
  "phone": "47999999999",
  "email": "pedro@example.com"
}
```

---

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/mycontacts/mycontacts/
│   │   ├── MyContactsApplication.java   # Classe principal Spring Boot
│   │   ├── controller/                  # Endpoints REST
│   │   ├── service/                     # Regras de negócio
│   │   ├── repository/                  # Acesso a dados (JPA)
│   │   └── entity/                      # Entidades do domínio
│   └── resources/
│       └── application.properties       # Configurações
└── test/
    └── java/com/mycontacts/mycontacts/
        ├── controller/ContactControllerTest.java
        └── service/ContactServiceTest.java
```

---

## Como Executar

### Pré-requisitos

- Docker e Docker Compose **ou**
- Java 22 + Maven + MySQL 8.4 instalados localmente

Antes de executar, crie um arquivo `.env` na raiz do projeto:

```env
DB_USERNAME=mycontacts
DB_PASSWORD=sua_senha_aqui
```

### Opção A — Docker Compose (recomendado)

Sobe a aplicação **e** o MySQL em containers conectados:

```bash
docker compose up -d
```

A API ficará disponível em `http://localhost:8080`.

> O compose usa a imagem publicada no Docker Hub (`jainea/my_contacts:latest`).

Para parar:

```bash
docker compose down
```

### Opção B — Localmente com Maven

Requer um MySQL acessível em `localhost:3306` com o banco `MyContactsDB` criado.

```bash
./mvnw spring-boot:run
```

---

## Como rodar os testes

```bash
./mvnw test
```

Atualmente o projeto possui **6 testes unitários**, distribuídos em:

- `ContactControllerTest` (3 testes — criação, listagem e validação de payload)
- `ContactServiceTest` (3 testes — persistência, listagem com dados e listagem vazia)

---

## Imagem no Docker Hub

A imagem é publicada automaticamente pelo pipeline a cada push na branch `main`:

🔗 **https://hub.docker.com/r/jainea/my_contacts**

```bash
docker pull jainea/my_contacts:latest
docker run -p 8080:8080 \
  -e DB_USERNAME=mycontacts \
  -e DB_PASSWORD=senha \
  jainea/my_contacts:latest
```

---

## Pipeline CI/CD — Relatório das Tarefas

O workflow está definido em `.github/workflows/maven.yml` e cobre as 9 tarefas exigidas pelo enunciado.

### Tarefa 1 — Repositório e estrutura inicial

- Branch `main` protegida (exige Pull Request para merge).
- Branch de desenvolvimento `develop`.
- `.gitignore` adequado para Java/Maven (ignora `target/`, `.idea/`, `.env`, etc.).
- Este `README.md` com descrição do projeto, integrantes, stack e instruções de uso.

### Tarefa 2 — Pipeline de Build Automático

- Disparado em todo `push` e `pull_request` na branch `main`.
- Configura o ambiente Java com `actions/setup-java`.
- Executa o build com `./mvnw package`.
- Se o build falhar, o pipeline falha como um todo (`needs` impede jobs subsequentes).

### Tarefa 3 — Execução Automática de Testes

- Job `test` executa `./mvnw test`.
- Pipeline falha automaticamente quando algum teste quebra (Surefire retorna exit code ≠ 0).

**Pergunta — o que acontece se um teste falhar propositalmente?**
O job de teste termina com status de falha, o GitHub Actions marca a execução em vermelho, o status check do PR não fica verde (bloqueando o merge na `main` por causa da proteção de branch) e os jobs que dependem dele (`lint`, `docker`) não chegam a executar.

### Tarefa 4 — Publicação de Artefatos

- O job `build` usa `actions/upload-artifact` para salvar o JAR gerado em `target/*.jar`.
- O artefato fica disponível para download na aba **Actions** do repositório.

**Pergunta — em que cenário real isso seria útil?**
Distribuir o binário exatamente como foi testado para QA, debug de falhas de pipeline (analisar o artefato sem precisar buildar de novo), e como input para etapas posteriores de deploy sem retrabalho.

### Tarefa 5 — Variáveis de Ambiente e Secrets

Secrets configurados no repositório:

| Secret | Uso |
|--------|-----|
| `DB_USERNAME` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |
| `API_KEY` | Exemplo de chave sensível |
| `DOCKERHUB_USERNAME` | Login no Docker Hub |
| `DOCKERHUB_TOKEN` | Access Token do Docker Hub |

Referenciados no workflow via `${{ secrets.NOME_DO_SECRET }}`.

**Pergunta — por que nunca devemos commitar credenciais no código?**
O Git mantém histórico permanente: mesmo apagando depois, a credencial fica acessível em commits antigos. Em repositórios públicos isso vira exposição imediata, e mesmo em privados aumenta a superfície de ataque (qualquer pessoa com acesso ao histórico vê a senha). Rotacionar uma credencial vazada exige reescrever histórico, o que é caro e arriscado. Secrets do GitHub Actions são criptografados em repouso, injetados apenas em runtime e mascarados automaticamente nos logs.

### Tarefa 6 — Matriz de Versões

O workflow usa `strategy.matrix` para rodar os jobs em **Java 22 e Java 23**, garantindo compatibilidade com a versão LTS atual e a próxima.

**Pergunta — qual versão apresentou diferença de comportamento?**
Nenhuma diferença funcional observada — todos os testes passam em ambas as versões. O exercício serve principalmente como validação de que o projeto continuará funcionando em versões futuras do Java, antecipando incompatibilidades antes que se tornem bloqueantes.

### Tarefa 7 — Pull Request com Status Check

A branch `main` está configurada com regra de proteção que exige o status check do job `build` (entre outros) como obrigatório antes do merge. Nenhum PR pode ser mergeado com pipeline vermelho.

> O print do painel de configuração da proteção de branch está incluído no relatório/apresentação.

### Tarefa 8 — Jobs Paralelos e Dependências

```
build  ─┐
        ├─► lint
test  ──┘   (needs: build)

build ─► docker (needs: build, somente push na main)
```

- `build` e `test` rodam **em paralelo**.
- `lint` depende de `build` (`needs: build`).
- `docker` depende de `build` e só executa em push para `main`.

**Pergunta — por que paralelismo importa em pipelines de CI?**
Reduz drasticamente o tempo total de feedback ao desenvolvedor, isola falhas (um job quebrar não cancela os outros, então identificamos múltiplos problemas em uma execução só), e aproveita melhor a capacidade dos runners. Em equipes que abrem dezenas de PRs por dia, paralelismo é a diferença entre minutos e horas de espera.

### Tarefa 9 — CD: Publicação da Imagem Docker no Docker Hub

- `Dockerfile` multi-stage na raiz (Maven builder + Eclipse Temurin 22 JRE).
- Job `docker` autentica com `docker/login-action` usando `DOCKERHUB_USERNAME` e `DOCKERHUB_TOKEN`.
- Executa `docker build` + `docker push` com **duas tags**: `latest` e `${{ github.sha }}`.
- Roda **somente** em push na branch `main`, depois que o CI passou.
- Imagem pública e executável: [hub.docker.com/r/jainea/my_contacts](https://hub.docker.com/r/jainea/my_contacts)

**Pergunta — qual a diferença entre uma tag `latest` e uma tag por SHA? Quando usar cada uma?**

| Aspecto | `latest` | Tag por SHA |
|---------|----------|-------------|
| Mutabilidade | Móvel — sempre aponta para o último push | Imutável — sempre aponta para aquele commit |
| Rastreabilidade | Difícil saber qual versão exatamente está rodando | Identifica o commit exato no histórico do Git |
| Reprodutibilidade | Pode mudar embaixo do deploy | Garante que o ambiente é sempre o mesmo |
| Uso recomendado | Desenvolvimento, demos rápidas | Produção, staging, ambientes auditáveis |

**Regra prática:** desenvolvimento pode puxar `latest` para sempre ter a versão mais recente; **produção** deve sempre fixar a tag por SHA (ou uma tag semântica imutável como `v1.2.3`) para permitir rollback determinístico e auditoria.

---

## Variáveis de Ambiente

| Variável | Descrição | Onde |
|----------|-----------|------|
| `DB_USERNAME` | Usuário do MySQL | `.env` local + GitHub Secrets |
| `DB_PASSWORD` | Senha do MySQL | `.env` local + GitHub Secrets |

---

## Trabalho Acadêmico

- **Disciplina:** Infraestrutura de Tecnologia da Informação e Comunicação
- **Professor:** Gabriel Castellani de Oliveira
- **Instituição:** FURB — Universidade Regional de Blumenau
- **Departamento:** Sistemas e Computação
- **Entrega:** 13/05/2026 — Apresentação: 20/05/2026
