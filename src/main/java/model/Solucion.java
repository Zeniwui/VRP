package model;

import java.util.ArrayList;
import java.util.List;

public class Solucion {
    private List<List<Integer>> rutas;
    private int tiempo;

    public Solucion(List<List<Integer>> r, int t) {
        rutas = r;
        tiempo = t;
    }

    @Override
    public String toString() {
        return "Tiempo: " + tiempo + " // ruta: " + rutas.toString() + "\n";
    }

    public int getTiempo() {
        return tiempo;
    }
    public List<List<Integer>> getRuta() { return rutas; }
}
