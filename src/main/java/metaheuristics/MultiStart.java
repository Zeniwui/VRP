package metaheuristics;

import model.Solucion;
import operators.OperadorLocal;
import operators.Split;
import utils.Evaluador;
import utils.ExportadorCSV;
import utils.GeneradorPermutacion;

import java.util.List;

public class MultiStart {

    private OperadorLocal operador;
    private Evaluador evaluador;
    private GeneradorPermutacion generador;
    private Split split;
    private ExportadorCSV exportadorCSV;
    private String nombreInstanciaCSV;

    public MultiStart(OperadorLocal operador, Split split, Evaluador evaluador, GeneradorPermutacion generador) {
        this.operador = operador;
        this.evaluador = evaluador;
        this.generador = generador;
        this.split = split;
    }

    public void setExportadorCSV(ExportadorCSV exportador, String instancia) {
        this.exportadorCSV = exportador;
        this.nombreInstanciaCSV = instancia;
    }

    public Solucion generarMejorSolucion(int numIteraciones, boolean conSplit) {
        long inicio = System.nanoTime();
        Solucion mejorSolucion, solucionInicial, minimoLocal;
        List<Integer> permutacion;

        // Generamos primera solucion inicial
        permutacion = generador.aleatoria();
        mejorSolucion = evaluador.evaluarCompleto(permutacion);

        if (exportadorCSV != null) {
            exportadorCSV.registrar(0, 0.0, mejorSolucion.getCosto(),
                    operador.getNombre(), nombreInstanciaCSV, mejorSolucion.getRuta().toString(), conSplit);
        }

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

            // Registrar mejor solución hasta el momento
            if (exportadorCSV != null) {
                double tiempo = (System.nanoTime() - inicio) / 1_000.0;
                exportadorCSV.registrar(i + 1, tiempo, mejorSolucion.getCosto(),
                        operador.getNombre(), nombreInstanciaCSV, mejorSolucion.getRuta().toString(), conSplit);
            }
        }

        if (exportadorCSV != null) {
            exportadorCSV.exportar();
        }

        return mejorSolucion;
    }
}
