package operators;

import model.Input;
import model.Solucion;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class OperadorOrOpt implements OperadorLocal{

    private Evaluador evaluador;
    private String nombre = "OR-opt";

    public OperadorOrOpt(Evaluador evaluador) {
        this.evaluador = evaluador;
    }
    /*
     * Extrae una cadena de 2 clientes consecutivos de su posicion actual y la reinserta en la posicion marcada como v2
     */
    public List<Integer> aplicarCambio(List<Integer> segmento, int v1, int v2) {
        List<Integer> segmentoCambiado = new ArrayList<>(segmento);
        int indiceAInsertar;

        int cliente1 = segmentoCambiado.remove(v1);
        int cliente2 = segmentoCambiado.remove(v1);

        if  (v2 < v1) {
            indiceAInsertar = segmentoCambiado.indexOf(segmento.get(v2));
        } else {
            indiceAInsertar = segmentoCambiado.indexOf(segmento.get(v2)) + 1;
        }

        segmentoCambiado.add(indiceAInsertar, cliente2);
        segmentoCambiado.add(indiceAInsertar, cliente1);

        return segmentoCambiado;
    }
    @Override
    public Solucion generarMinimoLocal (Solucion solucionInicial) {
        Solucion solucionMejor = new Solucion(new ArrayList<>(solucionInicial.getRuta()), solucionInicial.getCosto());
        double costoMejor = solucionInicial.getCosto();

        List<Integer> segmentoActual, segmentoMejor = null, segmentoCambiado = null;
        int numeroSegmentos = solucionInicial.getRuta().size();
        List<Double> costesMejoresSegmentos = new ArrayList<>(numeroSegmentos); // Array para guardar los mejores costes de cada segmento
        List<List<Integer>> listaMejoresSegmentos = new ArrayList<>(new ArrayList<>()); // Array para guardar los resultados de los mejores segmentos de cada corte
        double costoSegmento, costoSegmentoCambiado, costoSegmentoMejor, costoAux, diferencia, diferenciaMayor = 0;

        boolean hayMejora;

        int numAceptaciones = 0;

        // Tenemos que iterar por todos los segmentos que componen la ruta inicial
        for (int corte = 0; corte < numeroSegmentos; corte++) {
            // Segmento con el que trabajamos
            segmentoActual = solucionInicial.getRuta().get(corte);
            //System.out.println("Trabajando con segmento: " + segmentoActual);
            // costo de recorrer ese segmento
            costoSegmento = evaluador.evaluarSegmento(segmentoActual);
            costesMejoresSegmentos.add(corte, costoSegmento);
            listaMejoresSegmentos.add(segmentoActual);
            costoSegmentoMejor = costoSegmento;
            costoAux = solucionInicial.getCosto() - costoSegmento;

            hayMejora = true;
            while (hayMejora) {
                hayMejora = false;

                //System.out.println("--- Generando vecinos ---");
                // En cada segmento, aplicamos el cambio
                for (int i = 0; i < segmentoActual.size() - 1; i++) {
                    for (int j = 0; j < segmentoActual.size(); j++) {

                        if (( j == i) || (j == i + 1)) {
                            continue;
                        }
                        // Aplicamos el cambio
                        segmentoCambiado = aplicarCambio(segmentoActual, i, j);
                        //System.out.println("Segmento cambiado: " + segmentoCambiado);
                        // Evaluamos el costo del segmento cambiado
                        costoSegmentoCambiado = evaluador.evaluarSegmento(segmentoCambiado);
                        //System.out.println("costo segmento cambiado: "  + costoSegmentoCambiado);

                        if (costoSegmentoCambiado < costoSegmentoMejor) {
                            hayMejora = true;
                            segmentoMejor = segmentoCambiado;
                            costoSegmentoMejor = costoSegmentoCambiado;
                            costoMejor = costoAux + costoSegmentoCambiado;

                            //  Guardo en la lista de costes el nuevo coste que ha mejorado el coste inicial
                            costesMejoresSegmentos.set(corte, costoSegmentoMejor);

                            // Guardo en la lista de segmentos ese segmento que ha dado mejor resultado
                            listaMejoresSegmentos.set(corte, segmentoMejor);
                        }
                    }
                }
                //System.out.println("--- Todos los vecinos generados ---");
                // Una vez generados todos los vecinos, nos quedamos con el mejor
                if (hayMejora) {
                    segmentoActual = segmentoMejor;
                    solucionMejor.setSegmento(corte, listaMejoresSegmentos.get(corte));
                    // Si hay mejora, entonces aceptamos una solucion nueva
                    numAceptaciones++;
                }
            }
        }
        System.out.println(listaMejoresSegmentos);
        solucionMejor.setCosto(evaluador.evaluarRutaCompleta(listaMejoresSegmentos));

        //System.out.println(numAceptaciones);
        return solucionMejor;
    }
    public Solucion generarMinimoTodosSegmentos (Solucion solucionInicial) {
        Solucion solucionMejor = new Solucion(new ArrayList<>(solucionInicial.getRuta()), solucionInicial.getCosto());
        double costoMejor = solucionInicial.getCosto();

        List<Integer> segmentoActual, segmentoMejor = null, segmentoCambiado = null;
        int numeroSegmentos = solucionInicial.getRuta().size();
        List<Double> costesMejoresSegmentos = new ArrayList<>(numeroSegmentos); // Array para guardar los mejores costes de cada segmento
        List<List<Integer>> listaMejoresSegmentos = new ArrayList<>(new ArrayList<>()); // Array para guardar los resultados de los mejores segmentos de cada corte
        double costoSegmento, costoSegmentoCambiado, costoSegmentoMejor, costoAux, diferencia, diferenciaMayor = 0;
        int indiceMejorSegmento = -1;
        boolean hayMejora;

        int numAceptaciones = 0;

        // Tenemos que iterar por todos los segmentos que componen la ruta inicial
        for (int corte = 0; corte < numeroSegmentos; corte++) {
            // Segmento con el que trabajamos
            segmentoActual = solucionInicial.getRuta().get(corte);
            //System.out.println("Trabajando con segmento: " + segmentoActual);
            // costo de recorrer ese segmento
            costoSegmento = evaluador.evaluarSegmento(segmentoActual);
            costesMejoresSegmentos.add(corte, costoSegmento);
            listaMejoresSegmentos.add(segmentoActual);
            costoSegmentoMejor = costoSegmento;
            costoAux = solucionInicial.getCosto() - costoSegmento;

            hayMejora = true;
            while (hayMejora) {
                hayMejora = false;

                //System.out.println("--- Generando vecinos ---");
                // En cada segmento, aplicamos el cambio
                for (int i = 0; i < segmentoActual.size() - 1; i++) {
                    for (int j = 0; j < segmentoActual.size(); j++) {

                        if (( j == i) || (j == i + 1)) {
                            continue;
                        }
                        // Aplicamos el cambio
                        segmentoCambiado = aplicarCambio(segmentoActual, i, j);
                        //System.out.println("Segmento cambiado: " + segmentoCambiado);
                        // Evaluamos el costo del segmento cambiado
                        costoSegmentoCambiado = evaluador.evaluarSegmento(segmentoCambiado);
                        //System.out.println("costo segmento cambiado: "  + costoSegmentoCambiado);

                        if (costoSegmentoCambiado < costoSegmentoMejor) {
                            hayMejora = true;
                            segmentoMejor = segmentoCambiado;
                            costoSegmentoMejor = costoSegmentoCambiado;
                            costoMejor = costoAux + costoSegmentoCambiado;

                            //  Guardo en la lista de costes el nuevo coste que ha mejorado el coste inicial
                            costesMejoresSegmentos.set(corte, costoSegmentoMejor);

                            // Guardo en la lista de segmentos ese segmento que ha dado mejor resultado
                            listaMejoresSegmentos.set(corte, segmentoMejor);
                        }
                    }
                }
                //System.out.println("--- Todos los vecinos generados ---");
                // Una vez generados todos los vecinos, nos quedamos con el mejor
                if (hayMejora) {
                    segmentoActual = segmentoMejor;
                    // Si hay mejora, entonces aceptamos una solucion nueva
                    numAceptaciones++;
                }
            }
            // Ya no hay mejoras para el segmento[n], tengo que ver la diferencia de costes entre el segmento inicial y el segmento mejor conseguido para ese corte
            // y evaluar para ver cual es el indice que nos da mejor resultado
            diferencia = costoSegmento -  costesMejoresSegmentos.get(corte);

            if (diferencia > diferenciaMayor) {
                diferenciaMayor = diferencia;
                indiceMejorSegmento = corte;
            }
        }
        // Comprobar que haya mejor solucion
        if (indiceMejorSegmento != -1) {
            double costoSinSegmentoACambiar = solucionInicial.getCosto() - evaluador.evaluarSegmento(solucionInicial.getRuta().get(indiceMejorSegmento));
            double costoSegmentoACambiar = evaluador.evaluarSegmento(listaMejoresSegmentos.get(indiceMejorSegmento));
            solucionMejor.setCosto(costoSinSegmentoACambiar + costoSegmentoACambiar);
            solucionMejor.setSegmento(indiceMejorSegmento, listaMejoresSegmentos.get(indiceMejorSegmento));

        }
        //System.out.println(numAceptaciones);
        return solucionMejor;
    }

    public String getNombre() { return nombre; }
}
