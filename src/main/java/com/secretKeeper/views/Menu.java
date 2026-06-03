package main.java.com.secretKeeper.views;

import variosSorteios.util.EntradaUtil;

public class Menu {


    public void iniciar() {
        int opcao;

        do {
            exibirMenu();
            opcao = EntradaUtil.lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    System.out.println("\n[Adicionar participante]");
                    break;

                case 2:
                    System.out.println("\n[Listar participantes]");
                    break;

                case 3:
                    System.out.println("\n[Remover participante]");
                    break;

                case 4:
                    System.out.println("\n[Amigo secreto]");
                    break;

                case 5:
                    System.out.println("\n[Sorteio simples]");
                    break;

                case 6:
                    System.out.println("\n[Formação de times]");
                    break;

                case 7:
                    System.out.println("\n[Ordem de apresentação]");
                    break;

                case 8:
                    System.out.println("\nEncerrando sistema...");
                    break;

                default:
                    System.out.println("\nOpção inválida. Tente novamente.");
            }

        } while (opcao != 8);
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
        System.out.println("8 - Sair");
    }
}