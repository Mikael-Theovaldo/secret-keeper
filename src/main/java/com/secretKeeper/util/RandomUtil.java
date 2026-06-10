package main.java.com.secretKeeper.util;

import java.util.Random;

public class RandomUtil {

    private static final Random random = new Random();

    // Retorna o objeto Random
    public static Random getRandom() {
        return random;
    }

    // Gera número aleatório de 0 até bound - 1
    public static int nextInt(int bound) {

        if (bound <= 0) {
            throw new IllegalArgumentException(
                    "O valor bound deve ser maior que zero."
            );
        }

        return random.nextInt(bound);
    }

    // Gera número aleatório entre mínimo e máximo
    public static int nextInt(int min, int max) {

        if (min > max) {
            throw new IllegalArgumentException(
                    "O valor mínimo não pode ser maior que o máximo."
            );
        }

        return random.nextInt((max - min) + 1) + min;
    }
}