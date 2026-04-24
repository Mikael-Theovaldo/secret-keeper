package projectTerminal;
import java.util.*;

/**
 * SORTEADOR AMIGO SECRETO — TERMINAL
 *
 * Funcionalidades:
 *  - Cadastro de participantes (mínimo 3)
 *  - Sorteio sem auto-sorteio e sem pares invertidos
 *  - Revelação individual: cada participante vê apenas o seu resultado
 *
 * Como usar:
 *  - javac AmigoSecreto.java
 *  - java AmigoSecreto
 */
public class AmigoSecreto {

    // ─────────────────────────────────────────────────────────
    //  ESTADO DA APLICAÇÃO
    // ─────────────────────────────────────────────────────────
    private static final List<Participante> participantes  = new ArrayList<>();
    private static final List<Participante> ordemRevelacao = new ArrayList<>();
    public static final Scanner            scanner        = new Scanner(System.in);

    // ─────────────────────────────────────────────────────────
    //  CLASSE PARTICIPANTE
    // ─────────────────────────────────────────────────────────
    public static class Participante {
        final String nome;
        Participante amigoSecreto;

        Participante(String nome) {
            this.nome = nome;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Participante)) return false;
            return nome.equalsIgnoreCase(((Participante) o).nome);
        }

        @Override
        public int hashCode() {
            return nome.toLowerCase().hashCode();
        }
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
                case 4 -> realizarSorteio();
                case 5 -> iniciarRevelacao();
                case 0 -> System.out.println("\nAte logo!");
                default -> System.out.println("\n[!] Opcao invalida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    // ─────────────────────────────────────────────────────────
    //  MENU
    // ─────────────────────────────────────────────────────────
    private static void exibirCabecalho() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║         SORTEADOR  AMIGO SECRETO         ║");
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
        System.out.printf( "│  4. Realizar sorteio                      │%n");
        System.out.printf( "│  5. Iniciar revelacao individual          │%n");
        System.out.printf( "│  0. Sair                                  │%n");
        System.out.println("└──────────────────────────────────────────┘");
    }

    // ─────────────────────────────────────────────────────────
    //  1. ADICIONAR PARTICIPANTE
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
        for (Participante p : participantes) {
            if (p.nome.equalsIgnoreCase(nome)) {
                System.out.println("[!] \"" + nome + "\" ja esta cadastrado.");
                return;
            }
        }

        participantes.add(new Participante(nome));
        ordemRevelacao.clear(); // sorteio anterior fica invalidado
        System.out.println("[+] " + nome + " adicionado. Total: " + participantes.size() + " participante(s).");
    }

    // ─────────────────────────────────────────────────────────
    //  2. LISTAR PARTICIPANTES
    // ─────────────────────────────────────────────────────────
    private static void listarParticipantes() {
        System.out.println("\n--- PARTICIPANTES CADASTRADOS ---");
        if (participantes.isEmpty()) {
            System.out.println("Nenhum participante cadastrado ainda.");
            return;
        }
        for (int i = 0; i < participantes.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, participantes.get(i).nome);
        }
        System.out.println("Total: " + participantes.size() + " participante(s).");
    }

    // ─────────────────────────────────────────────────────────
    //  3. REMOVER PARTICIPANTE
    // ─────────────────────────────────────────────────────────
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

        Participante removido = participantes.remove(numero - 1);
        ordemRevelacao.clear(); // sorteio anterior fica invalidado
        System.out.println("[-] " + removido.nome + " removido.");
    }

    // ─────────────────────────────────────────────────────────
    //  4. REALIZAR SORTEIO
    // ─────────────────────────────────────────────────────────
    private static void realizarSorteio() {
        System.out.println("\n--- REALIZAR SORTEIO ---");

        if (participantes.size() < 3) {
            System.out.println("[!] E necessario no minimo 3 participantes.");
            System.out.println("    Atual: " + participantes.size() + " participante(s).");
            return;
        }

        List<Participante> receptores = new ArrayList<>(participantes);
        Random rng = new Random();
        boolean valido = false;
        int tentativas = 0;

        System.out.print("Sorteando");
        while (!valido && tentativas < 2000) {
            Collections.shuffle(receptores, rng);
            valido = true;

            // Regra 1: ninguem tira a si mesmo
            for (int i = 0; i < participantes.size(); i++) {
                if (participantes.get(i).equals(receptores.get(i))) {
                    valido = false;
                    break;
                }
            }

            // Regra 2: sem pares invertidos (A->B e B->A ao mesmo tempo)
            if (valido) {
                for (int i = 0; i < participantes.size(); i++) {
                    for (int j = i + 1; j < participantes.size(); j++) {
                        if (participantes.get(i).equals(receptores.get(j)) &&
                                participantes.get(j).equals(receptores.get(i))) {
                            valido = false;
                            break;
                        }
                    }
                    if (!valido) break;
                }
            }

            tentativas++;
            if (tentativas % 200 == 0) System.out.print(".");
        }
        System.out.println();

        if (!valido) {
            System.out.println("[!] Nao foi possivel gerar um sorteio valido.");
            System.out.println("    Tente adicionar mais participantes.");
            return;
        }

        // Atribui os amigos secretos
        for (int i = 0; i < participantes.size(); i++) {
            participantes.get(i).amigoSecreto = receptores.get(i);
        }

        // Monta ordem de revelacao embaralhada (nao segue a ordem de cadastro)
        ordemRevelacao.clear();
        ordemRevelacao.addAll(participantes);
        Collections.shuffle(ordemRevelacao, rng);

        System.out.println("[+] Sorteio realizado com sucesso! (" + participantes.size() + " participantes)");
        System.out.println("    Use a opcao 5 para iniciar a revelacao individual.");
    }

    // ─────────────────────────────────────────────────────────
    //  5. REVELAÇÃO INDIVIDUAL
    // ─────────────────────────────────────────────────────────
    private static void iniciarRevelacao() {
        System.out.println("\n--- REVELACAO INDIVIDUAL ---");

        if (ordemRevelacao.isEmpty()) {
            System.out.println("[!] O sorteio ainda nao foi realizado.");
            System.out.println("    Use a opcao 4 primeiro.");
            return;
        }

        System.out.println("Cada participante vera apenas o seu resultado.");
        System.out.println("Os outros devem olhar para o lado durante a revelacao.");
        System.out.println();
        aguardarEnter("Pressione ENTER para comecar...");

        for (int i = 0; i < ordemRevelacao.size(); i++) {
            Participante atual = ordemRevelacao.get(i);

            limparTela();

            // ── Tela de passagem ──────────────────────────
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║       PASSE PARA O PARTICIPANTE:         ║");
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.printf( "║   %-40s║%n", atual.nome);
            System.out.println("╠══════════════════════════════════════════╣");
            System.out.printf( "║   Participante %2d de %-20d║%n", i + 1, ordemRevelacao.size());
            System.out.println("╚══════════════════════════════════════════╝");
            System.out.println();
            System.out.println("  Somente " + atual.nome + " deve olhar a tela agora.");
            System.out.println();
            aguardarEnter("  " + atual.nome + ", pressione ENTER para ver seu resultado...");

            limparTela();

            // ── Tela de revelação ─────────────────────────
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
                aguardarEnter("  Memorizou? Pressione ENTER para chamar o proximo...");
                limparTela();
            } else {
                System.out.println("══════════════════════════════════════════");
                System.out.println("  Todos ja sabem seu amigo secreto!");
                System.out.println("  Bom sorteio e boas compras!");
                System.out.println("══════════════════════════════════════════");
                System.out.println();
                aguardarEnter("Pressione ENTER para voltar ao menu...");
            }
        }
    }

    // ─────────────────────────────────────────────────────────
    //  UTILITÁRIOS
    // ─────────────────────────────────────────────────────────

    /** Lê um inteiro do terminal, repetindo caso o valor seja inválido. */
    private static int lerInteiro(String mensagem) {
        System.out.print(mensagem);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("[!] Digite um numero valido: ");
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpa o buffer após nextInt()
        return valor;
    }

    /** Pausa até o usuário pressionar Enter. */
    private static void aguardarEnter(String mensagem) {
        System.out.print(mensagem);
        scanner.nextLine();
    }

    /**
     * Limpa o terminal.
     * Usa sequência ANSI no Linux/macOS e "cls" no Windows.
     * Caso ambos falhem, imprime linhas em branco como alternativa.
     */
    private static void limparTela() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) System.out.println();
        }
    }
}