package operators;

import model.Input;
import model.Solucion;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class OperadorSwap implements OperadorLocal{

    private Evaluador evaluador;
    private Input input;
    private String nombre = "Swap";

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
    @Override
    public Solucion generarMinimoLocal(Solucion solucionInicial) {
        List<List<Integer>> rutaActual = solucionInicial.getRuta();
        int tamanoRuta = solucionInicial.getRuta().size();
        double costoMejor = solucionInicial.getCosto();

        Solucion solucionMejor = new Solucion(solucionInicial);
        List<List<Integer>> segmentosACambiar = new ArrayList<>(2);
        List<Integer> segmento1;
        List<Integer> segmento2;
        List<List<Integer>> segmentosCambiados;
        double costoSeg1, costoSeg2, costoAux, costoNuevo1, costoNuevo2, costoNuevoTotal;
        double costoRutaActual;
        boolean hayMejora = true;

        // Bucle para encontrar el minimo local
        while (hayMejora) {
            hayMejora = false;
            costoRutaActual = evaluador.evaluarRutaCompleta(rutaActual);
            System.out.println("swap-CostoRutaActual: " + costoRutaActual);
            //System.out.println("--- Generando vecinos ---");
            // Itero por todos los segmentos que componen la ruta
            for (int i = 0; i < tamanoRuta - 1; i++) {
                for (int j = i + 1; j < tamanoRuta; j++) {
                    segmentosACambiar.clear();
                    segmentosACambiar.add(rutaActual.get(i));
                    segmentosACambiar.add(rutaActual.get(j));

                    //System.out.println("Segmentos a cambiar: " + segmentosACambiar);

                    // Una vez elegidos los segmentos, tendremos que iterar por todos los nodos que los componen para aplicarles el swap
                    segmento1 = new ArrayList<>(segmentosACambiar.get(0));
                    costoSeg1 = evaluador.evaluarSegmento(segmento1);
                    segmento2 = new ArrayList<>(segmentosACambiar.get(1));
                    costoSeg2 = evaluador.evaluarSegmento(segmento2);

                    costoAux = costoRutaActual - costoSeg1 - costoSeg2;

                    for (int k = 0; k < segmento1.size(); k++) {
                        for (int l = 0; l < segmento2.size(); l++) {
                            // Aplico el swap
                            segmentosCambiados = aplicarCambio(segmentosACambiar, k, l);
                            //System.out.println("Segmentos cambiados: " + segmentosCambiados);
                            // Si los nodos se pueden intercambiar, calculamos nuevos costos
                            if (segmentosCambiados != null) {
                                costoNuevo1 = evaluador.evaluarSegmento(segmentosCambiados.get(0));
                                costoNuevo2 = evaluador.evaluarSegmento(segmentosCambiados.get(1));
                                costoNuevoTotal = costoAux + costoNuevo1 + costoNuevo2;

                                // Si el nuevo costo es mejor que el que teniamos, tenemos que guardar la solucion
                                if (costoNuevoTotal < costoMejor) {
                                    // Hemos encontrado una mejora
                                    hayMejora = true;
                                    // Actualizamos el mejor costo
                                    costoMejor = costoNuevoTotal;
                                    // Antes que poner la ruta en la solucion la actualizamos por si hubo cambios anteriores
                                    solucionMejor.copiarRuta(rutaActual);
                                    // En la solucion mejor introducimos los segmentos cambiados que nos proporcionaron mejor resultado
                                    solucionMejor.setSegmento(i, segmentosCambiados.get(0));
                                    solucionMejor.setSegmento(j, segmentosCambiados.get(1));
                                    solucionMejor.setCosto(costoNuevoTotal);
                                    System.out.println(solucionMejor);
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
            //System.out.println("---- Todos los vecinos generados ----");
        }

        return solucionMejor;
    }
    public String getNombre() { return nombre; }
}
