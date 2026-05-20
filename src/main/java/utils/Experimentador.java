package utils;

import metaheuristics.MultiStart;
import model.Solucion;
import operators.OperadorLocal;
import operators.OperadorSplit;

import java.util.ArrayList;
import java.util.List;

public class Experimentador {

    private final int numSolucionesInicialesMultiStart = 100;

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

    public void ejecutarExperimentoSimple(OperadorLocal operador, OperadorSplit split,
                                          Evaluador evaluador, List<List<Integer>> listaPermutaciones, boolean conSplit) {
        ejecutarExperimento(operador.getNombre(), listaPermutaciones.size(), new GeneradorSolucion() {
            public Solucion generar(int i) {
                Solucion s;
                if (conSplit) {
                    s = split.generarSolucion(listaPermutaciones.get(i));
                } else {
                    s = evaluador.evaluarCompleto(listaPermutaciones.get(i));
                }
                return operador.generarMinimoTodosSegmentos(s);
            }
        });
    }

    public void ejecutarSplit(OperadorSplit operadorSplit, List<List<Integer>> listaPermutacionesIniciales) {
        ejecutarExperimento(operadorSplit.getNombre(), listaPermutacionesIniciales.size(), new GeneradorSolucion() {
            public Solucion generar(int i) {
                return operadorSplit.generarSolucion(listaPermutacionesIniciales.get(i));
            }
        });
    }

    public void ejecutarMultiStart(OperadorLocal operador, OperadorSplit split, int numIteraciones, Evaluador evaluador, GeneradorPermutacion generadorPermutacion, boolean conSplit) {
        MultiStart multiStart = new MultiStart(operador, split, evaluador, generadorPermutacion);
        System.out.println("------- MULTI START ------");
        ejecutarExperimento(operador.getNombre(), numIteraciones, new GeneradorSolucion() {
            public Solucion generar(int i) {
                return multiStart.generarMejorSolucion(numSolucionesInicialesMultiStart, conSplit);
            }
        });
    }

}
