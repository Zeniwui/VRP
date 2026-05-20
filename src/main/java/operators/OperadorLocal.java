package operators;

import model.Solucion;
import utils.ExportadorCSV;

public interface OperadorLocal {
    Solucion generarMinimoLocal(Solucion solucionInicial);
    Solucion generarMinimoTodosSegmentos(Solucion solucionInicial);
    String getNombre();
    void setExportador(ExportadorCSV exportador, String instancia, String permutacion, boolean conSplit);
}
