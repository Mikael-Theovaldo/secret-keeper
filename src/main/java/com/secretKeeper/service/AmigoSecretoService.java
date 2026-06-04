package main.java.com.secretKeeper.service;

import main.java.com.secretKeeper.model.Participante;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AmigoSecretoService {
    public static List<Participante> sortear(List<String> nomes){
        List<Participante> doadores = new ArrayList<>();

        for (String nome : nomes){
            doadores.add(new Participante(nome));
        }

        List<Participante> receptores = new ArrayList<>(doadores);

        Random rng = new Random();

        boolean valido = false;

        int tentativas = 0;

        while(!valido && tentativas < 2000){
            Collections.shuffle(receptores, rng);
            valido = true;

            for(int i = 0; i < doadores.size(); i++){
                if(doadores.get(i).equals(receptores.get(i))){
                    valido = false;
                    break;
                }
            }
            tentativas ++;
        }
        if(!valido){
            throw new RuntimeException("Não foi possível gerar sorteio válido.");
        }
        for(int i = 0; i < doadores.size(); i++){
            doadores.get(i).setAmigoSecreto(receptores.get(i));
        }
        return doadores;
    }
}