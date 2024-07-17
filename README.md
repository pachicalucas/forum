# challenge-forum-hub

FórumHub é um projeto de API REST que simula o funcionamento de um fórum, concentrando-se especificamente nos tópicos. A API permite criar, visualizar, atualizar e deletar tópicos, implementando assim um CRUD completo (Create, Read, Update, Delete). Este projeto foi desenvolvido utilizando o framework Spring.

## Funcionalidades

A API oferece as seguintes funcionalidades:

- **Criar um novo tópico**
- **Mostrar todos os tópicos criados**
- **Mostrar um tópico específico**
- **Atualizar um tópico**
- **Eliminar um tópico**

## Objetivo

O objetivo do FórumHub é replicar o processo de funcionamento de um fórum no nível do back-end, abordando questões como armazenamento de informações e relacionamento de dados entre tópicos e respostas. Isso inclui:

- Implementação de uma API REST com rotas seguindo as melhores práticas do modelo REST
- Validações realizadas de acordo com as regras de negócio
- Implementação de uma base de dados relacional para a persistência da informação
- Serviço de autenticação/autorização para restringir o acesso à informação

## Tecnologias Utilizadas

- **Java**
- **Spring Boot**
- **Spring Data JPA**
- **Hibernate**
- **H2 Database** (ou outro banco de dados relacional de sua escolha)
- **Spring Security**
- **Maven**

## Estrutura do Projeto

```bash
src
├── main
│   ├── java
│   │   └── com
│   │       └── forumhub
│   │           ├── ForumHubApplication.java
│   │           ├── controller
│   │           ├── model
│   │           ├── repository
│   │           ├── service
│   │           └── config
│   └── resources
│       ├── application.properties
│       └── data.sql
└── test
    └── java
        └── com
            └── forumhub
                └── ForumHubApplicationTests.java
