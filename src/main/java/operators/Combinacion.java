package operators;

import model.Solucion;
import operators.Operador2Opt;
import operators.OperadorLocal;
import operators.OperadorOrOpt;
import operators.OperadorSwap;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class Combinacion implements OperadorLocal {

    Operador2Opt operador2Opt;
    OperadorOrOpt operadorOrOpt;
    OperadorSwap operadorSwap;
    Evaluador evaluador;

    public Combinacion(Operador2Opt op1, OperadorOrOpt op2, OperadorSwap op3, Evaluador ev) {
        operador2Opt = op1;
        operadorOrOpt = op2;
        operadorSwap = op3;
        evaluador = ev;
    }

    public Solucion generarMinimoTodosSegmentos(Solucion solucionInicial) {
        int contador2Opt = 0;
        int contadorOrOpt = 0;
        int contadorSwap = 0;

        Solucion solucionActual = new Solucion(solucionInicial);
        boolean hayMejora = true;

        while (hayMejora) {
            hayMejora = false;

            Solucion mejor2Opt = vecinos2Opt(solucionActual);
            Solucion mejorOrOpt = vecinosOrOpt(solucionActual);
            Solucion mejorSwap = vecinosSwap(solucionActual);

            double mejorCosto = solucionActual.getCosto();
            Solucion nuevaSolucion = null;
            String operadorGanador = null;

            if (mejor2Opt.getCosto() < mejorCosto) {
                mejorCosto = mejor2Opt.getCosto();
                nuevaSolucion = mejor2Opt;
                operadorGanador = "2-opt";
            }
            if (mejorOrOpt.getCosto() < mejorCosto) {
                mejorCosto = mejorOrOpt.getCosto();
                nuevaSolucion = mejorOrOpt;
                operadorGanador = "or-opt";
            }
            if (mejorSwap.getCosto() < mejorCosto) {
                mejorCosto = mejorSwap.getCosto();
                nuevaSolucion = mejorSwap;
                operadorGanador = "swap";
            }

            if (nuevaSolucion != null) {
                solucionActual = nuevaSolucion;
                hayMejora = true;
                switch (operadorGanador) {
                    case "2-opt": contador2Opt++; break;
                    case "or-opt": contadorOrOpt++; break;
                    case "swap": contadorSwap++; break;
                }
            }
        }

        /*System.out.println("Aceptaciones 2-opt: " + contador2Opt);
        System.out.println("Aceptaciones OR-opt: " + contadorOrOpt);
        System.out.println("Aceptaciones swap: " + contadorSwap);*/

        return solucionActual;
    }

    public Solucion vecinos2Opt(Solucion s) {
        Solucion mejor = new Solucion(s);
        double mejorCosto = s.getCosto();

        for (int corte = 0; corte < s.getRuta().size(); corte++) {
            List<Integer> segmento = s.getRuta().get(corte);
            double costoSegOriginal = evaluador.evaluarSegmento(segmento);
            double costoSinSeg = s.getCosto() - costoSegOriginal;

            for (int i = 0; i <= segmento.size() - 2; i++) {
                for (int j = i + 1; j <= segmento.size() - 1; j++) {
                    List<Integer> segCambiado = operador2Opt.aplicarCambio(segmento, i, j);
                    double costoSegCambiado = evaluador.evaluarSegmento(segCambiado);
                    double costoTotal = costoSinSeg + costoSegCambiado;

                    if (costoTotal < mejorCosto) {
                        mejorCosto = costoTotal;
                        mejor = new Solucion(s);
                        mejor.setSegmento(corte, segCambiado);
                        mejor.setCosto(costoTotal);
                    }
                }
            }
        }

        return mejor;
    }

    public Solucion vecinosOrOpt(Solucion s) {
        Solucion mejor = new Solucion(s);
        double mejorCosto = s.getCosto();

        for (int corte = 0; corte < s.getRuta().size(); corte++) {
            List<Integer> segmento = s.getRuta().get(corte);
            double costoSegOriginal = evaluador.evaluarSegmento(segmento);
            double costoSinSeg = s.getCosto() - costoSegOriginal;

            for (int i = 0; i < segmento.size() - 1; i++) {
                for (int j = 0; j < segmento.size(); j++) {
                    if ((j == i) || (j == i + 1)) {
                        continue;
                    }

                    List<Integer> segCambiado = operadorOrOpt.aplicarCambio(segmento, i, j);
                    double costoSegCambiado = evaluador.evaluarSegmento(segCambiado);
                    double costoTotal = costoSinSeg + costoSegCambiado;

                    if (costoTotal < mejorCosto) {
                        mejorCosto = costoTotal;
                        mejor = new Solucion(s);
                        mejor.setSegmento(corte, segCambiado);
                        mejor.setCosto(costoTotal);
                    }
                }
            }
        }

        return mejor;
    }

    public Solucion vecinosSwap(Solucion s) {
        Solucion mejor = new Solucion(s);
        double mejorCosto = s.getCosto();

        for (int i = 0; i < s.getRuta().size() - 1; i++) {
            List<Integer> seg1 = s.getRuta().get(i);
            double costoSeg1 = evaluador.evaluarSegmento(seg1);

            for (int j = i + 1; j < s.getRuta().size(); j++) {
                List<Integer> seg2 = s.getRuta().get(j);
                double costoSeg2 = evaluador.evaluarSegmento(seg2);
                double costoSinSegs = s.getCosto() - costoSeg1 - costoSeg2;

                List<List<Integer>> segmentosACambiar = new ArrayList<>(2);
                segmentosACambiar.add(seg1);
                segmentosACambiar.add(seg2);

                for (int k = 0; k < seg1.size(); k++) {
                    for (int l = 0; l < seg2.size(); l++) {
                        List<List<Integer>> segCambiados = operadorSwap.aplicarCambio(segmentosACambiar, k, l);

                        if (segCambiados != null) {
                            double costoNuevo1 = evaluador.evaluarSegmento(segCambiados.get(0));
                            double costoNuevo2 = evaluador.evaluarSegmento(segCambiados.get(1));
                            double costoTotal = costoSinSegs + costoNuevo1 + costoNuevo2;

                            if (costoTotal < mejorCosto) {
                                mejorCosto = costoTotal;
                                mejor = new Solucion(s);
                                mejor.setSegmento(i, segCambiados.get(0));
                                mejor.setSegmento(j, segCambiados.get(1));
                                mejor.setCosto(costoTotal);
                            }
                        }
                    }
                }
            }
        }

        return mejor;
    }

    @Override
    public Solucion generarMinimoLocal(Solucion solucionInicial) {
        return null;
    }


    @Override
    public String getNombre() {
        return "Combinacion 2-opt, OR-opt y Swap";
    }
}
