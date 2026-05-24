package metaheuristics;

import model.Solucion;
import operators.OperadorLocal;
import operators.Split;
import utils.Evaluador;
import utils.GeneradorPermutacion;

import java.util.List;

public class MultiStart {

    private OperadorLocal operador;
    private Evaluador evaluador;
    private GeneradorPermutacion generador;
    private Split split;
    public MultiStart(OperadorLocal operador, Split split, Evaluador evaluador, GeneradorPermutacion generador) {
        this.operador = operador;
        this.evaluador = evaluador;
        this.generador = generador;
        this.split = split;
    }

    public Solucion generarMejorSolucion(int numIteraciones, boolean conSplit) {
        Solucion mejorSolucion, solucionInicial, minimoLocal;
        List<Integer> permutacion;

        // Generamos primera solucion inicial
        permutacion = generador.aleatoria();
        mejorSolucion = evaluador.evaluarCompleto(permutacion);

        for (int i = 0; i < numIteraciones; i++) {

            // Generamos permutacion aleatoria
            permutacion = generador.aleatoria();

            // Evaluamos la permutacion segun el boolean conSplit
            if (conSplit) {
                solucionInicial = split.generarCortes(permutacion);
            } else {
                solucionInicial = evaluador.evaluarCompleto(permutacion);
            }

            // Generamos minimoLocal
            minimoLocal = operador.generarMinimoTodosSegmentos(solucionInicial);

            if (minimoLocal.getCosto() < mejorSolucion.getCosto()) {
                mejorSolucion = minimoLocal;
            }
        }
        return mejorSolucion;
    }
}
