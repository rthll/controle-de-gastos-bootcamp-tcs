# AntBalance - Controle de Gastos Pessoais

O objetivo do projeto é oferecer um sistema de controle de gastos pessoais e empresariais, que permite o gerenciamento financeiro individual ou corporativo, com suporte a divisão de despesas por categoria, setor e funcionário.
Suas principais funcionalidades são: 

- Controle financeiro pessoal e empresarial.
- Cadastro e autenticação de usuários com JWT.
- Registro de despesas por data, valor e categoria.
- Visualização de gastos mensais e totais.
- Controle de funcionários e divisão por setores.
- Geração de relatórios financeiros e exportação.
- API REST desenvolvida com Spring Boot  com arquitetura de microserviços
- Banco de dados PostgreSQL.
- Contêineres Docker para execução unificada e simplificada.
  
## 👤 Autores
- Repositório GitHub: https://github.com/rthll/controle-de-gastos-bootcamp-tcs (Backend) e https://github.com/jotasoftware/Frontend-AntBalance.git (Frontend)
- Desenvolvedores do Projeto: Caio Alves Galassi, João Pedro Moreira, Arion Teixeira, Vinícius Augusto da Costa, Rythielly Francisco Garcia Bezerra. 


## 🖥️ Tecnologias Utilizadas

### Backend
- Java 21 - Linguagem principal da aplicação.
- Spring Boot - Framework para criação de APIs REST
- Spring Security (JWT) - Autenticação e segurança com tokens
- Maven - Gerenciador de dependências e build
- Docker – Contêinerização dos serviços.
  
### Frontend
- React - Biblioteca JavaScript para construção da interface do usuário
- Vite.js
- Axios

### Bando de Dados
- PostgreSQL – Banco de dados relacional utilizado na produção.
- JPA + Hibernate

## 🔧 Como Executar o Software

###  Pré-requisitos: 
- Java 21 instalado
- Node.js + npm (para rodar o frontend)
- Maven instalado
- PostgreSQL (caso deseje executar fora do Docker)
- Docker + Docker Compose

### Backend: 
🔗 Repositório: [rthll/controle-de-gastos-bootcamp-tcs](https://github.com/rthll/controle-de-gastos-bootcamp-tcs)

```bash
# Clone o projeto: 
git clone https://github.com/rthll/controle-de-gastos-bootcamp-tcs.git

# Acesse a pasta do backend
cd controle-de-gastos-bootcamp-tcs/backend

# Execute com Maven
./mvnw spring-boot:run

# Suba os serviços com Docker Compose
docker-compose up --build

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
- Registro e edição de gastos.
- Geração e exportação de relatórios financeiros.
- Divisão de gastos entre os usuários.
- Cadastro e edição dos funcionários.
- Organização por setores empresariais.
- Controle de gastos corporativos por setor.


## 🛠 Estrutura dos microserviços

- `api-gateway`: Roteia e autentica requisições.
- `login-auth-api`: Autenticação e segurança.
- `recuperar-senha-service`: Recuperação de senha por e-mail.
- `gastos-services`: Registro e consulta de gastos.
- `categorias-services`: CRUD de categorias.
- `relatorios-service`: Exportação de relatórios filtrados (pdf ou excel).
- `funcionarios-service`: Cadastro e gerenciamento de funcionários.
- `setor-service`: Organização e controle dos setores da empresa.


## 🔐 Segurança

- Autenticação via JWT
- Filtros de segurança com Spring Security
- Controle de acesso por rotas no API Gateway


## 📊 Diagrama de Arquitetura do Software
Abaixo estão os diagramas que ilustram a arquitetura da aplicação AntBalance, separando a visão do frontend e do backend, com foco na comunicação entre os componentes e nos fluxos principais da aplicação:
### Diagrama do Backend:
![Diagrama da Arquitetura](diagrama%20arquitetura%20software.png)

### Diagrama do Frontend:


## 📄 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.



