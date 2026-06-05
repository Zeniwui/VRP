import implementations.ImplementacionSolomon;
import implementations.ImplementacionTSPLIB;
import implementations.Implementation1;
import utils.Evaluador;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int SEMILLA = 2533;
    private static final int NUM_PERMUTACIONES = 30;

    public static void main(String[] args) {

        if (args.length < 6) {
            System.err.println("Uso: java -jar VRP.jar <archivo_instancia> <iteracionesSinMejora> <probCruce> <tipoMutacion> <probMutacion> <estrategiaBL> [porcentajeBL]");
            System.exit(1);
        }

        String instancia = args[0];
        int iteracionesSinMejora = Integer.parseInt(args[1]);
        double probCruce = Double.parseDouble(args[2]);
        String tipoMutacion = args[3];
        double probMutacion = Double.parseDouble(args[4]);
        String estrategiaBL = args[5];
        double porcentajeBL = args.length >= 7 ? Double.parseDouble(args[6]) : 0.5;

        ImplementacionTSPLIB tsplib = new ImplementacionTSPLIB();
        tsplib.implementar(instancia, SEMILLA, NUM_PERMUTACIONES, iteracionesSinMejora, probCruce, tipoMutacion, probMutacion, estrategiaBL, porcentajeBL);

    }
}
