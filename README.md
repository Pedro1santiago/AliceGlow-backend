# AliceGlow - Beauty Products Shop API

Plataforma backend para gerenciamento de loja de produtos de beleza, desenvolvida com as melhores práticas de arquitetura e segurança. Sistema completo de autenticação, gerenciamento de usuários, produtos e vendas em tempo real.

## Visão Geral

AliceGlow é uma API RESTful robusta e escalável desenvolvida para gerenciar uma loja de produtos de beleza. Com arquitetura moderna, segurança implementada via JWT e banco de dados relacional otimizado, a solução está pronta para ambientes de produção críticos.

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
- PostgreSQL 16 (para desenvolvimento local)
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

### Instalação Local

1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/AliceGlow-backend.git
cd AliceGlow-backend/aliceGlow
```

2. Instale as dependências
```bash
./mvnw clean install
```

3. Execute a aplicação
```bash
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

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

A aplicação inclui testes unitários para os principais componentes:

```bash
./mvnw test
```

### Arquivos de Teste
- `ProductServiceTest.java` - Testes do serviço de produtos
- `SaleServiceTest.java` - Testes do serviço de vendas
- `UserServiceTest.java` - Testes do serviço de usuários

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

## Monitoramento e Performance

Em produção no Render:
- Logs estruturados disponíveis via dashboard
- Métricas de uso de CPU e memória
- Alertas configuráveis para anomalias

## CI/CD

Configure pipelines de CI/CD para:
- Executar testes automaticamente
- Build e deploy automático em produção
- Validação de código via linters

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

## Testes Unitários

### Estrutura de Testes

A aplicação possui testes unitários com as melhores práticas de mercado, cobrindo controllers e serviços:

**Testes de Controllers:**
```
src/test/java/aliceGlow/example/aliceGlow/controller/
├── AuthControllerTest.java       (1 teste)
├── ProductControllerTest.java    (4 testes)
├── SaleControllerTest.java       (1 teste)
└── UserControllerTest.java       (1 teste)
```

**Testes de Serviços:**
```
src/test/java/aliceGlow/example/aliceGlow/service/
├── AuthServiceTest.java          (3 testes)
├── ProductServiceTest.java       (7 testes)
├── SaleServiceTest.java          (5 testes)
└── UserServiceTest.java          (8 testes)
```

### Arquitetura dos Testes

- **Padrão**: Injeção de Dependência via Construtor
- **Framework**: JUnit 5 + Mockito
- **Isolamento**: MockitoExtension para isolamento de dependências
- **Sem Banco de Dados**: Testes rápidos e independentes
- **Verificação de Comportamento**: `verify()` para garantir chamadas corretas
- **Setup Comum**: `@BeforeEach` para preparação de dados

### Exemplo de Teste (AuthControllerTest)

```java
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginDTO loginDTO;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        loginDTO = new LoginDTO("test@example.com", "password123");
        authResponse = new AuthResponse("token_jwt_123");
    }

    @Test
    void shouldLoginSuccessfully() {
        when(authService.login(any())).thenReturn(authResponse);

        ResponseEntity<AuthResponse> response = authController.login(loginDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token_jwt_123", response.getBody().token());
        verify(authService).login(any());
    }
}
```

### Executar Testes

```bash
# Todos os testes dos controllers
mvnw clean test -Dtest=*ControllerTest

# Todos os testes de serviços
mvnw clean test -Dtest=*ServiceTest

# Teste específico
mvnw test -Dtest=AuthControllerTest
mvnw test -Dtest=AuthServiceTest

# Um teste específico
mvnw test -Dtest=AuthServiceTest#shouldAuthenticateAndReturnToken

# Todos os testes
mvnw clean test

# Com cobertura de código
mvnw clean test jacoco:report
```

### Cobertura de Testes

| Camada | Classe | Testes | Métodos |
|--------|--------|--------|---------|
| **Controller** | Auth | 1 | login |
| **Controller** | Product | 4 | list, create, update, delete |
| **Controller** | Sale | 1 | list |
| **Controller** | User | 1 | create |
| **Service** | Auth | 3 | authenticate, token generation |
| **Service** | Product | 7 | list, create, update, delete, exceptions |
| **Service** | Sale | 5 | create, cancel, find, exceptions |
| **Service** | User | 8 | create, list, update, delete, exceptions |
| **Total** | - | **30** | **20+ endpoints** |

### Padrões Implementados

1. **AAA Pattern**: Arrange, Act, Assert, Verify
2. **Setup com @BeforeEach**: Preparação comum de dados
3. **Mockito**: Mock de todas as dependências
4. **Assertions Específicas**: Validação de HTTP Status, dados e comportamento
5. **Isolamento Total**: Sem banco de dados, sem chamadas HTTP reais
6. **Nomes Descritivos**: Métodos com padrão should...

### Testes por Categoria

**AuthServiceTest** (3 testes):
- `shouldAuthenticateAndReturnToken` - Autentica e retorna token JWT
- `shouldCallAuthenticationManagerWithCredentials` - Valida credenciais
- `shouldGenerateTokenFromAuthenticatedUser` - Token gerado corretamente

**ProductControllerTest** (4 testes):
- `shouldListProductsSuccessfully` - Lista produtos (200 OK)
- `shouldCreateProductSuccessfully` - Cria produto (201 CREATED)
- `shouldUpdateProductSuccessfully` - Atualiza produto (200 OK)
- `shouldDeleteProductSuccessfully` - Deleta produto (204 NO CONTENT)

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
