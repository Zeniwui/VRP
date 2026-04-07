package operators;


import model.Input;
import model.Solucion;
import utils.EvaluadorSolucion;

import java.util.ArrayList;
import java.util.List;

public class Operador2Opt implements OperadorLocalIntraRuta{
    private EvaluadorSolucion evaluador;
    private List<Solucion> historial;
    public Operador2Opt(EvaluadorSolucion evaluador) {
        this.evaluador = evaluador;
        historial = new ArrayList<>();
    }
    @Override
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
     * @param solucionInicial
     * @return La Solucion que da el mejor resultado, el minimo local.
     */
    public Solucion generarMinimoLocal(Solucion solucionInicial) {
        Input input = Input.getInstancia();
        List<Integer> segmentoActual, segmentoCambiado;
        int numeroCortes = solucionInicial.getRuta().size();  //Numero de segmentos que tiene la rutaInicial de la que partimos
        int mejorTiempoInicial = solucionInicial.getTiempo();
        int tiempoSegmento, tiempoSegmentoCambiado,  tiempoMejor;
        int indiceMejorTiempo = 0;
        int contador = -1;
        boolean hayMejora;

        // Como la ruta inicial puede tener varios cortes (Ej: [[1, 2, 3], [4, 5, 6]]
        // debemos aplicar el operador en cada segmento
        for (int corte = 0; corte < numeroCortes; corte++) {
            // Guardamos el segmento actual con el que estamos trabajando
            segmentoActual = solucionInicial.getRuta().get(corte);
            // Guardamos el tiempo que se tarda en recorrer ese segmento
            tiempoSegmento = evaluador.evaluarTiempoSegmento(segmentoActual);
            // Guardo en una variable auxiliar el tiempo que se tarda si quitamos el segmento actual con el que vamos a trabajar
            // Utilizamos evaluacion delta para agilizar las operaciones y no tener que evaluar toda una ruta completa para conseguir el tiempo
            int tiempoAux = mejorTiempoInicial - tiempoSegmento;
            tiempoMejor = tiempoSegmento;
            hayMejora = true;

            // Bucle para encontrar el mínimo local
            while (hayMejora) {
                hayMejora = false;

                //Bucle para encontrar todos los vecinos de un segmento inicial
                for (int i = 0; i <= segmentoActual.size() - 2; i++) {
                    for (int j = i + 1; j <= segmentoActual.size() - 1; j++) {
                        // Aplicamos el intercambio 2-opt
                        segmentoCambiado = aplicarCambio(segmentoActual, i, j);
                        // Calculamos el nuevo tiempo del segmento intercambiado
                        tiempoSegmentoCambiado = evaluador.evaluarTiempoSegmento(segmentoCambiado);

                        // Añadimos esta solucion al historial
                        // Hago una copia de la ruta de la solucion inicial
                        ArrayList<ArrayList<Integer>> rutaInicialCopia = new ArrayList<ArrayList<Integer>>(solucionInicial.getRuta());
                        // En la copia, cambio el segmento actual con el que trabajamos por el que intercambiamos
                        rutaInicialCopia.set(corte, (ArrayList<Integer>) segmentoCambiado);
                        // Añado al historial la nueva solucion con su tiempo
                        historial.add(new Solucion(rutaInicialCopia, tiempoAux + tiempoSegmentoCambiado));
                        // Como añadimos algo al historial, el contador (del indice) suma +1
                        contador++;

                        // Si el tiempo del segmento cambiado es mejor que el inicial, debemos guardarlo como posible solucion
                        if (tiempoSegmentoCambiado < tiempoMejor) {
                            hayMejora = true;
                            tiempoMejor = tiempoSegmentoCambiado;
                            // Guardo el indice de la mejor solucion que hay en el historial para despues devolverlo
                            indiceMejorTiempo = contador;
                        }
                    }
                }
                // Una vez hemos encontrado todos los vecinos del segmento inicial, nos tenemos que quedar con el de mejor resultado
                // Y debemos entonces generar los vecinos de esta nueva solucion
                // El segmento actual con el que tenemos que trabajar será el que diga el indiceMejorTiempo
                segmentoActual = historial.get(indiceMejorTiempo).getRuta().get(corte);
                System.out.println("Despues de generar todos los vecinos, el mejor es: " + segmentoActual);
            }
        }
        return historial.get(indiceMejorTiempo);
    }

    public List<Solucion> getHistorial() {
        return historial;
    }
}
