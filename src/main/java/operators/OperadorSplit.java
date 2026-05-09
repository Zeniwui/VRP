package operators;

import model.Input;
import model.Solucion;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class OperadorSplit{
    private Evaluador evaluador;
    private String nombre = "Operador Split";
    private Input input = Input.getInstancia();
    public OperadorSplit(Evaluador evaluador) {
        this.evaluador = evaluador;
    }


    public Solucion generarSolucion(List<Integer> T) {

        List<List<Integer>> S = new ArrayList<>();
        int n = T.size();
        int j, load = 0;
        double cost = 0;
        double[][] c = evaluador.getMatrizCostes();
        int Q = input.getCapacidad();
        int[] q = input.getDemandas();

        double[] V = new double[n + 1];
        int[] P = new int[n + 1];

        V[0] = 0;
        P[0] = 0;

        for (int i = 1; i < V.length; i++) {
            V[i] = Double.MAX_VALUE;
        }

        for (int i = 1; i < V.length; i++) {
             j = i;
             load = 0;
             do {
                 load += q[T.get(j - 1)];

                 if (i == j) {
                     cost = c[0][T.get(i - 1)] + c[T.get(i - 1)][0];
                 } else {
                     cost = cost - c[T.get(j - 2)][0] + c[T.get(j - 2)][T.get(j - 1)] + c[T.get(j - 1)][0];
                 }

                 if (load <= Q && (V[i-1] + cost < V[j])) {
                     V[j] = V[i-1] + cost;
                     P[j] = i - 1;
                 }
                 j++;
             } while (j <= n && load <= Q);
        }

        j = n;
        do {
            ArrayList<Integer> trip = new ArrayList<>();
            for (int k = P[j]; k < j; k++) {
                trip.add(T.get(k));
            }
            S.add(0, trip);
            j = P[j];
        } while (j > 0);

        return new Solucion(S, evaluador.evaluarRutaCompleta(S));
    }

    public String getNombre() {
        return nombre;
    }
}
