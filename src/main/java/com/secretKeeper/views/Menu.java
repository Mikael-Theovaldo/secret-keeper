package main.java.com.secretKeeper.views;

import main.java.com.secretKeeper.model.Participante;
import main.java.com.secretKeeper.repository.ParticipanteRepository;
import main.java.com.secretKeeper.service.*;
import main.java.com.secretKeeper.util.EntradaUtil;

import java.util.List;

public class Menu {

    // ─────────────────────────────────────────────
    //  Dependências
    // ─────────────────────────────────────────────
    private final ParticipanteRepository    repo                  = new ParticipanteRepository();
    private final AmigoSecretoService       amigoSecretoService   = new AmigoSecretoService();
    private final SorteioSimplesService     sorteioSimplesService = new SorteioSimplesService();
    private final FormacaoTimesServices     formacaoTimesService  = new FormacaoTimesServices();
    private final OrdemApresentacaoService  ordemService          = new OrdemApresentacaoService();
    private final SorteioNumeroService      numeroService         = new SorteioNumeroService();

    // ─────────────────────────────────────────────
    //  Ponto de entrada do menu
    // ─────────────────────────────────────────────
    public void iniciar() {
        exibirCabecalho();
        int opcao;

        do {
            exibirMenu();
            opcao = EntradaUtil.lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1  -> adicionarParticipante();
                case 2  -> listarParticipantes();
                case 3  -> removerParticipante();
                case 4  -> modoAmigoSecreto();
                case 5  -> modoSorteioSimples();
                case 6  -> modoFormacaoTimes();
                case 7  -> modoOrdemApresentacao();
                case 8  -> modoSorteioNumero();
                case 0  -> System.out.println("\nAte logo!");
                default -> System.out.println("\n[!] Opcao invalida. Tente novamente.");
            }

        } while (opcao != 0);
    }

    // ─────────────────────────────────────────────
    //  Cabeçalho e Menu
    // ─────────────────────────────────────────────
    private void exibirCabecalho() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       REVELA — SISTEMA DE SORTEIOS       ║");
        System.out.println("╚══════════════════════════════════════════╝");
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("┌──────────────────────────────────────────┐");
        System.out.println("│                   MENU                   │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.printf( "│  1. Adicionar participante                │%n");
        System.out.printf( "│  2. Listar participantes (%2d cadastrado(s))│%n", repo.totalParticipante());
        System.out.printf( "│  3. Remover participante                  │%n");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  MODOS DE SORTEIO:                       │");
        System.out.println("│  4. Amigo Secreto                        │");
        System.out.println("│  5. Sorteio Simples  (N ganhadores)      │");
        System.out.println("│  6. Formacao de Times                    │");
        System.out.println("│  7. Ordem de Apresentacao                │");
        System.out.println("│  8. Sorteio de Numeros                   │");
        System.out.println("├──────────────────────────────────────────┤");
        System.out.println("│  0. Sair                                 │");
        System.out.println("└──────────────────────────────────────────┘");
    }

    // ─────────────────────────────────────────────
    //  Gestão de participantes
    // ─────────────────────────────────────────────
    private void adicionarParticipante() {
        System.out.println("\n--- ADICIONAR PARTICIPANTE ---");
        String nome = EntradaUtil.lerTexto("Nome: ");

        if (nome.isEmpty()) {
            System.out.println("[!] O nome nao pode ser vazio.");
            return;
        }
        if (nome.length() < 2) {
            System.out.println("[!] Nome muito curto.");
            return;
        }
        if (repo.existe(nome)) {
            System.out.println("[!] \"" + nome + "\" ja esta cadastrado.");
            return;
        }

        repo.adicionar(nome);
        System.out.println("[+] " + nome + " adicionado. Total: " + repo.totalParticipante() + " participante(s).");
    }

    private void listarParticipantes() {
        System.out.println("\n--- PARTICIPANTES CADASTRADOS ---");
        List<String> lista = repo.listar();

        if (lista.isEmpty()) {
            System.out.println("Nenhum participante cadastrado ainda.");
            return;
        }
        for (int i = 0; i < lista.size(); i++) {
            System.out.printf("  %2d. %s%n", i + 1, lista.get(i));
        }
        System.out.println("Total: " + lista.size() + " participante(s).");
    }

    private void removerParticipante() {
        System.out.println("\n--- REMOVER PARTICIPANTE ---");

        if (repo.listar().isEmpty()) {
            System.out.println("Nenhum participante cadastrado.");
            return;
        }

        listarParticipantes();
        int numero = EntradaUtil.lerInteiro("\nNumero do participante a remover (0 para cancelar): ");

        if (numero == 0) return;
        if (numero < 1 || numero > repo.totalParticipante()) {
            System.out.println("[!] Numero invalido.");
            return;
        }

        String removido = repo.listar().get(numero - 1);
        repo.remover(numero - 1);
        System.out.println("[-] " + removido + " removido.");
    }

    // ─────────────────────────────────────────────
    //  Modo 1 — Amigo Secreto
    // ─────────────────────────────────────────────
    private void modoAmigoSecreto() {
        System.out.println("\n══ MODO: AMIGO SECRETO ══");

        if (repo.totalParticipante() < 3) {
            System.out.println("[!] Necessario no minimo 3 participantes.");
            System.out.println("    Atual: " + repo.totalParticipante() + " participante(s).");
            return;
        }

        try {
            List<Participante> resultado = amigoSecretoService.sortear(repo.listar());

            System.out.println("[+] Sorteio realizado! (" + resultado.size() + " participantes)");
            System.out.println("Cada participante vera apenas o seu resultado.");
            aguardarEnter("\nPressione ENTER para comecar...");

            for (int i = 0; i < resultado.size(); i++) {
                Participante atual = resultado.get(i);
                limparTela();

                System.out.println("╔══════════════════════════════════════════╗");
                System.out.println("║       PASSE PARA O PARTICIPANTE:         ║");
                System.out.println("╠══════════════════════════════════════════╣");
                System.out.printf( "║   %-40s║%n", atual.getNome());
                System.out.printf( "║   Participante %2d de %-20d║%n", i + 1, resultado.size());
                System.out.println("╚══════════════════════════════════════════╝");
                aguardarEnter("\n  " + atual.getNome() + ", pressione ENTER para ver seu resultado...");
                limparTela();

                System.out.println("╔══════════════════════════════════════════╗");
                System.out.printf( "║  Ola, %-35s║%n", atual.getNome() + "!");
                System.out.println("╠══════════════════════════════════════════╣");
                System.out.println("║         Seu amigo secreto e:             ║");
                System.out.println("║                                          ║");
                System.out.printf( "║   >>> %-35s║%n", atual.getAmigoSecreto().getNome() + " <<<");
                System.out.println("║                                          ║");
                System.out.println("║      Presenteie com muito carinho!       ║");
                System.out.println("╚══════════════════════════════════════════╝");

                if (i < resultado.size() - 1) {
                    aguardarEnter("\n  Memorizou? Pressione ENTER para o proximo...");
                    limparTela();
                } else {
                    System.out.println("\n══════════════════════════════════════════");
                    System.out.println("  Todos ja sabem seu amigo secreto!");
                    System.out.println("══════════════════════════════════════════");
                    aguardarEnter("\nPressione ENTER para voltar ao menu...");
                }
            }

        } catch (RuntimeException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  Modo 2 — Sorteio Simples
    // ─────────────────────────────────────────────
    private void modoSorteioSimples() {
        System.out.println("\n══ MODO: SORTEIO SIMPLES ══");

        if (repo.totalParticipante() < 2) {
            System.out.println("[!] Necessario no minimo 2 participantes.");
            return;
        }

        System.out.println("Participantes disponiveis: " + repo.totalParticipante());
        int n = EntradaUtil.lerInteiro("Quantos ganhadores sortear? (1 a " + repo.totalParticipante() + "): ");

        try {
            List<String> sorteados    = sorteioSimplesService.sortear(repo.listar(), n);
            List<String> naoSorteados = sorteioSimplesService.getNaoSorteados(repo.listar(), sorteados);

            System.out.println();
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║          RESULTADO DO SORTEIO            ║");
            System.out.println("╠══════════════════════════════════════════╣");

            for (int i = 0; i < sorteados.size(); i++) {
                System.out.printf("║  %-8s  %-30s║%n",
                        sorteioSimplesService.getLugar(i), sorteados.get(i));
            }

            System.out.println("╚══════════════════════════════════════════╝");

            if (!naoSorteados.isEmpty()) {
                System.out.println("\nNao sorteados (" + naoSorteados.size() + "):");
                naoSorteados.forEach(p -> System.out.println("  - " + p));
            }

        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }

        aguardarEnter("\nPressione ENTER para voltar ao menu...");
    }

    // ─────────────────────────────────────────────
    //  Modo 3 — Formação de Times
    // ─────────────────────────────────────────────
    private void modoFormacaoTimes() {
        System.out.println("\n══ MODO: FORMACAO DE TIMES ══");

        if (repo.totalParticipante() < 2) {
            System.out.println("[!] Necessario no minimo 2 participantes.");
            return;
        }

        System.out.println("Participantes disponiveis: " + repo.totalParticipante());
        System.out.println("Como deseja dividir?");
        System.out.println("  1. Por numero de times");
        System.out.println("  2. Por tamanho de cada time");
        int escolha = EntradaUtil.lerInteiro("Opcao: ");

        int numTimes;

        if (escolha == 1) {
            numTimes = EntradaUtil.lerInteiro("Numero de times (2 a " + repo.totalParticipante() + "): ");
            if (numTimes < 2 || numTimes > repo.totalParticipante()) {
                System.out.println("[!] Numero invalido.");
                return;
            }
        } else if (escolha == 2) {
            int max = repo.totalParticipante() / 2;
            int tamTime = EntradaUtil.lerInteiro("Membros por time (1 a " + max + "): ");
            if (tamTime < 1 || tamTime > max) {
                System.out.println("[!] Tamanho invalido.");
                return;
            }
            numTimes = (int) Math.ceil((double) repo.totalParticipante() / tamTime);
        } else {
            System.out.println("[!] Opcao invalida.");
            return;
        }

        List<List<String>> times = formacaoTimesService.formarTimes(repo.listar(), numTimes);

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

        aguardarEnter("\nPressione ENTER para voltar ao menu...");
    }

    // ─────────────────────────────────────────────
    //  Modo 4 — Ordem de Apresentação
    // ─────────────────────────────────────────────
    private void modoOrdemApresentacao() {
        System.out.println("\n══ MODO: ORDEM DE APRESENTACAO ══");

        try {
            List<String> ordem = ordemService.gerarOrdem(repo.listar());

            System.out.println();
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.println("║          ORDEM DE APRESENTACAO           ║");
            System.out.println("╠══════════════════════════════════════════╣");
            for (int i = 0; i < ordem.size(); i++) {
                System.out.printf("║  %2d.  %-36s║%n", i + 1, ordem.get(i));
            }
            System.out.println("╚══════════════════════════════════════════╝");

        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }

        aguardarEnter("\nPressione ENTER para voltar ao menu...");
    }

    // ─────────────────────────────────────────────
    //  Modo 5 — Sorteio de Números
    // ─────────────────────────────────────────────
    private void modoSorteioNumero() {
        System.out.println("\n══ MODO: SORTEIO DE NUMEROS ══");

        int min = EntradaUtil.lerInteiro("Numero minimo: ");
        int max = EntradaUtil.lerInteiro("Numero maximo: ");
        int quantidade = EntradaUtil.lerInteiro("Quantos numeros sortear? ");

        // Repetição
        System.out.println("\nPermitir repeticao de numeros?");
        System.out.println("  1. Sim");
        System.out.println("  2. Nao");
        int opcaoRepeticao = EntradaUtil.lerInteiro("Opcao: ");
        boolean comRepeticao = (opcaoRepeticao == 1);

        // Ordenação
        System.out.println("\nComo exibir o resultado?");
        System.out.println("  1. Aleatorio");
        System.out.println("  2. Crescente");
        System.out.println("  3. Decrescente");
        int opcaoOrdem = EntradaUtil.lerInteiro("Opcao: ");

        if (opcaoOrdem < 1 || opcaoOrdem > 3) {
            System.out.println("[!] Opcao de ordem invalida.");
            return;
        }

        SorteioNumeroService.OrdemResultado ordem = switch (opcaoOrdem) {
            case 2  -> SorteioNumeroService.OrdemResultado.CRESCENTE;
            case 3  -> SorteioNumeroService.OrdemResultado.DECRESCENTE;
            default -> SorteioNumeroService.OrdemResultado.ALEATORIO;
        };

        try {
            List<Integer> resultado = comRepeticao
                    ? numeroService.sortearComRepeticao(min, max, quantidade, ordem)
                    : numeroService.sortearSemRepeticao(min, max, quantidade, ordem);

            String tipoOrdem = switch (opcaoOrdem) {
                case 2  -> "CRESCENTE";
                case 3  -> "DECRESCENTE";
                default -> "ALEATORIO";
            };

            System.out.println();
            System.out.println("╔══════════════════════════════════════════╗");
            System.out.printf( "║     RESULTADO — ORDEM %-19s║%n", tipoOrdem);
            System.out.printf( "║     Intervalo: [%d - %d]%-19s║%n", min, max, "");
            System.out.printf( "║     Repeticao: %-26s║%n", comRepeticao ? "Sim" : "Nao");
            System.out.println("╠══════════════════════════════════════════╣");
            for (int i = 0; i < resultado.size(); i++) {
                System.out.printf("║  %2d.  %-36d║%n", i + 1, resultado.get(i));
            }
            System.out.println("╚══════════════════════════════════════════╝");

        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }

        aguardarEnter("\nPressione ENTER para voltar ao menu...");
    }

    // ─────────────────────────────────────────────
    //  Utilitários
    // ─────────────────────────────────────────────
    private void aguardarEnter(String mensagem) {
        EntradaUtil.lerTexto(mensagem);
    }

    private void limparTela() {
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