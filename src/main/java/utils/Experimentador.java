package utils;

import metaheuristics.MultiStart;
import model.Solucion;
import operators.OperadorLocal;
import operators.OperadorSplit;

import java.util.ArrayList;
import java.util.List;

public class Experimentador {

    private interface GeneradorSolucion {
        Solucion generar(int indice);
    }

    private void ejecutarExperimento(String nombreOperador, int numIteraciones, GeneradorSolucion gs) {
        List<Solucion> resultados = new ArrayList<>();

        long inicio = System.nanoTime();
        for (int i = 0; i < numIteraciones; i++) {
            resultados.add(gs.generar(i));
        }
        long fin = System.nanoTime();

        double tiempoCPU_ms = (fin - inicio) / 1_000_000.0;
        System.out.println("============================================================================================");
        System.out.printf("Tiempo ejecución %s %d repeticiones: %f ms\n", nombreOperador, numIteraciones, tiempoCPU_ms);
        new Estadisticas(resultados).calcularBasico();
    }

    public void ejecutarExperimentoSimple(OperadorLocal operador, List<Solucion> listaSolucionesIniciales) {
        ejecutarExperimento(operador.getNombre(), listaSolucionesIniciales.size(), new GeneradorSolucion() {
            public Solucion generar(int i) {
                return operador.generarMinimoTodosSegmentos(listaSolucionesIniciales.get(i));
            }
        });
    }

    public void ejecutarSplit(OperadorSplit operadorSplit, List<List<Integer>> listaPermutacionesIniciales) {
        ejecutarExperimento(operadorSplit.getNombre(), listaPermutacionesIniciales.size(), new GeneradorSolucion() {
            public Solucion generar(int i) {
                return operadorSplit.generarMinimoTodosSegmentos(listaPermutacionesIniciales.get(i));
            }
        });
    }

    public void ejecutarMultiStart(OperadorLocal operador, int numIteraciones, Evaluador evaluador, GeneradorPermutacion generadorPermutacion) {
        MultiStart multiStart = new MultiStart(operador, evaluador, generadorPermutacion);
        System.out.println("------- MULTI START ------");
        ejecutarExperimento(operador.getNombre(), numIteraciones, new GeneradorSolucion() {
            public Solucion generar(int i) {
                return multiStart.generarMejorSolucion(100);
            }
        });
    }

}
