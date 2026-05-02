package model;

public class Input {
    private static Input instancia;

    private int dimension;
    private int capacidad;
    private double[][] tiempos;
    private int[] demandas;
    private int[] coordX;
    private int[] coordY;
    private double[][] distancias;

    private Input() {}

    /* SINGLETON */
    public static Input getInstancia() {
        if (instancia == null) {
            instancia = new Input();
        }
        return instancia;
    }

    // Getters y Setters
    public int getDimension() { return dimension; }
    public void setDimension(int dimension) { this.dimension = dimension; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public double[][] getTiempos() { return tiempos; }
    public void setTiempos(double[][] tiempos) { this.tiempos = tiempos; }

    public int[] getDemandas() { return demandas; }
    public void setDemandas(int[] demandas) { this.demandas = demandas; }

    public int[] getCoordX() { return coordX; }
    public void setCoordX(int[] coordX) { this.coordX = coordX; }

    public int[] getCoordY() { return coordY; }
    public void setCoordY(int[] coordY) { this.coordY = coordY; }

    public double[][] getDistancias() { return distancias; }
    public void setDistancias(double[][] distancias) { this.distancias = distancias; }
    public void mostrarDatosCargados() {
        System.out.println("\n==========================================");
        System.out.println("   VERIFICACIÓN DE DATOS CARGADOS");
        System.out.println("==========================================");
        System.out.println("Dimensión (nodos): " + this.dimension);
        System.out.println("Capacidad máxima:  " + this.capacidad);
        System.out.println("------------------------------------------");

        /*
        System.out.println("DEMANDAS POR NODO:");
        if (this.demandas != null) {
            for (int i = 0; i < demandas.length; i++) {
                System.out.print("[" + i + ": " + demandas[i] + " cajas]  ");
                if ((i + 1) % 3 == 0) System.out.println();
            }
        }
         */
        System.out.println("\n==========================================\n");
    }
}