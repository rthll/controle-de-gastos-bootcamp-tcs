# AntBalance - Controle de Gastos Pessoais

O objetivo do projeto é oferecer um sistema de controle de gastos e investimentos pessoais com funcionalidades :

- Cadastro e autenticação de usuários com JWT.
- Registro de despesas por data, valor e categoria.
- Visualização de gastos mensais e totais.
- Cálculo de rentabilidade de investimentos.
- Relatórios e listagem de despesas e rendas.
- API REST desenvolvida com Spring Boot.
- Banco de dados H2 em memória.
  
## 👤 Autores
- Repositório GitHub: https://github.com/rthll/controle-de-gastos-bootcamp-tcs
- Desenvolvedores do Projeto: Caio Alves Galassi, João Pedro Moreira, Arion Teixeira, Vinícius Augusto da Costa, Rythielly Francisco Garcia Bezerra. 


## 🖥️ Tecnologias Utilizadas

### Backend
- Java 21 - Linguagem principal da aplicação.
- Spring Boot - Framework para criação de APIs REST
- Spring Security (JWT) - Autenticação e segurança com tokens
- Maven - Gerenciador de dependências e build
- H2 Database - Banco de dados em memória para testes locais
- Apache Kafka -  Comunicação assíncrona e troca de mensagens entre serviços
  
### Frontend
- React - Biblioteca JavaScript para construção da interface do usuário

## 🔧 Como Executar o Projeto

###  Pré-requisitos: 
- Java 21 instalado
- Node.js + npm (para rodar o frontend)
- Maven instalado

### Backend: 
🔗 Repositório: [rthll/controle-de-gastos-bootcamp-tcs](https://github.com/rthll/controle-de-gastos-bootcamp-tcs)

```bash
# Clone o projeto: 
git clone https://github.com/rthll/controle-de-gastos-bootcamp-tcs.git

# Acesse a pasta do backend
cd controle-de-gastos-bootcamp-tcs/backend

# Execute com Maven
./mvnw spring-boot:run

Acesse a API em:
http://localhost:8080
```
### Frontend: 
🔗 Repositório: [jotasoftware/Frontend-AntBalance](https://github.com/jotasoftware/Frontend-AntBalance)
```bash
# Clone o frontend
git clone https://github.com/jotasoftware/Frontend-AntBalance.git

# Acesse a pasta do projeto
cd Frontend-AntBalance

# Instale as dependências
npm install

# Rode a aplicação
npm run dev

Acesse no navegador:
http://localhost:5173
```
## Arquitetura do Software
```bash
Abaixo está o diagrama representando os microserviços, API Gateway, persistência de dados e serviços externos usados no projeto:

![Diagrama da Arquitetura](diagrama%20arquitetura%20software.png)

```

