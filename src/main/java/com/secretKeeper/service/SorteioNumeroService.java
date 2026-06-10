package main.java.com.secretKeeper.service;

import main.java.com.secretKeeper.util.RandomUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SorteioNumeroService {

    // ──────────────────────────────────────────────
    //  Enum que representa as opções de ordenação
    // ──────────────────────────────────────────────
    public enum OrdemResultado {
        ALEATORIO,
        CRESCENTE,
        DECRESCENTE
    }

    // ──────────────────────────────────────────────
    //  Sorteia números COM repetição permitida
    // ──────────────────────────────────────────────
    public List<Integer> sortearComRepeticao(int min, int max, int quantidade, OrdemResultado ordem) {
        validarIntervalo(min, max);
        validarQuantidade(quantidade);

        List<Integer> resultado = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            resultado.add(RandomUtil.nextInt(min, max));
        }

        return aplicarOrdem(resultado, ordem);
    }

    // ──────────────────────────────────────────────
    //  Sorteia números SEM repetição
    // ──────────────────────────────────────────────
    public List<Integer> sortearSemRepeticao(int min, int max, int quantidade, OrdemResultado ordem) {
        validarIntervalo(min, max);
        validarQuantidade(quantidade);

        int totalDisponiveis = max - min + 1;
        if (quantidade > totalDisponiveis) {
            throw new IllegalArgumentException(
                    "Quantidade solicitada (" + quantidade + ") maior que os numeros disponiveis sem repeticao (" + totalDisponiveis + ")."
            );
        }

        // Monta o pool e embaralha usando RandomUtil
        List<Integer> pool = new ArrayList<>();
        for (int i = min; i <= max; i++) pool.add(i);

        Collections.shuffle(pool, RandomUtil.getRandom());

        List<Integer> resultado = new ArrayList<>(pool.subList(0, quantidade));

        return aplicarOrdem(resultado, ordem);
    }

    // ──────────────────────────────────────────────
    //  Aplica a ordenação escolhida ao resultado
    // ──────────────────────────────────────────────
    public List<Integer> aplicarOrdem(List<Integer> numeros, OrdemResultado ordem) {
        if (numeros == null || numeros.isEmpty()) {
            throw new IllegalArgumentException("A lista de numeros nao pode ser nula ou vazia.");
        }

        List<Integer> resultado = new ArrayList<>(numeros);

        switch (ordem) {
            case CRESCENTE  -> Collections.sort(resultado);
            case DECRESCENTE -> resultado.sort(Collections.reverseOrder());
            case ALEATORIO  -> Collections.shuffle(resultado, RandomUtil.getRandom());
        }

        return resultado;
    }

    // ──────────────────────────────────────────────
    //  Validações internas
    // ──────────────────────────────────────────────
    private void validarIntervalo(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException(
                    "O numero minimo (" + min + ") deve ser menor que o maximo (" + max + ")."
            );
        }
    }

    private void validarQuantidade(int quantidade) {
        if (quantidade < 1) {
            throw new IllegalArgumentException(
                    "A quantidade deve ser no minimo 1."
            );
        }
    }
}