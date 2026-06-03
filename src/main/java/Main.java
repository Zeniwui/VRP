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

        if (args.length < 1) {
            System.err.println("Uso: java -jar VRP.jar <archivo_instancia>");
            System.exit(1);
        }

        String instancia = args[0];
//        String instancia = "CMT5.vrp";

        ImplementacionTSPLIB tsplib = new ImplementacionTSPLIB();
        tsplib.implementar(instancia, SEMILLA, NUM_PERMUTACIONES);

    }
}
