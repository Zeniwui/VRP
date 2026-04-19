package model;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Input {
    private static Input instancia;
    private int dimension;
    private int capacidad;
    private double[][] tiempos;
    private int[] demandas;
    private int[] coordX;
    private int[] coordY;
    private double[][] distancias;

    private Input() {
    }
    /*
        SINGLETON
     */
    public static Input getInstancia() {
        if (instancia == null) {
            instancia = new Input();
        }
        return instancia;
    }
    public void cargarDatosMios(String nombreFichero) {

        try {
            URI uri = getClass().getClassLoader().getResource(nombreFichero).toURI();
            List<String> lineas = Files.readAllLines(Paths.get(uri));
            parsearDatosMios(lineas);

        } catch (IOException | URISyntaxException e) {
            System.err.println("ERROR: fallo al leer el fichero: " + e.getMessage());
            System.exit(1);
        }
    }

    public void parsearDatosMios(List<String> lineas) {
        int n = 0;
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty()) continue;

            if (linea.startsWith("DIMENSION")) {
                this.dimension = Integer.parseInt(linea.split(":")[1].trim());
            } else if (linea.startsWith("CAPACIDAD:")) {
                this.capacidad = Integer.parseInt(linea.split(":")[1].trim());
            } else if (linea.startsWith("TIEMPOS")) {
                this.tiempos = new double[dimension][dimension];
                for (int f = 0; f < dimension; f++) {
                    String[] fila = lineas.get(++i).trim().split("\\s+");
                    for (int c = 0; c < dimension; c++) {
                        this.tiempos[f][c] = Integer.parseInt(fila[c]);
                    }
                }
            } else if (linea.startsWith("DEMANDA")) {
                this.demandas = new int[dimension];
                for (int d = 0; d < dimension; d++) {
                    String[] datosDemanda = lineas.get(++i).trim().split("\\s+");
                    int idNodo = Integer.parseInt(datosDemanda[0]);
                    int cant = Integer.parseInt(datosDemanda[1]);
                    this.demandas[idNodo] = cant;
                }
            }
        }
    }

    public void mostrarDatosCargados() {
        System.out.println("\n==========================================");
        System.out.println("   VERIFICACIÓN DE DATOS CARGADOS");
        System.out.println("==========================================");
        System.out.println("Dimensión (nodos): " + this.dimension);
        System.out.println("Capacidad máxima:  " + this.capacidad);
        System.out.println("------------------------------------------");

        System.out.println("DEMANDAS POR NODO:");
        if (this.demandas != null) {
            for (int i = 0; i < demandas.length; i++) {
                System.out.print("[" + i + ": " + demandas[i] + " cajas]  ");
                if ((i + 1) % 3 == 0) System.out.println();
            }
        }
        System.out.println("\n------------------------------------------");

        System.out.println("MATRIZ DE TIEMPOS (Viajes entre nodos):");
        if (this.tiempos != null) {
            System.out.print("      ");
            for (int i = 0; i < dimension; i++) System.out.printf("N%d    ", i);
            System.out.println("\n      " + "------".repeat(dimension));

            for (int i = 0; i < dimension; i++) {
                System.out.printf("N%d |  ", i);
                for (int j = 0; j < dimension; j++) {
                    System.out.printf("%-5d ", tiempos[i][j]);
                }
                System.out.println();
            }
        }
        System.out.println("==========================================\n");
    }

    public void cargarDatosSolomon(String nombreFichero) {
        try {
            URI uri = getClass().getClassLoader().getResource(nombreFichero).toURI();
            List<String> lineas = Files.readAllLines(Paths.get(uri));
            parsearDatosSolomon(lineas);
        } catch (IOException | URISyntaxException e) {
            System.err.println("ERROR: fallo al leer el fichero de Solomon: " + e.getMessage());
            System.exit(1);
        }
    }

    public void parsearDatosSolomon(List<String> lineas) {
        this.dimension = 0;
        boolean contandoNodos = false;
        for (String linea : lineas) {
            String l = linea.trim();
            if (l.startsWith("CUST NO.")) {
                contandoNodos = true;
                continue;
            }
            if (contandoNodos && !l.isEmpty()) {
                this.dimension++;
            }
        }

        this.demandas = new int[dimension];
        this.coordX = new int[dimension];
        this.coordY = new int[dimension];
        this.distancias = new double[dimension][dimension];
        this.tiempos = new double[dimension][dimension];

        boolean leerNodos = false;
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty()) continue;

            // Leer Capacidad
            if (linea.startsWith("NUMBER")) {
                String[] datosVehiculo = lineas.get(++i).trim().split("\\s+");
                this.capacidad = Integer.parseInt(datosVehiculo[1]);
            }
            // Detectar inicio de clientes
            else if (linea.startsWith("CUST NO.")) {
                leerNodos = true;
            }
            // Leer clientes y rellenar vectores
            else if (leerNodos) {
                String[] datos = linea.split("\\s+");
                if (datos.length >= 7) {
                    int idNodo = Integer.parseInt(datos[0]);
                    this.coordX[idNodo] = Integer.parseInt(datos[1]);
                    this.coordY[idNodo] = Integer.parseInt(datos[2]);
                    this.demandas[idNodo] = Integer.parseInt(datos[3]);

                }
            }
        }
        calcularDistanciaEuclidea();
    }

    private void calcularDistanciaEuclidea() {
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
    }

    public void mostrarDatosCargadosSolomon() {
        System.out.println("\n==========================================");
        System.out.println("   VERIFICACIÓN DE DATOS SOLOMON");
        System.out.println("==========================================");
        System.out.println("Dimensión (nodos): " + this.dimension);
        System.out.println("Capacidad máxima:  " + this.capacidad);
        System.out.println("------------------------------------------");

        System.out.println("DATOS DE LOS NODOS (Depósito = 0):");
        System.out.printf("%-6s %-10s %-10s %-10s%n", "NODO", "COORD X", "COORD Y", "DEMANDA");
        System.out.println("------------------------------------------");
        if (this.coordX != null && this.coordY != null && this.demandas != null) {
            for (int i = 0; i < dimension; i++) {
                System.out.printf("%-6d %-10d %-10d %-10d%n", i, coordX[i], coordY[i], demandas[i]);
            }
        }
        System.out.println("------------------------------------------");

        // Limitamos la vista que sale por consola para no saturar
        int limite = Math.min(15, dimension);

        System.out.println("\nMATRIZ DE DISTANCIAS EUCLÍDEAS (Muestra: primeros " + limite + " nodos):");
        if (this.distancias != null) {
            System.out.print("      ");
            for (int i = 0; i < limite; i++) System.out.printf("N%-6d", i);
            System.out.println("\n      " + "-------".repeat(limite));

            for (int i = 0; i < limite; i++) {
                System.out.printf("N%-3d|  ", i);
                for (int j = 0; j < limite; j++) {
                    System.out.printf("%-6.2f ", distancias[i][j]);
                }
                System.out.println();
            }
        }
        System.out.println("==========================================\n");
    }

    public int getDimension() {
        return dimension;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double[][] getTiempos() {
        return tiempos;
    }

    public int[] getDemandas() {
        return demandas;
    }

    public int[] getCoordX() {
        return coordX;
    }

    public int[] getCoordY() {
        return coordY;
    }

    public double[][] getDistancias() {
        return distancias;
    }
}
