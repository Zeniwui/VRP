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

    public List<Integer> getPermutacion() {
        return permutacion;
    }

    public double getFuncionObjetivo() {
        return funcionObjetivo;
    }

    public List<List<Integer>> getRutas() {
        return rutas;
    }
}
