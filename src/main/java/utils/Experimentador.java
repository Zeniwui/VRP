package utils;

import metaheuristics.MultiStart;
import model.Solucion;
import operators.OperadorLocal;
import operators.Split;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        System.out.printf("Tiempo ejecucion %s %d repeticiones: %f ms\n", nombreOperador, numIteraciones, tiempoCPU_ms);
        new Estadisticas(resultados).calcularBasico();
    }

    public void ejecutarExperimentoSimple(OperadorLocal operador, Split split,
                                          Evaluador evaluador, List<List<Integer>> listaPermutaciones, boolean conSplit) {
        ejecutarExperimento(operador.getNombre(), listaPermutaciones.size(), new GeneradorSolucion() {
            public Solucion generar(int i) {
                Solucion s;
                if (conSplit) {
                    s = split.generarCortes(listaPermutaciones.get(i));
                } else {
                    s = evaluador.evaluarCompleto(listaPermutaciones.get(i));
                }
                return operador.generarMinimoTodosSegmentos(s);
            }
        });
    }

    public void ejecutarExperimentoSimpleConCSV(OperadorLocal operador, Split split,
                                                Evaluador evaluador, List<List<Integer>> listaPermutaciones, boolean conSplit, String nombreInstancia) {
        ExportadorEstadisticasCSV exportador = new ExportadorEstadisticasCSV();

        List<Solucion> resultados = new ArrayList<>();

        long inicio = System.nanoTime();
        for (int i = 0; i < listaPermutaciones.size(); i++) {
            List<Integer> permutacion = listaPermutaciones.get(i);
            Solucion s;
            if (conSplit) {
                s = split.generarCortes(permutacion);
            } else {
                s = evaluador.evaluarCompleto(permutacion);
            }
            Solucion solucionFinal = operador.generarMinimoTodosSegmentos(s);
            resultados.add(solucionFinal);

            exportador.registrarDetalle(permutacion.toString(), solucionFinal.getRuta().toString(), solucionFinal.getCosto());
        }
        long fin = System.nanoTime();
        double tiempoTotalMs = (fin - inicio) / 1_000_000.0;

        exportador.exportar(operador.getNombre(), nombreInstancia, conSplit, true, false, tiempoTotalMs);

        System.out.printf("Tiempo ejecucion %s %d repeticiones: %f ms\n", operador.getNombre(), listaPermutaciones.size(), tiempoTotalMs);
        new Estadisticas(resultados).calcularBasico();
    }

    public void ejecutarExperimentoConCSV(OperadorLocal operador, Split split,
                                          Evaluador evaluador, List<Integer> permutacion, String nombreInstancia) {
        String operadorNombre = operador.getNombre().replace(" ", "_");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String nombreArchivo = "resultados_" + nombreInstancia.replace(".vrp", "").replace(".txt", "") + "_" + operadorNombre + "_" + timestamp + ".csv";

        ExportadorCSV exportador = new ExportadorCSV();
        exportador.setNombreArchivo(nombreArchivo);

        operador.setExportador(exportador, nombreInstancia, "", false);
        Solucion inicialSinSplit = evaluador.evaluarCompleto(permutacion);
        operador.generarMinimoTodosSegmentos(inicialSinSplit);

        operador.setExportador(exportador, nombreInstancia, "", true);
        Solucion inicialConSplit = split.generarCortes(permutacion);
        operador.generarMinimoTodosSegmentos(inicialConSplit);

        exportador.exportar();
    }

    public void ejecutarSplit(Split split, List<List<Integer>> listaPermutacionesIniciales) {
        ejecutarExperimento(split.getNombre(), listaPermutacionesIniciales.size(), new GeneradorSolucion() {
            public Solucion generar(int i) {
                return split.generarCortes(listaPermutacionesIniciales.get(i));
            }
        });
    }

    public void ejecutarMultiStart(OperadorLocal operador, Split split, int numIteraciones, Evaluador evaluador, GeneradorPermutacion generadorPermutacion, boolean conSplit) {
        MultiStart multiStart = new MultiStart(operador, split, evaluador, generadorPermutacion);
        System.out.println("------- MULTI START ------");
        ejecutarExperimento(operador.getNombre(), numIteraciones, new GeneradorSolucion() {
            public Solucion generar(int i) {
                return multiStart.generarMejorSolucion(numSolucionesInicialesMultiStart, conSplit);
            }
        });
    }

    public void ejecutarMultiStartConCSV(OperadorLocal operador, Split split, int numIteraciones, Evaluador evaluador, GeneradorPermutacion generadorPermutacion, boolean conSplit, String nombreInstancia) {
        MultiStart multiStart = new MultiStart(operador, split, evaluador, generadorPermutacion);
        ExportadorEstadisticasCSV exportador = new ExportadorEstadisticasCSV();

        List<Solucion> resultados = new ArrayList<>();

        System.out.println("------- MULTI START (con CSV) ------");
        long inicio = System.nanoTime();
        for (int i = 0; i < numIteraciones; i++) {
            Solucion solucionFinal = multiStart.generarMejorSolucion(numSolucionesInicialesMultiStart, conSplit);
            resultados.add(solucionFinal);

            exportador.registrarSoloRuta(solucionFinal.getRuta().toString(), solucionFinal.getCosto());
        }
        long fin = System.nanoTime();
        double tiempoTotalMs = (fin - inicio) / 1_000_000.0;

        exportador.exportar(operador.getNombre(), nombreInstancia, conSplit, false, true, tiempoTotalMs);

        System.out.printf("Tiempo ejecucion %s %d repeticiones: %f ms\n", operador.getNombre(), numIteraciones, tiempoTotalMs);
        new Estadisticas(resultados).calcularBasico();
    }

}
