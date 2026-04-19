package utils;

import model.Input;
import model.Solucion;

import java.util.ArrayList;
import java.util.List;

public class EvaluadorTiempos implements  Evaluador{
    private int dimension;
    private int capacidad;
    private int[] demandas;
    private double[][] tiempos;

    public EvaluadorTiempos(Input input) {
        this.dimension = input.getDimension();
        this.capacidad = input.getCapacidad();
        this.demandas = input.getDemandas();
        this.tiempos = input.getTiempos();
    }

    /**
     * Evalua una permutacion completa haciendo los cortes necesarios indicando que se debe volver al nodo
     * origen. Ademas calcula tambien el tiempo
     * @param permutacion Lista de enteros correspondientes al orden que debe seguir la ruta
     * @return Solucion compuesta por la permutacion cortada y el tiempo total que tarda en hacer esa ruta
     */
    @Override
    public Solucion evaluarCompleto(List<Integer> permutacion) {
        double tiempoSolucion = 0;

        List<List<Integer>> rutaSolucion = new ArrayList<>();
        rutaSolucion.add(new ArrayList<>());

        int cajasRestantes = capacidad;
        int nodoDondeEstoy = 0;
        int i = 0;
        int corte = 0;
        while (i < permutacion.size()) {

            int nodoAIr = permutacion.get(i);

            if (demandas[nodoAIr] <= cajasRestantes) {  // Si tengo suficentes cajas para visitar el nodoAIr
                // Voy al nodo
                tiempoSolucion += tiempos[nodoDondeEstoy][nodoAIr];
                // Ya estoy en el nodo
                nodoDondeEstoy = nodoAIr;
                // Dejo las cajas en el nodo
                cajasRestantes = cajasRestantes - demandas[nodoDondeEstoy];
                // Pongo nodo en la ruta
                rutaSolucion.get(corte).add(nodoDondeEstoy);
            } else {    // Si no tengo suficientes cajas, hay que ir de vuelta al nodo 0
                // Creo corte en la solucion
                rutaSolucion.add(new ArrayList<>());
                corte++;
                // Voy de vuelta al nodo 0
                tiempoSolucion += tiempos[nodoDondeEstoy][0];
                // Recargo cajas
                cajasRestantes = capacidad;
                // Voy al nodo que me tocaba segun la permutacion
                tiempoSolucion += tiempos[0][nodoAIr];
                // Ya estoy en el nuevo nodo
                nodoDondeEstoy = nodoAIr;
                // Dejo las cajas en el nodo
                cajasRestantes = cajasRestantes - demandas[nodoDondeEstoy];
                // Pongo nodo en la ruta
                rutaSolucion.get(corte).add(nodoDondeEstoy);
            }
            i++;
        }
        // Acabo de recorrer toda la ruta y me falta volver al origen
        tiempoSolucion += tiempos[nodoDondeEstoy][0];

        return new Solucion(rutaSolucion, tiempoSolucion);
    }
    public List<Solucion> evaluarListaPermutaciones(List<List<Integer>> lista) {
        List<Solucion> soluciones = new ArrayList<>();
        for(List<Integer> ruta: lista) {
            soluciones.add(evaluarCompleto(ruta));
        }
        return soluciones;
    }
    @Override
    public double evaluarRutaCompleta(List<List<Integer>> ruta) {
        double tiempoTotal = 0.0;
        for (List<Integer> segmento: ruta) {
            tiempoTotal += evaluarSegmento(segmento);
        }
        return tiempoTotal;
    }
    /**
     * Obtiene el tiempo que se tarda en recorrer el segmento que se le da por parametro
     * @param segmento
     * @return
     */
    @Override
    public double evaluarSegmento(List<Integer> segmento) {
        double tiempoTotal = 0.0;
        tiempoTotal += tiempos[0][segmento.get(0)];
        for (int i = 0; i < segmento.size() - 1; i++) {
            tiempoTotal += tiempos[segmento.get(i)][segmento.get(i+1)];
        }
        tiempoTotal += tiempos[segmento.get(segmento.size()-1)][0];
        return tiempoTotal;
    }

    /**
     * Indica si el vehiculo tiene suficiente capacidad para cubrir las demandas del segmento dato por parametro
     * @param segmento
     * @return
     */
    @Override
    public boolean suficienteCapacidadParaCubrirSegmento (List<Integer> segmento) {
        int capacidadRestante = capacidad;

        for (Integer nodo: segmento) {
            capacidadRestante -= demandas[nodo];

            if (capacidadRestante < 0) {
                return false;
            }
        }

        return true;
    }
}
