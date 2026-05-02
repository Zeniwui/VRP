package io.parsers;

import model.Input;
import java.util.List;

public class LectorFormatoTSPLIB implements LectorFormato {

    @Override
    public boolean soportaFormato(List<String> lineas) {
        return lineas.stream().anyMatch(l -> l.startsWith("TYPE : CVRP") || l.startsWith("NODE_COORD_SECTION"));
    }

    @Override
    public void parsear(List<String> lineas, Input input) {
        int dim = 0;
        int[] coordX = null;
        int[] coordY = null;
        int[] demandas = null;

        String seccionActual = "";

        for (String linea : lineas) {
            String l = linea.trim();
            if (l.isEmpty() || l.equals("EOF")) continue;

            if (l.startsWith("DIMENSION")) {
                dim = Integer.parseInt(l.split(":")[1].trim());
                input.setDimension(dim);
                coordX = new int[dim + 1];
                coordY = new int[dim + 1];
                demandas = new int[dim + 1];
            } else if (l.startsWith("CAPACITY")) {
                input.setCapacidad(Integer.parseInt(l.split(":")[1].trim()));
            } else if (l.startsWith("NODE_COORD_SECTION")) {
                seccionActual = "COORD";
            } else if (l.startsWith("DEMAND_SECTION")) {
                seccionActual = "DEMAND";
            } else if (l.startsWith("DEPOT_SECTION")) {
                seccionActual = "DEPOT";
            } else {
                // Leer datos según la sección actual
                String[] datos = l.split("\\s+");
                if (seccionActual.equals("COORD") && datos.length >= 3) {
                    int id = Integer.parseInt(datos[0]);
                    coordX[id] = (int) Double.parseDouble(datos[1]);
                    coordY[id] = (int) Double.parseDouble(datos[2]);
                } else if (seccionActual.equals("DEMAND") && datos.length >= 2) {
                    int id = Integer.parseInt(datos[0]);
                    demandas[id] = Integer.parseInt(datos[1]);
                }
            }
        }

        int[] coordXAjustado = new int[dim];
        int[] coordYAjustado = new int[dim];
        int[] demandasAjustado = new int[dim];

        for(int i = 1; i <= dim; i++) {
            coordXAjustado[i-1] = coordX[i];
            coordYAjustado[i-1] = coordY[i];
            demandasAjustado[i-1] = demandas[i];
        }

        input.setCoordX(coordXAjustado);
        input.setCoordY(coordYAjustado);
        input.setDemandas(demandasAjustado);
    }
}