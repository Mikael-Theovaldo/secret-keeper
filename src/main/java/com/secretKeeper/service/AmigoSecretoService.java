package com.secretKeeper.service;

import com.secretKeeper.model.Participante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AmigoSecretoService {
    public static List<Participante> sortear(List<String> nomes) {
        if (nomes == null || nomes.size() < 3) {
            throw new IllegalArgumentException("É necessário no mínimo 3 participantes.");
        }

        List<Participante> doadores = new ArrayList<>();
        for (String nome : nomes) {
            doadores.add(new Participante(nome));
        }

        List<Participante> receptores = new ArrayList<>(doadores);
        Random rng = new Random();

        boolean valido = false;
        int tentativas = 0;

        while (!valido && tentativas < 2000) {
            Collections.shuffle(receptores, rng);
            valido = true;

            for (int i = 0; i < doadores.size(); i++) {
                Participante a = doadores.get(i);
                Participante b = receptores.get(i);

                // regra 1: ninguém tira a si mesmo
                if (a.equals(b)) {
                    valido = false;
                    break;
                }

                // regra 2: evitar ciclo de 2 pessoas (A->B e B->A)
                int indexB = doadores.indexOf(b);
                if (indexB != -1 && receptores.get(indexB).equals(a)) {
                    valido = false;
                    break;
                }
            }
            tentativas++;
        }

        if (!valido) {
            throw new RuntimeException("Não foi possível gerar sorteio válido.");
        }

        for (int i = 0; i < doadores.size(); i++) {
            doadores.get(i).setAmigoSecreto(receptores.get(i));
        }

        return doadores;
    }

}