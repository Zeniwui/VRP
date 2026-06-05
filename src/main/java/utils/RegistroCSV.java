package utils;

public class RegistroCSV {
    private final int iteracion;
    private final double tiempoUs;
    private final double coste;
    private final double promedioPadres;
    private final String tipoCruce;
    private final double probCruce;
    private final String tipoMutacion;
    private final double probMutacion;
    private final int iteracionesSinMejora;
    private final String estrategiaBL;
    private final double porcentajeBL;
    private final String operador;
    private final String instancia;
    private final String rutaActual;
    private final boolean conSplit;

    public RegistroCSV(int iteracion, double tiempoUs, double coste, double promedioPadres,
                       String tipoCruce, double probCruce, String tipoMutacion, double probMutacion,
                       int iteracionesSinMejora, String estrategiaBL, double porcentajeBL,
                       String operador, String instancia, String rutaActual, boolean conSplit) {
        this.iteracion = iteracion;
        this.tiempoUs = tiempoUs;
        this.coste = coste;
        this.promedioPadres = promedioPadres;
        this.tipoCruce = tipoCruce;
        this.probCruce = probCruce;
        this.tipoMutacion = tipoMutacion;
        this.probMutacion = probMutacion;
        this.iteracionesSinMejora = iteracionesSinMejora;
        this.estrategiaBL = estrategiaBL;
        this.porcentajeBL = porcentajeBL;
        this.operador = operador;
        this.instancia = instancia;
        this.rutaActual = rutaActual;
        this.conSplit = conSplit;
    }

    public int getIteracion() { return iteracion; }
    public double getTiempoUs() { return tiempoUs; }
    public double getCoste() { return coste; }
    public double getPromedioPadres() { return promedioPadres; }
    public String getTipoCruce() { return tipoCruce; }
    public double getProbCruce() { return probCruce; }
    public String getTipoMutacion() { return tipoMutacion; }
    public double getProbMutacion() { return probMutacion; }
    public int getIteracionesSinMejora() { return iteracionesSinMejora; }
    public String getEstrategiaBL() { return estrategiaBL; }
    public double getPorcentajeBL() { return porcentajeBL; }
    public String getOperador() { return operador; }
    public String getInstancia() { return instancia; }
    public String getRutaActual() { return rutaActual; }
    public boolean isConSplit() { return conSplit; }
}
