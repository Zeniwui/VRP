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

        //System.out.println(segmentosCambiado);

        for (int i = 0; i < segmentosCambiado.size(); i++) {
            if (!evaluador.suficienteCapacidadParaCubrirSegmento(segmentosCambiado.get(i))) {
                return null;
            }
        }
        return segmentosCambiado;
    }

    public Solucion generarMinimoLocal(Solucion solucionInicial) {
        List<List<Integer>> rutaActual = solucionInicial.getRuta();
        int tamanoRuta = solucionInicial.getRuta().size();
        int tiempoMejor = solucionInicial.getTiempo();

        Solucion solucionMejor = new Solucion(solucionInicial);
        List<List<Integer>> segmentosACambiar = new ArrayList<>(2);
        List<Integer> segmento1;
        List<Integer> segmento2;
        List<List<Integer>> segmentosCambiados;
        int tiempoSeg1, tiempoSeg2, tiempoAux, tiempoNuevo1, tiempoNuevo2, tiempoNuevoTotal;
        int tiempoRutaActual;
        boolean hayMejora = true;

        // Bucle para encontrar el minimo local
        while (hayMejora) {
            hayMejora = false;
            tiempoRutaActual = evaluador.evaluarTiempoCompleto(rutaActual);
            System.out.println("--- Generando vecinos ---");
            // Itero por todos los segmentos que componen la ruta
            for (int i = 0; i < tamanoRuta - 1; i++) {
                for (int j = i + 1; j < tamanoRuta; j++) {
                    segmentosACambiar.clear();
                    segmentosACambiar.add(rutaActual.get(i));
                    segmentosACambiar.add(rutaActual.get(j));

                    System.out.println("Segmentos a cambiar: " + segmentosACambiar);

                    // Una vez elegidos los segmentos, tendremos que iterar por todos los nodos que los componen para aplicarles el swap
                    segmento1 = new ArrayList<>(segmentosACambiar.get(0));
                    tiempoSeg1 = evaluador.evaluarTiempoSegmento(segmento1);
                    segmento2 = new ArrayList<>(segmentosACambiar.get(1));
                    tiempoSeg2 = evaluador.evaluarTiempoSegmento(segmento2);

                    tiempoAux = tiempoRutaActual - tiempoSeg1 - tiempoSeg2;

                    for (int k = 0; k < segmento1.size(); k++) {
                        for (int l = 0; l < segmento2.size(); l++) {
                            // Aplico el swap
                            segmentosCambiados = aplicarCambio(segmentosACambiar, k, l);
                            System.out.println("Segmentos cambiados: " + segmentosCambiados);
                            // Si los nodos se pueden intercambiar, calculamos nuevos tiempos
                            if (segmentosCambiados != null) {
                                tiempoNuevo1 = evaluador.evaluarTiempoSegmento(segmentosCambiados.get(0));
                                tiempoNuevo2 = evaluador.evaluarTiempoSegmento(segmentosCambiados.get(1));
                                tiempoNuevoTotal = tiempoAux + tiempoNuevo1 + tiempoNuevo2;

                                // Si el nuevo tiempo es mejor que el que teniamos, tenemos que guardar la solucion
                                if (tiempoNuevoTotal < tiempoMejor) {
                                    // Hemos encontrado una mejora
                                    hayMejora = true;
                                    // Actualizamos el mejor tiempo
                                    tiempoMejor = tiempoNuevoTotal;
                                    // Antes que poner la ruta en la solucion la actualizamos por si hubo cambios anteriores
                                    solucionMejor.copiarRuta(rutaActual);
                                    // En la solucion mejor introducimos los segmentos cambiados que nos proporcionaron mejor resultado
                                    solucionMejor.setSegmento(i, segmentosCambiados.get(0));
                                    solucionMejor.setSegmento(j, segmentosCambiados.get(1));
                                    solucionMejor.setTiempo(tiempoNuevoTotal);
                                }
                            }
                        }
                    }
                }
            }
            // Una vez que he generado todos los vecinos, tengo que trabajar con el mejor vecino
            // TODO: si vuelvo a iterar por todos los segmentos, estare repitiendo pruebas que ya he hecho. porque con el swap solo intercambio dos segmentos
            // TODO: en un futuro, optimizar para solo iterar haciendo swap con los segmentos que cambiaron
            rutaActual = solucionMejor.getRuta();
            System.out.println("---- Todos los vecinos generados ----");
        }

        return solucionMejor;
    }
}
