package genetic;

import java.util.List;

public class Individuo {
    private List<Integer> permutacion;
    private double funcionObjetivo;
    private List<List<Integer>> rutas;

    public Individuo(List<Integer> per, double costo, List<List<Integer>> rutas) {
        permutacion = per;
        funcionObjetivo = costo;
        this.rutas = rutas;
    }
}
