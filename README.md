<div align="center">
  <img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=F48FB1,FFF59D,81D4FA&height=250&section=header&text=CyberpunkMech&fontSize=50&fontColor=ffffff&fontAlignY=40&animation=fadeIn"/>
</div>

<div align="center">
  <img src="https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen?style=for-the-badge&logo=codefactor&logoColor=white" alt="Status Concluído"/>
  <img src="https://img.shields.io/badge/Java_23-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white&color=F48FB1" alt="Java 23"/>
  <img src="https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 3"/>
  <img src="https://img.shields.io/badge/Firebase_Firestore-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase Firestore"/>
</div>

<div align="center">
  <br>
  <a href="#-versão-em-português">
    <img src="https://img.shields.io/badge/Idioma-Português_BR-green?style=for-the-badge&logo=github&logoColor=white&color=F48FB1" alt="Versão em Português"/>
  </a>
  <a href="#-english-version">
    <img src="https://img.shields.io/badge/Language-English-blue?style=for-the-badge&logo=github&logoColor=white&color=81D4FA" alt="English Version"/>
  </a>
</div>

<p align="center">
  <b>PT-BR:</b> API REST em Spring Boot e Firebase Firestore para gerenciamento de Jogadores e Mechs com suporte a polimorfismo. 🤖⚡
  <br>
  <b>EN:</b> REST API built with Spring Boot and Firebase Firestore for managing Players and Mechs with polymorphic support.
</p>

<br>

---

## 🇧🇷 Versão em Português

## 🤖 Visão Geral
A CyberpunkMech API é um backend em Spring Boot integrado ao Google Cloud Firestore (Firebase). O sistema gerencia jogadores e suas garagens de Mechs, permitindo cadastrar robôs de classes distintas (`AttackMech` e `DefensiveMech`) mantendo a integridade dos dados e serialização polimórfica.

### ✨ Funcionalidades Prontas
- [x] **Arquitetura Polimórfica:** Hierarquia de classes a partir da abstrata `Mech`, gerenciando `AttackMech` e `DefensiveMech`.
- [x] **Persistência Firestore:** Integração assíncrona com Firebase usando discriminador `"type"` para recriar instâncias corretas.
- [x] **Garagem Dinâmica:** A consulta de um Jogador traz automaticamente a lista completa de Mechs vinculados ao seu `playerId`.
- [x] **Endpoints REST:** Controladores completos para rotas de Player e Mech com suporte a CORS.

---

