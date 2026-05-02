package operators;

import model.Solucion;

public interface OperadorLocal {
    Solucion generarMinimoLocal(Solucion solucionInicial);
    Solucion generarMinimoTodosSegmentos(Solucion solucionInicial);
    String getNombre();
}
