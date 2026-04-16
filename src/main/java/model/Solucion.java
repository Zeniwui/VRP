package model;

import java.util.ArrayList;
import java.util.List;

public class Solucion {
    private List<List<Integer>> rutas;
    private int tiempo;
    private double distancia;

    public Solucion(List<List<Integer>> r, int t) {
        rutas = r;
        tiempo = t;
    }

    public Solucion(List<List<Integer>> r, double d) {
        rutas = r;
        distancia = d;
    }

    /**
     * Genera una copia identica de otra Solucion que se le pasa por parametro
     * @param otra
     */
    public Solucion(Solucion otra) {
        this.tiempo = otra.tiempo;

        this.rutas = new ArrayList<>();
        for (List<Integer> subruta : otra.rutas) {
            this.rutas.add(new ArrayList<>(subruta));
        }
    }

    @Override
    public String toString() {
        return "Tiempo: " + tiempo + " // ruta: " + rutas.toString() + "\n";
    }

    public int getTiempo() {
        return tiempo;
    }
    public double getDistancia() { return distancia; }
    public List<List<Integer>> getRuta() { return rutas; }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }
    /*
     * Cambia el segmento en el indice i por el que se le pasa como parametro
     */
    public void setSegmento(int i, List<Integer> segmento) {
        rutas.set(i, segmento);
    }
    public void copiarRuta(List<List<Integer>> ruta) {
        this.rutas = new ArrayList<>(ruta);
    }
}