## 🛠 Tech Stack
* **Lógica e Backend:** Java 23 & Spring Boot 3.
* **Banco de Dados:** Google Cloud Firestore (Firebase Admin SDK).
* **Gerenciamento de Dependências:** Maven.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
- [JDK 23](https://www.oracle.com/java/technologies/downloads/) instalado.
- Credenciais da conta de serviço do Firebase configuradas.

### 1. Clonar o Repositório
```bash
git clone https://github.com/Naita1/Cyber-Punk-Mech.git
cd Cyber-Punk-Mech
```

### 2. Rodar a Aplicação

```bash
./mvnw spring-boot:run
```

---

## 🔌 Endpoints da API

### Players

* `POST /players` - Cadastra um novo jogador.
* `GET /players/{idPlayer}` - Retorna o jogador com a garagem de Mechs preenchida.
* `DELETE /players/{idPlayer}` - Remove o jogador.

### Mechs

* `POST /mechs/attack` - Cadastra um Mech do tipo Ataque.
* `POST /mechs/defensive` - Cadastra um Mech do tipo Defensivo.
* `GET /mechs/{idMech}` - Busca um Mech por ID.
* `GET /mechs?playerId={idPlayer}` - Busca os Mechs de um jogador específico.
* `DELETE /mechs/{idMech}` - Remove um Mech por ID.

---

## 🧪 Exemplo de Requisição (PowerShell / cURL)

```powershell
# Cadastrar Player
Invoke-RestMethod -Uri "http://localhost:8080/players" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"idPlayer":"player-001","namePlayer":"CyberSamurai","coins":500}'

# Cadastrar Mech de Ataque
Invoke-RestMethod -Uri "http://localhost:8080/mechs/attack" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"idMech":"mech-atk-01","playerId":"player-001","model":"Viper-X","maxHealth":100,"battery":80,"attackPower":35,"maxHeat":100}'

# Cadastrar Mech Defensivo
Invoke-RestMethod -Uri "http://localhost:8080/mechs/defensive" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"idMech":"mech-def-01","playerId":"player-001","model":"Aegis-Prime","maxHealth":150,"battery":70,"defenseRating":40,"shieldCapacity":120}'

# Obter Player
Invoke-RestMethod -Uri "http://localhost:8080/players/player-001" -Method GET

# Obter Mechs de um Player
Invoke-RestMethod -Uri "http://localhost:8080/mechs?playerId=player-001" -Method GET
```

```bash
# Cadastrar Player
curl -X POST "http://localhost:8080/players" -H "Content-Type: application/json" -d '{"idPlayer":"player-001","namePlayer":"CyberSamurai","coins":500}'

# Cadastrar Mech de Ataque
curl -X POST "http://localhost:8080/mechs/attack" -H "Content-Type: application/json" -d '{"idMech":"mech-atk-01","playerId":"player-001","model":"Viper-X","maxHealth":100,"battery":80,"attackPower":35,"maxHeat":100}'

# Cadastrar Mech Defensivo
curl -X POST "http://localhost:8080/mechs/defensive" -H "Content-Type: application/json" -d '{"idMech":"mech-def-01","playerId":"player-001","model":"Aegis-Prime","maxHealth":150,"battery":70,"defenseRating":40,"shieldCapacity":120}'

# Obter Player
curl -X GET "http://localhost:8080/players/player-001"

# Obter Mechs de um Player
curl -X GET "http://localhost:8080/mechs?playerId=player-001"
```

---

## 🇺🇸 English Version

## 🤖 Overview

CyberpunkMech API is a Spring Boot backend integrated with Google Cloud Firestore (Firebase). The system manages players and their Mech garages, allowing registration of distinct robot classes (`AttackMech` and `DefensiveMech`) while maintaining data integrity and polymorphic serialization.

### ✨ Current Features

* [x] **Polymorphic Architecture:** Class hierarchy based on abstract `Mech`, managing `AttackMech` and `DefensiveMech`.
* [x] **Firestore Persistence:** Asynchronous Firebase integration using a `"type"` discriminator field to rebuild exact instances.
* [x] **Dynamic Garage:** Fetching a Player automatically populates their full Mech garage based on their `playerId`.
* [x] **REST Endpoints:** Full REST Controllers for Player and Mech routes with CORS support.

---

## 🛠 Tech Stack

* **Language & Backend:** Java 23 & Spring Boot 3.
* **Database:** Google Cloud Firestore (Firebase Admin SDK).
* **Build Tool:** Maven.

---

## 🚀 How to Run

### Prerequisites

* [JDK 23](https://www.oracle.com/java/technologies/downloads/) installed.
* Firebase Service Account credentials configured.

### 1. Clone the repository

```bash
git clone https://github.com/Naita1/Cyber-Punk-Mech.git
cd Cyber-Punk-Mech
```

### 2. Run Application

```bash
./mvnw spring-boot:run
```

---

## 🔌 API Endpoints

### Players

* `POST /players` - Register a new player.
* `GET /players/{idPlayer}` - Get a player along with their populated Mech garage.
* `DELETE /players/{idPlayer}` - Delete a player.

### Mechs

* `POST /mechs/attack` - Register an Attack Mech.
* `POST /mechs/defensive` - Register a Defensive Mech.
* `GET /mechs/{idMech}` - Get a Mech by ID.
* `GET /mechs?playerId={idPlayer}` - List all Mechs belonging to a player.
* `DELETE /mechs/{idMech}` - Delete a Mech by ID.

---

## 🧪 Request Examples (PowerShell / cURL)

```powershell
# Register Player
Invoke-RestMethod -Uri "http://localhost:8080/players" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"idPlayer":"player-001","namePlayer":"CyberSamurai","coins":500}'

# Register Attack Mech
Invoke-RestMethod -Uri "http://localhost:8080/mechs/attack" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"idMech":"mech-atk-01","playerId":"player-001","model":"Viper-X","maxHealth":100,"battery":80,"attackPower":35,"maxHeat":100}'

# Register Defensive Mech
Invoke-RestMethod -Uri "http://localhost:8080/mechs/defensive" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"idMech":"mech-def-01","playerId":"player-001","model":"Aegis-Prime","maxHealth":150,"battery":70,"defenseRating":40,"shieldCapacity":120}'

# Get Player
Invoke-RestMethod -Uri "http://localhost:8080/players/player-001" -Method GET

# Get Player Mechs
Invoke-RestMethod -Uri "http://localhost:8080/mechs?playerId=player-001" -Method GET
```

```bash
# Register Player
curl -X POST "http://localhost:8080/players" -H "Content-Type: application/json" -d '{"idPlayer":"player-001","namePlayer":"CyberSamurai","coins":500}'

# Register Attack Mech
curl -X POST "http://localhost:8080/mechs/attack" -H "Content-Type: application/json" -d '{"idMech":"mech-atk-01","playerId":"player-001","model":"Viper-X","maxHealth":100,"battery":80,"attackPower":35,"maxHeat":100}'

# Register Defensive Mech
curl -X POST "http://localhost:8080/mechs/defensive" -H "Content-Type: application/json" -d '{"idMech":"mech-def-01","playerId":"player-001","model":"Aegis-Prime","maxHealth":150,"battery":70,"defenseRating":40,"shieldCapacity":120}'

# Get Player
curl -X GET "http://localhost:8080/players/player-001"

# Get Player Mechs
curl -X GET "http://localhost:8080/mechs?playerId=player-001"
```
