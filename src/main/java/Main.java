import implementations.ImplementacionSolomon;

public class Main {
    private static final int SEMILLA = 2533;
    private static final int NUM_PERMUTACIONES = 30;

    public static void main(String[] args) {

        ImplementacionSolomon solomon = new ImplementacionSolomon();
        solomon.implementar(SEMILLA, NUM_PERMUTACIONES);
    }
}
