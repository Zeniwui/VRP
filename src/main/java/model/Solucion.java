package model;

import java.util.ArrayList;
import java.util.List;

public class Solucion {
    private List<List<Integer>> rutas;
    private double costo;

    public Solucion(List<List<Integer>> r, double c) {
        rutas = r;
        costo = c;
    }

    /**
     * Genera una copia identica de otra Solucion que se le pasa por parametro
     * @param otra
     */
    public Solucion(Solucion otra) {
        this.costo = otra.costo;

        this.rutas = new ArrayList<>();
        for (List<Integer> subruta : otra.rutas) {
            this.rutas.add(new ArrayList<>(subruta));
        }
    }

    @Override
    public String toString() {
        return "Costo: " + String.format("%.2f", costo)
                + " // ruta: " + rutas.toString() + "\n";
    }

    public double getCosto() {
        return costo;
    }
    public List<List<Integer>> getRuta() { return rutas; }

    public void setCosto(double costo) {
        this.costo = costo;
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
