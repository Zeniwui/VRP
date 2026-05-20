package operators;

import model.Input;
import model.Solucion;
import utils.Evaluador;
import utils.ExportadorCSV;

import java.util.ArrayList;
import java.util.List;

public class OperadorSwap implements OperadorLocal{

    private Evaluador evaluador;
    private Input input;
    private String nombre = "Swap";
    private ExportadorCSV exportador;
    private String instancia;
    private String permutacion;
    private boolean conSplit;

    public OperadorSwap(Evaluador evaluador) {
        this.evaluador = evaluador;
        input = Input.getInstancia();
    }

    @Override
    public void setExportador(ExportadorCSV exportador, String instancia, String permutacion, boolean conSplit) {
        this.exportador = exportador;
        this.instancia = instancia;
        this.permutacion = permutacion;
        this.conSplit = conSplit;
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
        return new Solucion(null, 3);
    }
    @Override
    public Solucion generarMinimoTodosSegmentos(Solucion solucionInicial) {
        long inicio = System.nanoTime();
        int iteracion = 0;

        if (exportador != null) {
            exportador.registrar(iteracion, 0.0, solucionInicial.getCosto(), nombre, instancia, solucionInicial.getRuta().toString(), conSplit);
        }

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

        while (hayMejora) {
            hayMejora = false;
            costoRutaActual = evaluador.evaluarRutaCompleta(rutaActual);
            for (int i = 0; i < tamanoRuta - 1; i++) {
                for (int j = i + 1; j < tamanoRuta; j++) {
                    segmentosACambiar.clear();
                    segmentosACambiar.add(rutaActual.get(i));
                    segmentosACambiar.add(rutaActual.get(j));

                    segmento1 = new ArrayList<>(segmentosACambiar.get(0));
                    costoSeg1 = evaluador.evaluarSegmento(segmento1);
                    segmento2 = new ArrayList<>(segmentosACambiar.get(1));
                    costoSeg2 = evaluador.evaluarSegmento(segmento2);

                    costoAux = costoRutaActual - costoSeg1 - costoSeg2;

                    for (int k = 0; k < segmento1.size(); k++) {
                        for (int l = 0; l < segmento2.size(); l++) {
                            segmentosCambiados = aplicarCambio(segmentosACambiar, k, l);
                            if (segmentosCambiados != null) {
                                costoNuevo1 = evaluador.evaluarSegmento(segmentosCambiados.get(0));
                                costoNuevo2 = evaluador.evaluarSegmento(segmentosCambiados.get(1));
                                costoNuevoTotal = costoAux + costoNuevo1 + costoNuevo2;

                                if (costoNuevoTotal < costoMejor) {
                                    hayMejora = true;
                                    costoMejor = costoNuevoTotal;
                                    solucionMejor.copiarRuta(rutaActual);
                                    solucionMejor.setSegmento(i, segmentosCambiados.get(0));
                                    solucionMejor.setSegmento(j, segmentosCambiados.get(1));
                                    solucionMejor.setCosto(costoNuevoTotal);
                                }
                            }
                        }
                    }
                }
            }
            rutaActual = solucionMejor.getRuta();
            if (hayMejora) {
                iteracion++;
                if (exportador != null) {
                    double tiempo = (System.nanoTime() - inicio) / 1_000.0;
                    exportador.registrar(iteracion, tiempo, costoMejor, nombre, instancia, solucionMejor.getRuta().toString(), conSplit);
                }
            }
        }

        return solucionMejor;
    }

    public String getNombre() { return nombre; }
}
