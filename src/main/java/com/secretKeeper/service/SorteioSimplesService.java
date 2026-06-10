package com.secretKeeper.service;

import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.Random;

public class SorteioSimplesService {
    public static List<String> sortear(List<String> participantes, int quantidade) {

        if (participantes == null || participantes.size() < 2) {
            throw new IllegalArgumentException("Necessario no minimo 2 participantes.");
        }

        if (quantidade < 1 || quantidade > participantes.size()) {
            throw new IllegalArgumentException("Numero invalido. Informe entre 1 e " + participantes.size() + ".");
        }

        List<String> copia = new ArrayList<>(participantes);
        Collections.shuffle(copia, new Random());

        return new ArrayList<>(copia.subList(0, quantidade));
    }


    public List<String> getNaoSorteados(List<String> participantes, List<String> sorteados) {
        List<String> naoSorteados = new ArrayList<>(participantes);
        naoSorteados.removeAll(sorteados);
        return naoSorteados;
    }


    public String getLugar(int posicao) {
        if      (posicao == 0) return "1o lugar";
        else if (posicao == 1) return "2o lugar";
        else if (posicao == 2) return "3o lugar";
        else                   return (posicao + 1) + "o lugar";
    }
}