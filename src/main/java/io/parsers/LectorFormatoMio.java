package io.parsers;

import model.Input;

import java.util.List;

public class LectorFormatoMio implements LectorFormato {
    @Override
    public boolean soportaFormato(List<String> lineas) {
        return lineas.stream().anyMatch(l -> l.startsWith("DIMENSION:") || l.startsWith("CAPACIDAD:"));
    }

    @Override
    public void parsear(List<String> lineas, Input input) {
        int dim = 0;
        double[][] tiempos = null;
        int[] demandas = null;

        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty()) continue;

            if (linea.startsWith("DIMENSION")) {
                dim = Integer.parseInt(linea.split(":")[1].trim());
                input.setDimension(dim);
            } else if (linea.startsWith("CAPACIDAD:")) {
                input.setCapacidad(Integer.parseInt(linea.split(":")[1].trim()));
            } else if (linea.startsWith("TIEMPOS")) {
                tiempos = new double[dim][dim];
                for (int f = 0; f < dim; f++) {
                    String[] fila = lineas.get(++i).trim().split("\\s+");
                    for (int c = 0; c < dim; c++) {
                        tiempos[f][c] = Double.parseDouble(fila[c]);
                    }
                }
                input.setTiempos(tiempos);
            } else if (linea.startsWith("DEMANDA")) {
                demandas = new int[dim];
                for (int d = 0; d < dim; d++) {
                    String[] datosDemanda = lineas.get(++i).trim().split("\\s+");
                    int idNodo = Integer.parseInt(datosDemanda[0]);
                    int cant = Integer.parseInt(datosDemanda[1]);
                    demandas[idNodo] = cant;
                }
                input.setDemandas(demandas);
            }
        }
    }
}
