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
        return "Tiempo: " + tiempo + " // ruta: " + rutas.toString();
    }

    public ArrayList<ArrayList<Integer>> getRutas() {
        return rutas;
    }

    public int getTiempo() {
        return tiempo;
    }
}
