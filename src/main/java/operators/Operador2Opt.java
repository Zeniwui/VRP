package operators;


import model.Input;
import model.Solucion;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class Operador2Opt implements OperadorLocal {
    private Evaluador evaluador;
    private List<Solucion> historial;
    private Input input;
    private String nombre = "2-opt";
    public Operador2Opt(Evaluador evaluador) {
        this.evaluador = evaluador;
        historial = new ArrayList<>();
    }

    public List<Integer> aplicarCambio(List<Integer> segmento, int v1, int v2) {
        List<Integer> nuevoSegmento = new ArrayList<>(segmento);
        for (int i = v1, j = v2; i < j; i++, j--){
            int aux = nuevoSegmento.get(i);
            nuevoSegmento.set(i, nuevoSegmento.get(j));
            nuevoSegmento.set(j, aux);
        }
        return nuevoSegmento;
    }
    /**
     * Funcion que dada una solucion inicial (de la forma [[1, 2, 3], [4, 5, 6]], itera por todas los segmentos del array.
     * Para cada segmento, se generan todos sus vecinos y se escoge de entre ellos, el de mejor resultado.
     * Se vuelve a repetir el proceso hasta no encontrar vecinos que mejoren el resultado que ya teníamos.
     */
    @Override
    public Solucion generarMinimoLocal(Solucion solucionInicial) {
        Solucion solucionMejor = new Solucion(new ArrayList<>(solucionInicial.getRuta()), solucionInicial.getCosto());
        List<List<Integer>> rutaActual = solucionInicial.getRuta();
        double costoMejor = solucionInicial.getCosto();

        List<Integer> segmentoActual, segmentoMejor = null, segmentoCambiado = null;
        int numeroCortes = solucionInicial.getRuta().size();  //Numero de segmentos que tiene la rutaInicial de la que partimos
        double costoSegmento, costoSegmentoCambiado,  costoSegmentoMejor, costoAux;
        int indiceMejorSegmento = -1;
        boolean hayMejora;

        // Como la ruta inicial puede tener varios cortes (Ej: [[1, 2, 3], [4, 5, 6]]
        // debemos aplicar el operador en cada segmento
        for (int corte = 0; corte < numeroCortes; corte++) {
            // Guardamos el segmento actual con el que estamos trabajando
            segmentoActual = solucionInicial.getRuta().get(corte);
            //System.out.println("Trabajando con segmento: " + segmentoActual);
            // Guardamos el costo de recorrer ese segmento
            costoSegmento = evaluador.evaluarSegmento(segmentoActual);
            // Guardo en una variable auxiliar el costo si quitamos el segmento actual con el que vamos a trabajar
            // Utilizamos evaluacion delta para agilizar las operaciones y no tener que evaluar toda una ruta completa para conseguir el costo
            costoAux = costoMejor - costoSegmento;
            costoSegmentoMejor = costoSegmento;

            hayMejora = true;
            // Bucle para encontrar el mínimo local
            while (hayMejora) {
                hayMejora = false;

                //System.out.println("--- Generando vecinos ---");
                //Bucle para encontrar todos los vecinos de un segmento inicial
                for (int i = 0; i <= segmentoActual.size() - 2; i++) {
                    for (int j = i + 1; j <= segmentoActual.size() - 1; j++) {
                        // Aplicamos el intercambio 2-opt
                        segmentoCambiado = aplicarCambio(segmentoActual, i, j);
                        //System.out.println("Segmento cambiado: " + segmentoCambiado);
                        // Calculamos el nuevo costo del segmento intercambiado
                        costoSegmentoCambiado = evaluador.evaluarSegmento(segmentoCambiado);

                        // Si el costo del segmento cambiado es mejor que el inicial, debemos guardarlo como posible solucion
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
                // Una vez hemos encontrado todos los vecinos del segmento inicial, nos tenemos que quedar con el de mejor resultado
                // Y debemos entonces generar los vecinos de esta nueva solucion
                // El segmento actual con el que tenemos que trabajar será el que diga el indiceMejorcosto
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

    public List<Solucion> getHistorial() {
        return historial;
    }
    public String getNombre() { return nombre; }
}
