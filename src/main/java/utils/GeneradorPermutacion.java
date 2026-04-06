package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneradorPermutacion {
    private int dimension;
    private int semilla;

    public GeneradorPermutacion(int dimension, int semilla) {
        this.dimension = dimension;
        this.semilla = semilla;
    }
    public List<Integer> aleatoria() {
        List<Integer> permutacion = new ArrayList<>();

        for (int i = 1; i < dimension; i++) {
            permutacion.add(i);
        }

        Random random = new Random(semilla);

        Collections.shuffle(permutacion, random);

        return permutacion;
    }
}
