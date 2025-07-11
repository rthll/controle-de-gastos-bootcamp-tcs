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
  
### Frontend
- React - Biblioteca JavaScript para construção da interface do usuário

## 🔧 Como Executar o Software

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
## ⚙️ Funcionalidades

- Cadastro e autenticação de usuários com JWT.
- Recuperação de senha via e-mail.
- Cadastro e gerenciamento de categorias.
- Registro e edição de gastos e investimentos.
- Geração de relatórios financeiros.
- Divisão de gastos entre os usuários.

## 🛠 Estrutura dos microserviços

- `api-gateway`: Roteia e autentica requisições
- `login-auth-api`: Autenticação e segurança
- `recuperar-senha-service`: Recuperação de senha por e-mail
- `gastos-services`: Registro e consulta de gastos
- `investimentos-service`: Controle de investimentos
- `categorias-services`: CRUD de categorias
- `relatorio-services`: Exportação de relatórios filtrados (pdf ou excel)

## 🔐 Segurança

- Autenticação via JWT
- Filtros de segurança com Spring Security
- Controle de acesso por rotas no API Gateway


## 📊 Diagrama de Arquitetura do Software
Abaixo está o diagrama representando os microserviços, API Gateway, persistência de dados e serviços externos usados no projeto:

![Diagrama da Arquitetura](diagrama%20arquitetura%20software.png)

## 📄 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.



