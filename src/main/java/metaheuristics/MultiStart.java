package metaheuristics;

import model.Input;
import model.Solucion;
import operators.OperadorLocal;
import utils.Evaluador;
import utils.GeneradorPermutacion;

import java.util.List;

public class MultiStart {

    private OperadorLocal operador;
    private Evaluador evaluador;
    private GeneradorPermutacion generador;
    public MultiStart(OperadorLocal operador, Evaluador evaluador, GeneradorPermutacion generador) {
        this.operador = operador;
        this.evaluador = evaluador;
        this.generador = generador;
    }

    public Solucion generarMejorSolucion(int numIteraciones) {
        Solucion mejorSolucion, solucionInicial, minimoLocal;
        List<Integer> permutacion;

        // Generamos primera solucion inicial
        permutacion = generador.aleatoria();
        mejorSolucion = evaluador.evaluarCompleto(permutacion);

        for (int i = 0; i < numIteraciones; i++) {

            // Generamos permutacion aleatoria
            permutacion = generador.aleatoria();

            // Evaluamos la permutacion
            solucionInicial = evaluador.evaluarCompleto(permutacion);

            // Generamos minimoLocal
            minimoLocal = operador.generarMinimoTodosSegmentos(solucionInicial);

            if (minimoLocal.getCosto() < mejorSolucion.getCosto()) {
                mejorSolucion = minimoLocal;
            }
        }
        return mejorSolucion;
    }
}
