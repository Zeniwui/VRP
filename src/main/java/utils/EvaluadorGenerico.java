package utils;

import model.Input;
import model.Solucion;

import java.util.ArrayList;
import java.util.List;

public class EvaluadorGenerico implements Evaluador {
    private final int dimension;
    private final int capacidad;
    private final int[] demandas;
    private final double[][] matrizCostes;

    public EvaluadorGenerico(double[][] matrizCostes, Input input) {
        this.matrizCostes = matrizCostes;
        this.dimension = input.getDimension();
        this.capacidad = input.getCapacidad();
        this.demandas = input.getDemandas();
    }

    @Override
    public Solucion evaluarCompleto(List<Integer> permutacion) {
        double costeSolucion = 0;

        List<List<Integer>> rutaSolucion = new ArrayList<>();
        rutaSolucion.add(new ArrayList<>());

        int cajasRestantes = capacidad;
        int nodoDondeEstoy = 0;
        int i = 0;
        int corte = 0;
        while (i < permutacion.size()) {

            int nodoAIr = permutacion.get(i);

            if (demandas[nodoAIr] <= cajasRestantes) {
                costeSolucion += matrizCostes[nodoDondeEstoy][nodoAIr];
                nodoDondeEstoy = nodoAIr;
                cajasRestantes = cajasRestantes - demandas[nodoDondeEstoy];
                rutaSolucion.get(corte).add(nodoDondeEstoy);
            } else {
                rutaSolucion.add(new ArrayList<>());
                corte++;
                costeSolucion += matrizCostes[nodoDondeEstoy][0];
                cajasRestantes = capacidad;
                costeSolucion += matrizCostes[0][nodoAIr];
                nodoDondeEstoy = nodoAIr;
                cajasRestantes = cajasRestantes - demandas[nodoDondeEstoy];
                rutaSolucion.get(corte).add(nodoDondeEstoy);
            }
            i++;
        }
        costeSolucion += matrizCostes[nodoDondeEstoy][0];

        return new Solucion(rutaSolucion, costeSolucion);
    }

    public List<Solucion> evaluarListaPermutaciones(List<List<Integer>> lista) {
        List<Solucion> soluciones = new ArrayList<>();
        for (List<Integer> ruta : lista) {
            soluciones.add(evaluarCompleto(ruta));
        }
        return soluciones;
    }

    @Override
    public double evaluarRutaCompleta(List<List<Integer>> ruta) {
        double costeTotal = 0.0;
        for (List<Integer> segmento : ruta) {
            costeTotal += evaluarSegmento(segmento);
        }
        return costeTotal;
    }

    @Override
    public double evaluarSegmento(List<Integer> segmento) {
        double costeTotal = 0.0;
        costeTotal += matrizCostes[0][segmento.get(0)];
        for (int i = 0; i < segmento.size() - 1; i++) {
            costeTotal += matrizCostes[segmento.get(i)][segmento.get(i + 1)];
        }
        costeTotal += matrizCostes[segmento.get(segmento.size() - 1)][0];
        return costeTotal;
    }

    @Override
    public boolean suficienteCapacidadParaCubrirSegmento(List<Integer> segmento) {
        int capacidadRestante = capacidad;

        for (Integer nodo : segmento) {
            capacidadRestante -= demandas[nodo];

            if (capacidadRestante < 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public double costoEntre(int i, int j) {
        return matrizCostes[i][j];
    }
}
