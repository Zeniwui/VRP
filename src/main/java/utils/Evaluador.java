package utils;

import model.Solucion;

import java.util.List;

public interface Evaluador {
    double evaluarRutaCompleta(List<List<Integer>> ruta);
    double evaluarSegmento(List<Integer> segmento);
    boolean suficienteCapacidadParaCubrirSegmento(List<Integer> segmento);
    Solucion evaluarCompleto(List<Integer> permutacion);
}
