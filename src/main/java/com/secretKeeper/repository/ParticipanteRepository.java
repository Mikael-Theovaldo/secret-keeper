
package main.java.com.secretKeeper.repository;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteRepository {
    private static final List<String> participantes = new ArrayList<>();

    public static void adicionar(String nome){
        participantes.add(nome);
    }

    public static void remover(int numero){
        int indice = numero - 1; // converte para índice interno (0-based)
        if (indice < 0 || indice >= participantes.size()) {
            throw new IllegalArgumentException("Número inválido. Informe um número entre 1 e " + participantes.size());
        }
        participantes.remove(indice);
    }

    public static List<String> listar(){
        return participantes;
    }

    public static int totalParticipante(){
        return participantes.size();
    }

    public boolean existe(String nome){
        return participantes.stream()
                .anyMatch(p -> p.equalsIgnoreCase(nome));
    }
}