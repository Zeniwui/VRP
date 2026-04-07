package operators;

import model.Input;
import model.Solucion;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class OperadorSwap {

    private Evaluador evaluador;
    private Input input;

    public OperadorSwap(Evaluador evaluador) {
        this.evaluador = evaluador;
        input = Input.getInstancia();
    }

    /*
     * Intercambia un nodo de un segmento con otro nodo de otro segmento diferente
     * Comprueba internamente que el intercambio de nodos sea factible (que la capacidad del vehiculo pueda satisfacer las nuevas demandas)
     */
    public List<List<Integer>> aplicarCambio (List<List<Integer>> segmentos, int v1, int v2) {
        List<List<Integer>> segmentosCambiado = new ArrayList<>();
        for (List<Integer> segmento: segmentos) {
            segmentosCambiado.add(new ArrayList<>(segmento));
        }
        segmentosCambiado.get(0).set(v1, segmentos.get(1).get(v2));
        segmentosCambiado.get(1).set(v2, segmentos.get(0).get(v1));

        for (int i = 0; i < segmentosCambiado.size(); i++) {
            if (!evaluador.suficienteCapacidadParaCubrirSegmento(segmentosCambiado.get(i))) {
                return null;
            }
        }
        return segmentosCambiado;
    }

    public Solucion generarMinimoLocal(Solucion solucionInicial) {
        List<List<Integer>> rutaInicial = solucionInicial.getRuta();
        int tamanoRuta = solucionInicial.getRuta().size();
        int tiempoMejor = solucionInicial.getTiempo();

        Solucion solucionMejor;
        List<List<Integer>> segmentosACambiar = new ArrayList<>(2);
        List<Integer> segmento1, segmento2;
        boolean hayMejora = true;

        // Bucle para encontrar el minimo local
        while (hayMejora) {
            hayMejora = false;

            // Itero por todos los segmentos que componen la ruta
            for (int i = 0; i < tamanoRuta - 1; i++) {
                for (int j = i; j < tamanoRuta; j++) {
                    segmentosACambiar.set(0, rutaInicial.get(i));
                    segmentosACambiar.set(1, rutaInicial.get(j));

                    // Una vez elegidos los segmentos, tendremos que iterar por todos los nodos que los componen para aplicarles el swap
                    segmento1 = new ArrayList<>(segmentosACambiar.get(0));
                    segmento2 = new ArrayList<>(segmentosACambiar.get(1));

                    for (int k = 0; k < segmento1.size(); k++) {
                        for (int l = 0; l < segmento2.size(); l++) {

                        }
                    }
                }
            }
        }

        return null;
    }
}
