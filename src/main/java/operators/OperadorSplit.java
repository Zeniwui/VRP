package operators;

import model.Input;
import model.Solucion;
import utils.Evaluador;

import java.util.ArrayList;

public class OperadorSplit implements OperadorLocal{
    private Evaluador evaluador;
    private String nombre = "Operador Split";
    private Input input = Input.getInstancia();
    public OperadorSplit(Evaluador evaluador) {
        this.evaluador = evaluador;
    }


    @Override
    public Solucion generarMinimoLocal(Solucion solucionInicial) {
        return null;
    }

    @Override
    public Solucion generarMinimoTodosSegmentos(Solucion solucionInicial) {
        int j, load;
        double cost;
        double c =

        double[] V = new double[input.getDimension()];
        int[] P = new int[input.getDimension()];

        V[0] = 0;
        P[0] = 0;

        for (int i = 1; i < V.length; i++) {
            V[i] = Integer.MAX_VALUE;
        }

        for (int i = 1; i < V.length; i++) {
             j = i;
             load = 0;
             do {
                 load += input.getDemandas()[j];

                 if (i == j) {
                     cost = input.getDistancias()[0][i] + input.getDistancias()[i][0];
                 } else {
                     cost = cost -
                 }
             }
        }
    }

    @Override
    public String getNombre() {
        return nombre;
    }
}
