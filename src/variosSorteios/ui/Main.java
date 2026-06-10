package variosSorteios.ui;

import variosSorteios.util.EntradaUtil;

public class  Main {

    public static void main(String[] args) {

        int opcao;

        do {

            exibirMenu();

            opcao = EntradaUtil.lerInteiro("Escolha uma opção: ");

            switch (opcao) {

                case 1:
                    System.out.println("Adicionar participante");
                    break;

                case 2:
                    System.out.println("Listar participantes");
                    break;

                case 3:
                    System.out.println("Remover participante");
                    break;

                case 4:
                    System.out.println("Amigo secreto");
                    break;

                case 5:
                    System.out.println("Sorteio simples");
                    break;

                case 6:
                    System.out.println("Formação de times");
                    break;

                case 7:
                    System.out.println("Ordem de apresentação");
                    break;

                case 0:
                    System.out.println("Encerrando sistema...");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    public static void exibirMenu() {

        System.out.println();
        System.out.println("=== SISTEMA DE SORTEIOS ===");
        System.out.println("1 - Adicionar participante");
        System.out.println("2 - Listar participantes");
        System.out.println("3 - Remover participante");
        System.out.println("4 - Amigo secreto");
        System.out.println("5 - Sorteio simples");
        System.out.println("6 - Formação de times");
        System.out.println("7 - Ordem de apresentação");
        System.out.println("0 - Sair");
    }
}