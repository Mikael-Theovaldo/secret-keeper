#  Secret Keeper — Sistema Interativo de Sorteios

**Linguagem:** Java  
**Paradigma:** Orientado a Objetos  
**Arquitetura:** Camadas (Model · Repository · Service · Views · Util)  
**Execução:** Terminal (CLI)

---

##  Sobre o projeto
Secret Keeper é um sistema de sorteios interativo para uso presencial em grupo.  
Executado em um único dispositivo, garante **imparcialidade**, **sigilo** e **simplicidade**, sem depender de internet ou bibliotecas externas.

O código segue boas práticas de **orientação a objetos** e **arquitetura em camadas**, facilitando manutenção e expansão.

---

##  Objetivo
Fornecer uma ferramenta confiável para sorteios em grupo, desde o clássico **Amigo Secreto** até formações de times e ordens de apresentação, sempre com regras claras e resultados justos.

---

##  Funcionalidades

###  Participantes
- **Adicionar** nomes (sem duplicatas)
- **Listar** todos cadastrados
- **Remover** pelo índice da lista

###  Modos de sorteio
1. **Amigo Secreto**
    - Algoritmo de *derangement* (ninguém sorteia a si mesmo, sem pares invertidos)
    - Revelação individual com limpeza de tela
    - Mínimo: 3 participantes

2. **Sorteio Simples**
    - Seleção aleatória de N ganhadores
    - Resultado em ordem de classificação
    - Mínimo: 2 participantes

3. **Formação de Times**
    - Divisão por número de times ou tamanho dos grupos
    - Distribuição *round-robin* para equilíbrio
    - Mínimo: 2 participantes

4. **Ordem de Apresentação**
    - Sequência embaralhada e numerada
    - Mínimo: 1 participante

---

##  Estrutura do projeto

