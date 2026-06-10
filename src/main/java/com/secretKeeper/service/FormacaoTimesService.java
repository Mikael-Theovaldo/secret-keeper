package com.secretKeeper.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FormacaoTimesService{

    public List<List<String>> formarTimes(List<String> participantes, int numeroDeTimes) {

        List<String> embaralhados = new ArrayList<>(participantes);

        Collections.shuffle(embaralhados, new Random());

        List<List<String>> times = new ArrayList<>();

        // cria os times vazios
        for (int i = 0; i < numeroDeTimes; i++) {
            times.add(new ArrayList<>());
        }

        // distribui os participantes
        for (int i = 0; i < embaralhados.size(); i++) {
            times.get(i % numeroDeTimes)
                    .add(embaralhados.get(i));
        }

        return times;
    }
}