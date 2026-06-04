package main.java.com.secretKeeper.repository;

import java.util.ArrayList;
import java.util.List;

public class ParticipanteRepository {
    private static final List<String> participantes = new ArrayList<>();

    public static void adicionar(String nome){
        participantes.add(nome);
    }

    public static void remover(int indice){
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