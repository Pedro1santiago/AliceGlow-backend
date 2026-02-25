# AliceGlow - Beauty Products Shop API

Backend para gerenciamento de loja de produtos de beleza. Possui autenticação via JWT, endpoints REST e persistência com PostgreSQL + Flyway.

## Visão Geral

AliceGlow é uma API RESTful para gerenciar usuários, produtos, vendas, caixa e relatórios. O deploy de referência roda no Render e o banco no Neon.

## Links (Produção)

- Base URL: https://aliceglow-backend.onrender.com
- Swagger UI: https://aliceglow-backend.onrender.com/swagger-ui/index.html
- OpenAPI JSON: https://aliceglow-backend.onrender.com/v3/api-docs

## Stack Tecnológico

### Backend
- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.9** - Framework web e IoC
- **Spring Data JPA** - ORM e persistência de dados
- **Spring Security** - Autenticação e autorização
- **JWT (JSON Web Token)** - Autenticação stateless

### Banco de Dados
- **PostgreSQL 16** - Sistema gerenciador de banco de dados relacional
- **Flyway 10.21.0** - Versionamento e migração de banco de dados
- **Neon** - Plataforma cloud para hospedagem do PostgreSQL

### Outros
- **Maven** - Gerenciador de dependências e build
- **Jakarta Validation** - Validação de dados de entrada

## Deployment

- **Backend**: Render (render.com)
- **Banco de Dados**: Neon (neon.tech)
- **Containerização**: Docker (disponível via Dockerfile)

## Dependências Principais

```xml
<!-- Spring Boot Starter Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>

<!-- Flyway Migration -->
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
    <version>10.21.0</version>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## Começando

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- PostgreSQL (apenas se for rodar local sem Docker)
- Git

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente para executar a aplicação:

```bash
# Banco de Dados
DB_URL_JDBC=jdbc:postgresql://host:port/database
DB_USER=seu_usuario
DB_PASSWORD=sua_senha

# JWT
JWT_SECRET=sua_chave_secreta_jwt
```

### Executar localmente (opcional)

Se você não vai rodar localmente, pode pular esta seção e usar os links de produção acima.

#### Clonar

1. Clone o repositório
```bash
git clone https://github.com/Pedro1santiago/AliceGlow-backend.git
cd AliceGlow-backend/aliceGlow
```

#### Rodar via Maven Wrapper

1. Instale as dependências
```bash
./mvnw clean install
```

2. Execute a aplicação
```bash
./mvnw spring-boot:run
```

Após iniciar, use a base URL do ambiente (por exemplo, a URL de produção no Render).

## Documentação da API (Swagger/OpenAPI)

A documentação é gerada automaticamente via OpenAPI 3 + Swagger UI.

Troque `BASE_URL` pelo host que você estiver usando (produção ou local):

- Swagger UI: BASE_URL/swagger-ui/index.html
- OpenAPI JSON: BASE_URL/v3/api-docs
- OpenAPI YAML: BASE_URL/v3/api-docs.yaml

Observações:

- Se você receber `401 Unauthorized`, confirme se o deploy está com a versão mais recente do código e se as rotas do Swagger/OpenAPI estão liberadas no Spring Security.
- Para testar endpoints protegidos via JWT, use o botão **Authorize** no Swagger UI e informe o token.

## Arquitetura

### Estrutura de Diretórios

```
src/
├── main/
│   ├── java/aliceGlow/example/aliceGlow/
│   │   ├── AliceGlowApplication.java           # Classe principal
│   │   ├── controller/                         # Endpoints da API
│   │   │   ├── AuthController.java
│   │   │   ├── ProductController.java
│   │   │   ├── SaleController.java
│   │   │   └── UserController.java
│   │   ├── service/                            # Lógica de negócio
│   │   │   ├── AuthService.java
│   │   │   ├── ProductService.java
│   │   │   ├── SaleService.java
│   │   │   └── UserService.java
│   │   ├── repository/                         # Acesso aos dados
│   │   │   ├── UserRepository.java
│   │   │   ├── ProductRepository.java
│   │   │   ├── SaleRepository.java
│   │   │   ├── SaleItemRepository.java
│   │   │   └── PerfilRepository.java
│   │   ├── domain/                             # Entidades JPA
│   │   │   ├── User.java
│   │   │   ├── Product.java
│   │   │   ├── Sale.java
│   │   │   ├── SaleItem.java
│   │   │   └── Perfil.java
│   │   ├── dto/                                # Data Transfer Objects
│   │   │   ├── auth/
│   │   │   ├── user/
│   │   │   ├── product/
│   │   │   ├── sale/
│   │   │   ├── saleItem/
│   │   │   └── perfil/
│   │   ├── exception/                          # Exceções customizadas
│   │   │   ├── EmailAlreadyExistsException.java
│   │   │   ├── ProductNotFoundException.java
│   │   │   ├── SaleNotFoundException.java
│   │   │   ├── UserNotFoundException.java
│   │   │   └── ...
│   │   ├── infra/
│   │   │   ├── config/                         # Configurações
│   │   │   └── security/                       # Segurança e JWT
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/                   # Scripts Flyway
│   └── test/                                   # Testes unitários
└── ...
```

### Padrões Utilizados

- **MVC** - Model-View-Controller
- **DTO** - Data Transfer Objects para separação de responsabilidades
- **Repository Pattern** - Abstração de acesso aos dados
- **Service Layer** - Lógica de negócio centralizada
- **Exception Handling** - Tratamento customizado de erros

## Autenticação e Segurança

A aplicação utiliza **JWT (JSON Web Token)** para autenticação e autorização:

- Tokens stateless sem necessidade de sessão no servidor
- Secrets seguros para assinatura de tokens
- Integração com Spring Security para proteção de endpoints
- Validação automática de permissões por role

## Endpoints da API

### Autenticação
```
POST /auth/login
Realiza login e retorna token JWT
```

### Usuários
```
POST /users                 # Criar novo usuário
GET /users                  # Listar todos os usuários
PUT /users/{id}            # Atualizar usuário
DELETE /users/{id}         # Deletar usuário
```

### Produtos
```
GET /products              # Listar todos os produtos
POST /products             # Criar novo produto
DELETE /products/{id}      # Deletar produto
```

### Vendas
```
GET /sales                 # Listar todas as vendas
GET /sales/{id}            # Obter detalhes de uma venda
POST /sales                # Criar nova venda
```

### Perfis
Gerenciamento de perfis de usuário (admin, cliente, etc)

## Banco de Dados

### Migrations (Flyway)

As migrações são executadas automaticamente ao iniciar a aplicação:

- **V1__create_tables_users_and_perfis.sql** - Criação de tabelas base de usuários
- **V2__create_products_sales_items.sql** - Tabelas de produtos e vendas
- **V3__rename_price_to_cost_price.sql** - Atualização de schema
- **V4__insert_default_profiles.sql** - Dados iniciais de perfis
- **V5__add_canceled_column_to_sales.sql** - Adição de coluna de cancelamento

### Entidades Principais

**User** - Usuários do sistema
- Email único
- Senha criptografada
- Perfil de acesso
- Dados de contato

**Product** - Produtos de beleza
- Nome e descrição
- Preço de custo
- Preço de venda
- Estoque

**Sale** - Vendas realizadas
- Data da venda
- Usuário responsável
- Itens da venda
- Status (ativo/cancelado)

**SaleItem** - Itens individuais de uma venda
- Produto
- Quantidade
- Preço unitário

**Perfil** - Papéis/Roles do sistema
- Admin
- Cliente
- Gerenciador

## Configuração

### application.properties

A aplicação é configurada via variáveis de ambiente para máxima flexibilidade em diferentes ambientes:

```properties
# Application
spring.application.name=aliceGlow

