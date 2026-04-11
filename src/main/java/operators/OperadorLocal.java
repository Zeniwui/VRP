package operators;

import model.Solucion;

public interface OperadorLocal {
    Solucion generarMinimoLocal(Solucion solucionInicial);
    String getNombre();
}
