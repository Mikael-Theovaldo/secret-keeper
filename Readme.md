# Secret Keeper

**Sistema Interativo de Sorteios para Terminal**

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/technologies/javase-downloads.html)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Version](https://img.shields.io/badge/version-1.0.0-lightgrey)]()

---

## Sumário

- [Visão Geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Executar](#como-executar)
- [Manual do Usuário](#manual-do-usuário)
- [Modos de Sorteio](#modos-de-sorteio)
- [Contribuidores](#contribuidores)

---

## Visão Geral

O **Secret Keeper** é uma aplicação em Java projetada para realizar sorteios de forma justa, transparente e interativa, diretamente no terminal. Desenvolvido sob os princípios da **Orientação a Objetos** e **Arquitetura em Camadas**, o sistema garante imparcialidade e sigilo, sendo ideal para dinâmicas de grupo presenciais sem necessidade de internet.

**Principais características:**
- Execução 100% local (offline)
- Interface de linha de comando (CLI) intuitiva
- Cinco modos distintos de sorteio
- Algoritmo de *derangement* para Amigo Secreto
- Distribuição equilibrada para formação de times

---

## Funcionalidades

| Módulo | Descrição |
|--------|------------|
| **Gerenciamento de Participantes** | Adicionar, listar e remover nomes. |
| **Amigo Secreto** | Algoritmo de desarranjo (ninguém sorteia a si mesmo, sem pares invertidos). Revelação individual com limpeza de tela. |
| **Sorteio Simples** | Seleção aleatória de N ganhadores. Resultados ordenados por classificação. |
| **Formação de Times** | Divisão por número de times ou tamanho dos grupos. Distribuição *round-robin* para equilíbrio. |
| **Ordem de Apresentação** | Sequência embaralhada e numerada dos participantes. |
| **Sorteio de Números** | Sorteio de números dentro de um intervalo definido pelo usuário. Permite escolher quantidade de números e ordem de exibição (aleatória, crescente ou decrescente). |
---

## Tecnologias Utilizadas

| Categoria | Tecnologia |
|-----------|-------------|
| Linguagem | Java 17+ |
| Paradigma | Orientado a Objetos |
| Ambiente | Terminal (CLI) |
| Controle de versão | Git + GitHub |
| Build | Compilação manual (javac) |



---

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas para separar responsabilidades:



| Diretório | Responsabilidade |
|-----------|------------------|
| `model/` | Entidades do sistema (Participante) |
| `repository/` | Persistência em memória dos dados |
| `service/` | Regras de negócio e algoritmos de sorteio |
| `views/` | Interação com o usuário (interface CLI) |
| `util/` | Helpers, validação e formatação |
| `Main.java` | Ponto de entrada da aplicação |



---

## Como Executar

### Pré-requisitos
- Java Development Kit (JDK) 17 ou superior instalado
- Terminal (Linux, macOS, WSL no Windows ou CMD/PowerShell)

### Passo a passo

1. **Clone o repositório**
   ```bash
   git clone https://github.com/Mikael-Theovaldo/secret-keeper.git
   cd secret-keeper

2. Compile o código 
   ```bash
   javac src/main/java/con/secretKeeper/Main.java 

3. Execute o sistema
   ```bash
    java -cp src Main
## Manual do Usuário

### Menu Principal ###

**Ao executar o programa, você verá as seguintes opções:**

| Opção | Funcionalidade |
|-------|----------------|
| 1 | Adicionar participante |
| 2 | Listar participantes |
| 3 | Remover participante |
| 4 | Amigo secreto |
| 5 | Sorteio simples |
| 6 | Formação de times |
| 7 | Ordem de apresentação |
| 8 | Sortear número aleatório |
| 9 | Sair |


### Gerenciamento de Participantes

- **1 - Adicionar nome:** Insira um nome único (case-sensitive: "Ana" ≠ "ana").
- **2 - Listar participantes:** Exibe índice e nome.
- **3 - Remover por índice:** Exclui participante com base na posição listada.


### Realizando um Sorteio

Cada modo possui requisitos mínimos de participantes:

- **Amigo Secreto:** mínimo 3 participantes.
- **Sorteio Simples:** mínimo 2 participantes.
- **Formação de Times:** mínimo 2 participantes.
- **Ordem de Apresentação:** mínimo 1 participante.

> **Importante:** O sistema sempre valida os pré-requisitos antes de executar o sorteio. Se a lista estiver vazia ou insuficiente, uma mensagem de erro será exibida.

---

## Modos de Sorteio

### 1. Amigo Secreto

- **Algoritmo:** Derangement (permutação sem pontos fixos e sem pares invertidos A→B e B→A)
- **Revelação:** Individual, com pausa e limpeza de tela a cada par revelado.
- **Garantias:** Ninguém tira a si mesmo; pares mútuos (A↔B) são evitados.

### 2. Sorteio Simples

- **Entrada:** Número de ganhadores (1 ≤ N ≤ total participantes).
- **Saída:** Lista ordenada de vencedores (ex.: "1º: João", "2º: Maria").

### 3. Formação de Times

- **Entrada:** Número de times **ou** tamanho dos grupos.
- **Distribuição:** Round-robin (melhor equilíbrio possível).
- **Exemplo:** 7 participantes em 3 times → Times com 3, 2 e 2 integrantes.

### 4. Ordem de Apresentação

- **Entrada:** Lista de participantes.
- **Saída:** Sequência embaralhada e numerada.
- **Exemplo:**

   ```bash
    1. Ana
    2. João
    3. Maria

### 5. Sorteio de Números

- **Entradas:**
  - Intervalo inicial (mínimo)
  - Intervalo final (máximo)
  - Quantidade de números a serem sorteados
  - Ordem de exibição: aleatória, crescente ou decrescente

- **Regras:**
  - O intervalo deve ser válido (inicial < final)
  - A quantidade sorteada não pode exceder o tamanho do intervalo

- **Exemplo de saída (ordem crescente):**
  - Intervalo: 1 a 50 | Quantidade: 5 | Ordem: Crescente
  - Resultado: 07, 12, 23, 34, 45

- **Exemplo de saída (ordem decrescente):**
  - Resultado: 45, 34, 23, 12, 07

- **Exemplo de saída (ordem aleatória):**
  - Resultado: 23, 45, 07, 34, 12
  

---

## Contribuidores

Projeto desenvolvido por:

| Nome | GitHub |
|------|--------|
| **Mikael Theovaldo** | [@Mikael-Theovaldo](https://github.com/Mikael-Theovaldo) |
| **Victor** | [@victor-coffee](https://github.com/victor-coffee) |
| **João** | [@rocksDjoao](https://github.com/rocksDjoao) |

---


> **Nota final:** O Secret Keeper foi desenvolvido como um trabalho acadêmico aplicando conceitos de POO, camadas arquiteturais e lógica de algoritmos de sorteio. Contribuições são bem-vindas através de issues ou pull requests.


