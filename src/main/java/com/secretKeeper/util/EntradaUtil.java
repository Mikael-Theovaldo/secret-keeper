package main.java.com.secretKeeper.util;

import java.util.Scanner;

public class EntradaUtil {

    private static final Scanner scanner = new Scanner(System.in);

    // Lê números inteiros
    public static int lerInteiro(String mensagem) {

        while (true) {
            try {
                System.out.print(mensagem);

                int valor = Integer.parseInt(scanner.nextLine().trim());

                return valor;

            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    // Lê texto
    public static String lerTexto(String mensagem) {

        System.out.print(mensagem);

        return scanner.nextLine().trim();
    }
}