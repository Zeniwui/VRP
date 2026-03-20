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
    private int[][] tiempos;
    private int[] demandas;
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
    public void cargarDatos(String nombreFichero) {

        try {
            URI uri = getClass().getClassLoader().getResource(nombreFichero).toURI();
            List<String> lineas = Files.readAllLines(Paths.get(uri));
            parsearDatos(lineas);

        } catch (IOException | URISyntaxException e) {
            System.err.println("ERROR: fallo al leer el fichero: " + e.getMessage());
            System.exit(1);
        }
    }

    public void parsearDatos(List<String> lineas) {
        int n = 0;
        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty()) continue;

            if (linea.startsWith("DIMENSION")) {
                this.dimension = Integer.parseInt(linea.split(":")[1].trim());
            } else if (linea.startsWith("CAPACIDAD:")) {
                this.capacidad = Integer.parseInt(linea.split(":")[1].trim());
            } else if (linea.startsWith("TIEMPOS")) {
                this.tiempos = new int[dimension][dimension];
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

    public int getDimension() {
        return dimension;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int[][] getTiempos() {
        return tiempos;
    }

    public int[] getDemandas() {
        return demandas;
    }

}
