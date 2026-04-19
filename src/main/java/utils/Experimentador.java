package utils;

import model.Solucion;
import operators.OperadorLocal;

import java.util.ArrayList;
import java.util.List;

public class Experimentador {

    public void ejecutarExperimento(OperadorLocal operador, List<Solucion> listaSolucionesIniciales) {
        List<Solucion> resultados = new ArrayList<>();
        long tiempoInicio, tiempoFin;
        double tiempoCPU_ms;

        int numIteraciones = listaSolucionesIniciales.size();

        tiempoInicio = System.nanoTime();
        for (int i = 0; i < numIteraciones; i++) {
            resultados.add(operador.generarMinimoLocal(listaSolucionesIniciales.get(i)));
        }
        tiempoFin = System.nanoTime();
        tiempoCPU_ms = (tiempoFin - tiempoInicio) / 1_000_000.0;

        System.out.println("============================================================================================");
        System.out.printf("Tiempo ejecución %s %d repeticiones: %f ms\n", operador.getNombre(), numIteraciones, tiempoCPU_ms);
        Estadisticas estadisticas = new Estadisticas(resultados);
        estadisticas.calcularBasico();
    }
}
