package variosSorteios;

import java.util.*;

/**
 * REVELA — SISTEMA DE SORTEIOS
 *
 * Modos disponíveis:
 *  1. Amigo Secreto       — derangement com revelação individual
 *  2. Sorteio Simples     — N ganhadores aleatórios de uma lista
 *  3. Formação de Times   — divide participantes em grupos
 *  4. Ordem de Apresentação — sequência aleatória numerada
 *
 * Como usar:
 *  javac Revela.java
 *  java  Revela
 */
public class Revela {

    // ─────────────────────────────────────────────────────────
    //  ESTADO COMPARTILHADO — acessível por todos os modos
    // ─────────────────────────────────────────────────────────
    private static final List<String> participantes = new ArrayList<>();
    private static final Scanner      scanner       = new Scanner(System.in);

    // ─────────────────────────────────────────────────────────
    //  CLASSE INTERNA — usada exclusivamente pelo Modo 1
    // ─────────────────────────────────────────────────────────
    static class Participante {
        final String nome;
        Participante amigoSecreto;

        Participante(String nome) { this.nome = nome; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Participante)) return false;
            return nome.equalsIgnoreCase(((Participante) o).nome);
        }

        @Override
        public int hashCode() { return nome.toLowerCase().hashCode(); }
    }

    // ─────────────────────────────────────────────────────────
    //  PONTO DE ENTRADA
    // ─────────────────────────────────────────────────────────
    public static void main(String[] args) {
        exibirCabecalho();
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opcao: ");
            switch (opcao) {
                case 1 -> adicionarParticipante();
                case 2 -> listarParticipantes();
                case 3 -> removerParticipante();
                case 4 -> modoAmigoSecreto();
                case 5 -> modoSorteioSimples();
                case 6 -> modoFormacaoTimes();
                case 7 -> modoOrdemApresentacao();
                case 0 -> System.out.println("\nAte logo!");
                default -> System.out.println("\n[!] Opcao invalida. Tente novamente.");
            }
        } while (opcao != 0);
        scanner.close();
    }

    // ─────────────────────────────────────────────────────────
    //  CABEÇALHO E MENU
    // ─────────────────────────────────────────────────────────
    private static void exibirCabecalho() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       REVELA — SISTEMA DE SORTEIOS       ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│                   MENU                   │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.printf( "│  1. Adicionar participante                │%n");
        System.out.printf( "│  2. Listar participantes (%2d cadastrado(s))│%n", participantes.size());
        System.out.printf( "│  3. Remover participante                  │%n");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  MODOS DE SORTEIO:                       │");
        System.out.println("│  4. Amigo Secreto                        │");
        System.out.println("│  5. Sorteio Simples  (N ganhadores)      │");
        System.out.println("│  6. Formacao de Times                    │");
        System.out.println("│  7. Ordem de Apresentacao                │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  0. Sair                                 │");
        System.out.println("└──────────────────────────────────────────┘");
    }

    // ─────────────────────────────────────────────────────────
    //  GESTÃO DE PARTICIPANTES
    // ─────────────────────────────────────────────────────────
    private static void adicionarParticipante() {
        System.out.println("\n--- ADICIONAR PARTICIPANTE ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("[!] O nome nao pode ser vazio.");
            return;
        }
        if (nome.length() < 2) {
            System.out.println("[!] Nome muito curto.");
            return;
        }
        for (String p : participantes) {
            if (p.equalsIgnoreCase(nome)) {
                System.out.println("[!] \"" + nome + "\" ja esta cadastrado.");
                return;
            }
        }
        participantes.add(nome);
        System.out.println("[+] " + nome + " adicionado. Total: " + participantes.size() + " participante(s).");
    }

    private static void listarParticipantes() {
        System.out.println("\n--- PARTICIPANTES CADASTRADOS ---");
        if (participantes.isEmpty()) {
            System.out.println("Nenhum participante cadastrado ainda.");
            return;
        }
        for (int i = 0; i < participantes.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, participantes.get(i));
        }
        System.out.println("Total: " + participantes.size() + " participante(s).");
    }

    private static void removerParticipante() {
        System.out.println("\n--- REMOVER PARTICIPANTE ---");
        if (participantes.isEmpty()) {
            System.out.println("Nenhum participante cadastrado.");
            return;
        }
        listarParticipantes();
        int numero = lerInteiro("\nNumero do participante a remover (0 para cancelar): ");
        if (numero == 0) return;
        if (numero < 1 || numero > participantes.size()) {
            System.out.println("[!] Numero invalido.");
            return;
        }
        String removido = participantes.remove(numero - 1);
        System.out.println("[-] " + removido + " removido.");
    }

    // ══════════════════════════════════════════════════════════
    //  MODO 1 — AMIGO SECRETO
    //  Algoritmo: derangement — ninguém sorteia a si mesmo
    //  nem forma pares invertidos (A→B e B→A juntos)
    // ══════════════════════════════════════════════════════════
    private static void modoAmigoSecreto() {
        System.out.println("\n══ MODO: AMIGO SECRETO ══");

        if (participantes.size() < 3) {
            System.out.println("[!] Necessario no minimo 3 participantes.");
            System.out.println("    Atual: " + participantes.size() + " participante(s).");
            return;
        }

        // Constrói lista de objetos Participante a partir dos nomes
        List<Participante> doadores   = new ArrayList<>();
        for (String nome : participantes) doadores.add(new Participante(nome));
        List<Participante> receptores = new ArrayList<>(doadores);

        // Embaralha até encontrar um derangement válido
        Random  rng       = new Random();
        boolean valido    = false;
        int     tentativas = 0;
        System.out.print("Sorteando");

        while (!valido && tentativas < 2000) {
            Collections.shuffle(receptores, rng);
            valido = true;

            // Regra 1: auto-sorteio
            for (int i = 0; i < doadores.size(); i++) {
                if (doadores.get(i).equals(receptores.get(i))) { valido = false; break; }
            }

            // Regra 2: pares invertidos
            if (valido) {
                outer:
                for (int i = 0; i < doadores.size(); i++) {
                    for (int j = i + 1; j < doadores.size(); j++) {
                        if (doadores.get(i).equals(receptores.get(j)) &&
                                doadores.get(j).equals(receptores.get(i))) {
                            valido = false;
                            break outer;
                        }
                    }
                }
            }
            tentativas++;
            if (tentativas % 200 == 0) System.out.print(".");
        }
        System.out.println();

        if (!valido) {
            System.out.println("[!] Nao foi possivel gerar sorteio valido.");
            System.out.println("    Tente adicionar mais participantes.");
            return;
        }

        for (int i = 0; i < doadores.size(); i++) {
            doadores.get(i).amigoSecreto = receptores.get(i);
        }

        List<Participante> ordemRevelacao = new ArrayList<>(doadores);
        Collections.shuffle(ordemRevelacao, rng);

        System.out.println("[+] Sorteio realizado! (" + doadores.size() + " participantes)");
        System.out.println("Cada participante vera apenas o seu resultado.");
        System.out.println();
        aguardarEnter("Pressione ENTER para comecar...");

        for (int i = 0; i < ordemRevelacao.size(); i++) {
            Participante atual = ordemRevelacao.get(i);
            limparTela();

            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║       PASSE PARA O PARTICIPANTE:         ║");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.printf( "║   %-40s║%n", atual.nome);
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.printf( "║   Participante %2d de %-20d║%n", i + 1, ordemRevelacao.size());
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println();
            aguardarEnter("  " + atual.nome + ", pressione ENTER para ver seu resultado...");
            limparTela();

            System.out.println("╔══════════════════════════════════════════╗");
            System.out.printf( "║  Ola, %-35s║%n", atual.nome + "!");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.println("║         Seu amigo secreto e:             ║");
            System.out.println("║                                          ║");
            System.out.printf( "║   >>> %-35s║%n", atual.amigoSecreto.nome + " <<<");
            System.out.println("║                                          ║");
            System.out.println("║      Presenteie com muito carinho!       ║");
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println();

            if (i < ordemRevelacao.size() - 1) {
                aguardarEnter("  Memorizou? Pressione ENTER para o proximo...");
                limparTela();
            } else {
                System.out.println("══════════════════════════════════════════");
                System.out.println("  Todos ja sabem seu amigo secreto!");
                System.out.println("  Bom sorteio e boas compras!");
                System.out.println("══════════════════════════════════════════");
                aguardarEnter("\nPressione ENTER para voltar ao menu...");
            }
        }
    }

    // ══════════════════════════════════════════════════════════
    //  MODO 2 — SORTEIO SIMPLES
    //  Sorteia N ganhadores aleatórios da lista de participantes
    // ══════════════════════════════════════════════════════════
    private static void modoSorteioSimples() {
        System.out.println("\n══ MODO: SORTEIO SIMPLES ══");

        if (participantes.size() < 2) {
            System.out.println("[!] Necessario no minimo 2 participantes.");
            return;
        }

        System.out.println("Participantes disponiveis: " + participantes.size());
        int n = lerInteiro("Quantos ganhadores sortear? (1 a " + participantes.size() + "): ");

        if (n < 1 || n > participantes.size()) {
            System.out.println("[!] Numero invalido.");
            return;
        }

        // Copia e embaralha — os N primeiros são os ganhadores
        List<String> copia = new ArrayList<>(participantes);
        Collections.shuffle(copia, new Random());

        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║          RESULTADO DO SORTEIO            ║");
        System.out.println("╠══════════════════════════════════════════╣");

        for (int i = 0; i < n; i++) {
            String lugar;
            if      (i == 0) lugar = "1o lugar ";
            else if (i == 1) lugar = "2o lugar ";
            else if (i == 2) lugar = "3o lugar ";
            else             lugar = (i + 1) + "o lugar";
            System.out.printf("║  %s  %-30s║%n", lugar, copia.get(i));
        }

        System.out.println("╚══════════════════════════════════════════╝");

        if (n < participantes.size()) {
            System.out.println("\nNao sorteados (" + (participantes.size() - n) + "):");
            for (int i = n; i < copia.size(); i++) {
                System.out.println("  - " + copia.get(i));
            }
        }

        aguardarEnter("\nPressione ENTER para voltar ao menu...");
    }

    // ══════════════════════════════════════════════════════════
    //  MODO 3 — FORMAÇÃO DE TIMES
    //  Divide os participantes em grupos equilibrados.
    //  O usuário escolhe por número de times ou por tamanho.
    // ══════════════════════════════════════════════════════════
    private static void modoFormacaoTimes() {
        System.out.println("\n══ MODO: FORMACAO DE TIMES ══");

        if (participantes.size() < 2) {
            System.out.println("[!] Necessario no minimo 2 participantes.");
            return;
        }

        System.out.println("Participantes disponiveis: " + participantes.size());
        System.out.println("Como deseja dividir?");
        System.out.println("  1. Por numero de times");
        System.out.println("  2. Por tamanho de cada time");
        int escolha = lerInteiro("Opcao: ");

        int numTimes;

        if (escolha == 1) {
            numTimes = lerInteiro("Numero de times (2 a " + participantes.size() + "): ");
            if (numTimes < 2 || numTimes > participantes.size()) {
                System.out.println("[!] Numero invalido.");
                return;
            }
        } else if (escolha == 2) {
            int max = participantes.size() / 2;
            int tamTime = lerInteiro("Membros por time (1 a " + max + "): ");
            if (tamTime < 1 || tamTime > max) {
                System.out.println("[!] Tamanho invalido.");
                return;
            }
            numTimes = (int) Math.ceil((double) participantes.size() / tamTime);
        } else {
            System.out.println("[!] Opcao invalida.");
            return;
        }

        // Embaralha e distribui em round-robin
        List<String> embaralhados = new ArrayList<>(participantes);
        Collections.shuffle(embaralhados, new Random());

        List<List<String>> times = new ArrayList<>();
        for (int i = 0; i < numTimes; i++) times.add(new ArrayList<>());
        for (int i = 0; i < embaralhados.size(); i++) {
            times.get(i % numTimes).add(embaralhados.get(i));
        }

        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║             TIMES FORMADOS               ║");
        System.out.println("╠══════════════════════════════════════════╣");
        for (int i = 0; i < times.size(); i++) {
            String membros = String.join(", ", times.get(i));
            System.out.printf("║  Time %-2d  (%d): %-26s║%n",
                    i + 1, times.get(i).size(), membros);
        }
        System.out.println("╚══════════════════════════════════════════╝");

        int base  = embaralhados.size() / numTimes;
        int extra = embaralhados.size() % numTimes;
        if (extra != 0) {
            System.out.printf("%n[i] %d time(s) com %d membro(s), %d time(s) com %d membro(s).%n",
                    extra, base + 1, numTimes - extra, base);
        }

        aguardarEnter("\nPressione ENTER para voltar ao menu...");
    }

    // ══════════════════════════════════════════════════════════
    //  MODO 4 — ORDEM DE APRESENTAÇÃO
    //  Gera uma sequência aleatória numerada da lista completa
    // ══════════════════════════════════════════════════════════
    private static void modoOrdemApresentacao() {
        System.out.println("\n══ MODO: ORDEM DE APRESENTACAO ══");

        if (participantes.isEmpty()) {
            System.out.println("[!] Nenhum participante cadastrado.");
            return;
        }

        List<String> ordem = new ArrayList<>(participantes);
        Collections.shuffle(ordem, new Random());

        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║          ORDEM DE APRESENTACAO           ║");
        System.out.println("╠══════════════════════════════════════════╣");
        for (int i = 0; i < ordem.size(); i++) {
            System.out.printf("║  %2d.  %-36s║%n", i + 1, ordem.get(i));
        }
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println();
        System.out.println("[i] Execute este modo novamente para gerar uma nova ordem.");
        aguardarEnter("\nPressione ENTER para voltar ao menu...");
    }

    // ─────────────────────────────────────────────────────────
    //  UTILITÁRIOS
    // ─────────────────────────────────────────────────────────

    /** Lê um inteiro, rejeitando entradas não numéricas. */
    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("[!] Digite um numero valido: ");
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    /** Pausa até o usuário pressionar Enter. */
    private static void aguardarEnter(String mensagem) {
        System.out.print(mensagem);
        scanner.nextLine();
    }

    /** Limpa o terminal de forma compatível com Windows, Linux e macOS. */
    private static void limparTela() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}