# Database
spring.datasource.url=${DB_URL_JDBC}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=none
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.schemas=public
spring.flyway.default-schema=public

# JWT
jwt.secret=${JWT_SECRET}
```

## Testes

O projeto possui testes unitários para controllers e services (JUnit 5 + Mockito).

```bash
./mvnw test
```

No Windows, se preferir:

```bash
.\mvnw.cmd test
```

## Docker

A aplicação está containerizada e pode ser executada via Docker:

```bash
docker build -t aliceglow-backend:latest .
docker run -e DB_URL_JDBC=jdbc:postgresql://... \
           -e DB_USER=... \
           -e DB_PASSWORD=... \
           -e JWT_SECRET=... \
           -p 8080:8080 \
           aliceglow-backend:latest
```

## Tratamento de Erros

A aplicação implementa exceções customizadas para cenários específicos:

- `EmailAlreadyExistsException` - Email duplicado
- `ProductNotFoundException` - Produto não encontrado
- `SaleNotFoundException` - Venda não encontrada
- `UserNotFoundException` - Usuário não encontrado
- `StockNegativeException` - Estoque insuficiente
- `SaleWithoutItemsException` - Venda sem itens
- `CostPriceCannotBeNegativeException` - Preço negativo
- `DefaultUserProfileNotFoundException` - Perfil padrão não encontrado

## Validações

O sistema implementa validações robustas em todas as camadas:

- Validação de entrada via Jakarta Validation
- Regras de negócio no serviço
- Constraints em banco de dados

## Deployment em Produção

### Passos para Deploy no Render

1. Criar nova aplicação no Render
2. Conectar repositório Git
3. Configurar variáveis de ambiente
4. Conectar banco de dados Neon
5. Deploy automático em push

### Variáveis Obrigatórias no Render

```
DB_URL_JDBC=jdbc:postgresql://ep-....neon.tech:5432/neondb
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
JWT_SECRET=sua_chave_secreta_muito_segura
```

## Commits Convencionais

Utilize commits convencionais para melhor rastreabilidade:

```
feat: adição de nova funcionalidade
fix: correção de bug
refactor: refatoração de código
test: adição/atualização de testes
docs: atualização de documentação
```

## Licença

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

### Copyright
© 2026 Pedro1santiago. Todos os direitos reservados.

### Desenvolvimento
**Desenvolvedor**: Pedro1santiago  
**GitHub**: [@Pedro1santiago](https://github.com/Pedro1santiago)  
**Contato**: Entre em contato pelo GitHub

---

## Suporte

Para questões ou problemas, entre em contato através do GitHub: [@Pedro1santiago](https://github.com/Pedro1santiago)

---

**Versão**: 0.0.1  
**Status**: Em Produção  
**Propriedade**: Pedro1santiago  
**Última Atualização**: Fevereiro 2026
