package implementations;

import model.Solucion;
import operators.OperadorLocal;

import java.util.List;

public class Combinacion {

    List<OperadorLocal> operadores;

    public Combinacion(List<OperadorLocal> operadores) {
        this.operadores = operadores;
    }

    public Solucion generarMinimo(Solucion solucionInicial) {
        // Partiendo de una solucion tengo que generar los vecinos de los tres operadores
        // Tengo que ir guardando los mejores resultados que se vayan generando
    }

    // Puedo generar las vecindades con diferentes metodos
    // El metodo me da el mejor de los vecinos
    // Y despues los comparo en el metodo principal
    public Solucion vecinos2Opt (List<Integer> segmentoInicial) {

    }
}
