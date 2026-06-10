package main.java.com.secretKeeper.views;

import main.java.com.secretKeeper.model.Participante;
import main.java.com.secretKeeper.service.AmigoSecretoService;
import main.java.com.secretKeeper.service.FormacaoTimesService;
import main.java.com.secretKeeper.service.OrdemApresentacaoService;
import main.java.com.secretKeeper.service.SorteioNumeroService;
import main.java.com.secretKeeper.service.SorteioSimplesService;
import main.java.com.secretKeeper.util.EntradaUtil;
import main.java.com.secretKeeper.repository.ParticipanteRepository;

import java.util.List;

public class Menu {


    public void iniciar() {
        int opcao;

        do {
            exibirMenu();
            opcao = EntradaUtil.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    System.out.println("\n[Adicionar participante]");
                    String nome =
                            EntradaUtil.lerTexto("Nome: ");
                    ParticipanteRepository.adicionar(nome);
                    break;

                case 2:
                    System.out.println("\n[Listar participantes]");
                    ParticipanteRepository.listar().forEach(System.out::println);
                    break;

                case 3:

                    // implementar o indice indicando a sequencia de participantes ex: 1 - theo 2 - joao ...
                    System.out.println("\n[Remover participante]");
                    int indice =
                            EntradaUtil.lerInteiro("Informe o índice:");
                    ParticipanteRepository.remover(indice);
                    break;

                case 4:
                    // problema : nome titando o proprio nome
                    System.out.println("\n[Amigo secreto]");
                    List<String> participantes = ParticipanteRepository.listar();

                    try {
                        List<Participante> resultado = AmigoSecretoService.sortear(participantes);

                        System.out.println("Resultado do sorteio:");
                        for (Participante p : resultado) {
                            System.out.println(p.getNome() + " -> " + p.getAmigoSecreto().getNome());
                        }
                    } catch (RuntimeException e) {
                        System.out.println("Erro: " + e.getMessage());
                        System.out.println("Voltando ao menu inicial...");
                        break; // volta para o menu
                    }

                    break;
                case 5:
                    System.out.println("\n[Sorteio simples]");
                    int quantidade = EntradaUtil.lerInteiro("Informe a quantidade participantes a serem sorteados:");
                    List<String> nomesSorteados = ParticipanteRepository.listar();

                    List<String> resultadoSorteio = SorteioSimplesService.sortear(nomesSorteados, quantidade);

                    System.out.println("Sorteados:");
                    for (int i = 0; i < resultadoSorteio.size(); i++) {
                        System.out.println((i + 1) + " - " + resultadoSorteio.get(i));
                    }
                    SorteioSimplesService service = new SorteioSimplesService();
                    List<String> naoSorteados = service.getNaoSorteados(nomesSorteados, resultadoSorteio);

                    System.out.println("\nNão sorteados:");
                    for (String nomes : naoSorteados) {
                        System.out.println(nomes);
                    }

                    System.out.println("\nRanking:");
                    for (int i = 0; i < resultadoSorteio.size(); i++) {
                        System.out.println(service.getLugar(i) + ": " + resultadoSorteio.get(i));
                    }

                    break;


                case 6:
                    System.out.println("\n[Formação de times]");
                    int numeroDeTimes = EntradaUtil.lerInteiro("Informe o número de times:");

                    List<String> participantesFT = ParticipanteRepository.listar();

                    FormacaoTimesService formacaoService = new FormacaoTimesService();
                    List<List<String>> times = formacaoService.formarTimes(participantesFT, numeroDeTimes);

                    // Exibe os times formados
                    for (int i = 0; i < times.size(); i++) {
                        System.out.println("\nTime " + (i + 1) + ":");
                        for (String nomes : times.get(i)) {
                            System.out.println(" - " + nomes);
                        }
                    }
                    break;

                case 7:
                    System.out.println("\n[Ordem de apresentação]");


                    List<String> participantesOA = ParticipanteRepository.listar();

                    OrdemApresentacaoService ordemService = new OrdemApresentacaoService();
                    List<String> ordemApresentacao = ordemService.gerarOrdem(participantesOA);

                    System.out.println("Ordem de apresentação:");
                    for (int i = 0; i < ordemApresentacao.size(); i++) {
                        System.out.println((i + 1) + " - " + ordemApresentacao.get(i));
                    }
                    break;

                case 8:
                    System.out.println("\n[Sorteio de número aleatório]");
                    int min = EntradaUtil.lerInteiro("Informe o número mínimo:");
                    int max = EntradaUtil.lerInteiro("Informe o número máximo:");
                    int quantidadeNA = EntradaUtil.lerInteiro("Informe a quantidade de números a serem sorteados:");

                    System.out.println("Escolha a ordem do resultado:");
                    System.out.println("1 - Aleatório");
                    System.out.println("2 - Crescente");
                    System.out.println("3 - Decrescente");
                    int opcaoOrdem = EntradaUtil.lerInteiro("Opção:");

                    SorteioNumeroService.OrdemResultado ordem;
                    switch (opcaoOrdem) {
                        case 2 -> ordem = SorteioNumeroService.OrdemResultado.CRESCENTE;
                        case 3 -> ordem = SorteioNumeroService.OrdemResultado.DECRESCENTE;
                        default -> ordem = SorteioNumeroService.OrdemResultado.ALEATORIO;
                    }

                    System.out.println("Permitir repetição?");
                    System.out.println("1 - Sim");
                    System.out.println("2 - Não");
                    int opcaoRepeticao = EntradaUtil.lerInteiro("Opção:");

                    SorteioNumeroService numeroService = new SorteioNumeroService();
                    List<Integer> resultado;

                    if (opcaoRepeticao == 1) {
                        resultado = numeroService.sortearComRepeticao(min, max, quantidadeNA, ordem);
                    } else {
                        resultado = numeroService.sortearSemRepeticao(min, max, quantidadeNA, ordem);
                    }

                    System.out.println("\nNúmeros sorteados:");
                    for (int i = 0; i < resultado.size(); i++) {
                        System.out.println((i + 1) + " - " + resultado.get(i));
                    }

                    break;


                case 9:
                    System.out.println("\nEncerrando sistema...");
                    break;

                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }

        } while (opcao != 9);
    }

    // Método privado para desenhar as opções na tela
    private void exibirMenu() {
        System.out.println("\n=== SISTEMA DE SORTEIOS ===");
        System.out.println("1 - Adicionar participante");
        System.out.println("2 - Listar participantes");
        System.out.println("3 - Remover participante");
        System.out.println("4 - Amigo secreto");
        System.out.println("5 - Sorteio simples");
        System.out.println("6 - Formação de times");
        System.out.println("7 - Ordem de apresentação");
        System.out.println("8 - Sortear número aleatório");
        System.out.println("9 - Sair");
    }
}