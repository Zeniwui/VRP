package utils;

public class RegistroCSV {
    private final int iteracion;
    private final double tiempoUs;
    private final double coste;
    private final String operador;
    private final String instancia;
    private final String rutaActual;
    private final boolean conSplit;

    public RegistroCSV(int iteracion, double tiempoUs, double coste, String operador, String instancia, String rutaActual, boolean conSplit) {
        this.iteracion = iteracion;
        this.tiempoUs = tiempoUs;
        this.coste = coste;
        this.operador = operador;
        this.instancia = instancia;
        this.rutaActual = rutaActual;
        this.conSplit = conSplit;
    }

    public int getIteracion() { return iteracion; }
    public double getTiempoUs() { return tiempoUs; }
    public double getCoste() { return coste; }
    public String getOperador() { return operador; }
    public String getInstancia() { return instancia; }
    public String getRutaActual() { return rutaActual; }
    public boolean isConSplit() { return conSplit; }
}
