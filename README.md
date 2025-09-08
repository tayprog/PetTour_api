# PetTour API 🐾

API RESTful desenvolvida em Java com Spring Boot para o backend do aplicativo PetTour, um catálogo de serviços para pets.

## 📜 Descrição

Este projeto é o backend responsável por toda a regra de negócio, segurança e persistência de dados do sistema PetTour. Ele fornece uma API robusta para gerenciamento de usuários, perfis, pets, serviços e agendamentos, com um sistema de autenticação seguro baseado em JWT e controle de acesso por papéis (Roles).

## ✨ Funcionalidades

* **Autenticação Segura:** Sistema de registro e login com senhas criptografadas e autenticação via JSON Web Tokens (JWT).
* **Segurança Baseada em Papéis (Roles):** Distinção entre usuários comuns (`ROLE_USER`) e administradores (`ROLE_ADMIN`), com permissões específicas para cada um.
* **Gerenciamento de Perfis:** Usuários podem visualizar, atualizar e **deletar** as suas próprias contas.
* **Gerenciamento de Pets:** Usuários logados podem cadastrar, visualizar, atualizar e deletar os seus próprios pets.
* **CRUD de Serviços:** Administradores têm controle total sobre o catálogo de serviços.
* **Sistema de Agendamento:** Usuários podem agendar serviços para os seus pets, com validação de regras de negócio como prevenção de agendamentos duplos no mesmo horário.
* **Validação de Dados:** Proteção contra dados de entrada inválidos (ex: emails mal formatados, agendamentos no passado).
* **Tratamento de Erros:** Respostas de erro padronizadas e claras para o cliente (`400`, `403`, `404`, `409`).

## 🛠️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3**
* **Spring Security**
* **Spring Data JPA / Hibernate**
* **H2 Database** (para desenvolvimento)
* **Maven**
* **Lombok**
* **Jakarta Bean Validation**
* **java-jwt (Auth0)**

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **JDK 17** ou superior.
* **Maven 3.8** ou superior.

### Passos para Execução
1.  Clone o repositório.
2.  Navegue até a pasta raiz do projeto.
3.  Execute o comando: `./mvnw spring-boot:run`
4.  A API estará rodando em `http://localhost:8080`.

### Acessando o Banco de Dados H2
* Com a aplicação rodando, acesse `http://localhost:8080/h2-console`.
* **JDBC URL:** `jdbc:h2:file:./pettourdb`
* **User Name:** `sa`
* **Password:** `password`

## 📖 Endpoints da API

> **Nota:** Todos os endpoints marcados como **Protegido** requerem um token JWT válido no cabeçalho `Authorization: Bearer <token>`.

---

### Autenticação (`/auth`)

#### `POST /auth/registrar`
Registra um novo usuário com o papel padrão `ROLE_USER`.
* **Protegido:** Não

#### `POST /auth/login`
Autentica um usuário e retorna um token JWT.
* **Protegido:** Não

---

### Perfil do Usuário (`/perfil`)

#### `GET /perfil`
Retorna as informações do perfil do usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `PUT /perfil`
Atualiza as informações do perfil do usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `DELETE /perfil`
Deleta a conta do usuário logado e todos os seus dados associados (pets, agendamentos).
* **Protegido:** Sim (`USER` ou `ADMIN`)

---

### Pets do Usuário (`/perfil/pets`)

#### `POST /perfil/pets`
Cadastra um novo pet para o usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `GET /perfil/pets`
Lista todos os pets do usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `PUT /perfil/pets/{id}`
Atualiza um pet específico. A API valida se o pet pertence ao usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `DELETE /perfil/pets/{id}`
Deleta um pet específico. A API valida se o pet pertence ao usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

---

### Serviços (`/servicos`)

#### `GET /servicos`
Lista todos os serviços disponíveis no catálogo.
* **Protegido:** Não

#### `POST /servicos`, `PUT /servicos/{id}`, `DELETE /servicos/{id}`
Cria, atualiza ou deleta um serviço.
* **Protegido:** Sim (**Apenas `ADMIN`**)

---

### Agendamentos (`/agendamentos`)

#### `POST /agendamentos`
Cria um novo agendamento para um pet do usuário logado. A API valida se o horário está disponível.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `GET /agendamentos`
Lista todos os agendamentos feitos pelo usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `PATCH /agendamentos/{id}/cancelar`
Muda o status de um agendamento para `CANCELADO`. A API valida se o agendamento pertence ao usuário logado.
* **Protegido:** Sim (`USER` ou `ADMIN`)

#### `PATCH /agendamentos/{id}/concluir`
Muda o status de um agendamento para `CONCLUIDO`.
* **Protegido:** Sim (**Apenas `ADMIN`**)

#### `GET /agendamentos/horarios-disponiveis`
Retorna uma lista de horários disponíveis para uma data específica.
* **Endpoint:** `/agendamentos/horarios-disponiveis?data=AAAA-MM-DD`
* **Protegido:** Sim (`USER` ou `ADMIN`)



