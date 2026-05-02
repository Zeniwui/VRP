import implementations.ImplementacionSolomon;
import implementations.ImplementacionTSPLIB;
import implementations.Implementation1;
import utils.Evaluador;

import java.awt.image.BaseMultiResolutionImage;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int SEMILLA = 2533;
    private static final int NUM_PERMUTACIONES = 30;

    public static void main(String[] args) {

        Implementation1 mio = new Implementation1();
        //mio.implementar(SEMILLA, NUM_PERMUTACIONES);

        ImplementacionSolomon solomon = new ImplementacionSolomon();
        solomon.implementar(SEMILLA, NUM_PERMUTACIONES);

        ImplementacionTSPLIB tsplib = new ImplementacionTSPLIB();
        //tsplib.implementar(SEMILLA, NUM_PERMUTACIONES);

    }
}
