
package main.java.com.secretKeeper.service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OrdemApresentacaoService {
    public List<String> gerarOrdem(List<String> participantes) {

        if (participantes == null || participantes.isEmpty()) {
            throw new IllegalArgumentException("Nenhum participante cadastrado.");
        }

        List<String> ordem = new ArrayList<>(participantes);

        Collections.shuffle(ordem, new Random());

        return ordem;
    }
}