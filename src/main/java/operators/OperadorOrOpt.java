package operators;

import model.Input;
import model.Solucion;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class OperadorOrOpt implements OperadorLocal{

    private Evaluador evaluador;
    private Input input;
    private String nombre = "OR-opt";

    public OperadorOrOpt(Evaluador evaluador) {
        this.evaluador = evaluador;
        input = Input.getInstancia();
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
        List<List<Integer>> rutaActual = solucionInicial.getRuta();
        double costoMejor = solucionInicial.getCosto();
        List<Integer> segmentoActual, segmentoMejor = null;
        List<Integer> segmentoCambiado = null;
        int indiceMejorSegmento = -1;
        double costoSegmento, costoSegmentoCambiado, costoSegmentoMejor, costoAux;
        int numeroCortes = solucionInicial.getRuta().size();
        boolean hayMejora;

        // Tenemos que iterar por todos los segmentos que componen la ruta inicial
        for (int corte = 0; corte < numeroCortes; corte++) {
            // Segmento con el que trabajamos
            segmentoActual = rutaActual.get(corte);
            //System.out.println("Trabajando con segmento: " + segmentoActual);
            // costo de recorrer ese segmento
            costoSegmento = evaluador.evaluarSegmento(segmentoActual);
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
                            indiceMejorSegmento = corte;
                        }
                    }
                }
                //System.out.println("--- Todos los vecinos generados ---");
                // Una vez generados todos los vecinos, nos quedamos con el mejor
                if (hayMejora) {
                    segmentoActual = segmentoMejor;
                }
            }
        }
        // Comprobar que haya mejor solucion
        if (indiceMejorSegmento != -1) {
            solucionMejor.setCosto(costoMejor);
            solucionMejor.setSegmento(indiceMejorSegmento, segmentoMejor);
        }

        return solucionMejor;
    }
    public String getNombre() { return nombre; }
}
