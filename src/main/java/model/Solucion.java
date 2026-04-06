package model;

import java.util.ArrayList;

public class Solucion {
    private ArrayList<ArrayList<Integer>> rutas;
    private int tiempo;

    public Solucion(ArrayList<ArrayList<Integer>> r, int t) {
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
    public ArrayList<ArrayList<Integer>> getRuta() { return rutas; }
}
