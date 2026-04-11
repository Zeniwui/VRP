package utils;

import java.util.*;

public class GeneradorPermutacion {
    private int dimension;
    private int semilla;

    public GeneradorPermutacion(int dimension, int semilla) {
        this.dimension = dimension;
        this.semilla = semilla;
    }

    /**
     * Generar una permutacion aleatoria con una semilla
     * @return
     */
    public List<Integer> aleatoria() {
        List<Integer> permutacion = new ArrayList<>();

        for (int i = 1; i < dimension; i++) {
            permutacion.add(i);
        }

        Random random = new Random(semilla);

        Collections.shuffle(permutacion, random);

        return permutacion;
    }

    public List<List<Integer>> listaDePermutaciones(int cantidad) {
        // Para garantizar que no haya listas repetidas
        Set<List<Integer>> permutacionesUnicas = new HashSet<>();
        List<List<Integer>> listaFinal = new ArrayList<>();
        // Generamos la permutacion base
        List<Integer> permutacionBase = new ArrayList<>();

        Random random = new Random(semilla);

        for (int i = 1; i < dimension; i++) {
            permutacionBase.add(i);
        }

        int intentosFallidos = 0;
        final int MAX_INTENTOS_FALLIDOS = 10000;
        while (listaFinal.size() < cantidad && intentosFallidos < MAX_INTENTOS_FALLIDOS) {
            List<Integer> nuevaPermutacion = new ArrayList<>(permutacionBase);
            Collections.shuffle(nuevaPermutacion, random);

            if (permutacionesUnicas.add(nuevaPermutacion)) {
                listaFinal.add(nuevaPermutacion);
                intentosFallidos = 0;
            } else {
                intentosFallidos++;
            }
        }
        if (intentosFallidos >= MAX_INTENTOS_FALLIDOS) {
            System.err.println("Advertencia: Límite de combinaciones alcanzado. Solo se pudieron generar "
                    + listaFinal.size() + " permutaciones únicas de las " + cantidad + " solicitadas.");
        }

        return listaFinal;
    }
}
