package io.parsers;

import model.Input;
import java.util.List;

public class LectorFormatoSolomon implements LectorFormato {

    @Override
    public boolean soportaFormato(List<String> lineas) {
        return lineas.stream().anyMatch(l -> l.contains("VEHICLE") || l.contains("CUST NO."));
    }

    @Override
    public void parsear(List<String> lineas, Input input) {
        int dimension = 0;
        boolean contandoNodos = false;

        // Primero, calculamos la dimensión contando los clientes
        for (String linea : lineas) {
            String l = linea.trim();
            if (l.startsWith("CUST NO.")) {
                contandoNodos = true;
                continue;
            }
            if (contandoNodos && !l.isEmpty()) {
                dimension++;
            }
        }
        input.setDimension(dimension);

        int[] demandas = new int[dimension];
        int[] coordX = new int[dimension];
        int[] coordY = new int[dimension];

        boolean leerNodos = false;
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty()) continue;

            if (linea.startsWith("NUMBER")) {
                String[] datosVehiculo = lineas.get(++i).trim().split("\\s+");
                input.setCapacidad(Integer.parseInt(datosVehiculo[1]));
            } else if (linea.startsWith("CUST NO.")) {
                leerNodos = true;
            } else if (leerNodos) {
                String[] datos = linea.split("\\s+");
                if (datos.length >= 7) {
                    int idNodo = Integer.parseInt(datos[0]);
                    coordX[idNodo] = Integer.parseInt(datos[1]);
                    coordY[idNodo] = Integer.parseInt(datos[2]);
                    demandas[idNodo] = Integer.parseInt(datos[3]);
                }
            }
        }

        input.setCoordX(coordX);
        input.setCoordY(coordY);
        input.setDemandas(demandas);

        // Calcular distancias
        input.setDistancias(calcularDistanciaEuclidea(coordX, coordY, dimension));
    }

    private double[][] calcularDistanciaEuclidea(int[] coordX, int[] coordY, int dimension) {
        double[][] distancias = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i != j) {
                    double dx = coordX[i] - coordX[j];
                    double dy = coordY[i] - coordY[j];
                    distancias[i][j] = Math.sqrt(dx * dx + dy * dy);
                } else {
                    distancias[i][j] = 0.0;
                }
            }
        }
        return distancias;
    }
}