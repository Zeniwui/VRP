import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int SEMILLA = 2533;
    private static List<Solucion> soluciones;

    public static void main(String[] args) {
        soluciones = new ArrayList<>();

        Input input = Input.getInstancia();
        input.cargarDatos("input.txt");

        input.mostrarDatosCargados();

        int dimension = input.getDimension();

        List<Integer> permutacionInicial = generarPermutacionAleatoria(dimension);
        System.out.println("Permutación aleatoria inicial: " + permutacionInicial);

        evaluarPermutacion(permutacionInicial, input);
        System.out.println(soluciones);

        List<Integer> permutacionNueva = intercambio2opt(permutacionInicial, 2, 3);
        System.out.println(permutacionInicial);
        System.out.println(permutacionNueva);

        // Algoritmo 2-opt
        int tiempoMinimo = soluciones.get(0).getTiempo();


    }
    /*
     * Generar una permutacion aleatoria con una semilla
     */
    public static List<Integer> generarPermutacionAleatoria(int dimension) {
        List<Integer> permutacion = new ArrayList<>();

        for (int i = 1; i < dimension; i++) {
            permutacion.add(i);
        }

        Random random = new Random(SEMILLA);

        Collections.shuffle(permutacion, random);

        return permutacion;
    }
    /*
     * Evaluar una permutacion y conseguir el tiempo que se tarda con su ruta. Se añade a la lista de soluciones
     */
    public static void evaluarPermutacion(List<Integer> permutacionAEvaluar, Input input) {
        int tiempoTotal = 0;
        // int capacidadTotal = input.getCapacidad();
        int capacidadTotal = 15;
        int dimension = input.getDimension();
        int[] demandas = input.getDemandas();
        int[][] tiempos = input.getTiempos();

        ArrayList<ArrayList<Integer>> ruta = new ArrayList<>();
        ruta.add(new ArrayList<>());

        int cajasRestantes = capacidadTotal;
        int nodoDondeEstoy = 0;
        int i = 0;
        int corte = 0;
        while (i < dimension - 1) {

            int nodoAIr = permutacionAEvaluar.get(i);

            if (demandas[nodoAIr] <= cajasRestantes) {  // Si tengo suficentes cajas para visitar el nodoAIr
                // Voy al nodo
                tiempoTotal += tiempos[nodoDondeEstoy][nodoAIr];
                // Ya estoy en el nodo
                nodoDondeEstoy = nodoAIr;
                // Dejo las cajas en el nodo
                cajasRestantes = cajasRestantes - demandas[nodoDondeEstoy];
                // Pongo nodo en la ruta
                ruta.get(corte).add(nodoDondeEstoy);
            } else {    // Si no tengo suficientes cajas, hay que ir de vuelta al nodo 0
                // Creo corte en la solucion
                ruta.add(new ArrayList<>());
                corte++;
                // Voy de vuelta al nodo 0
                tiempoTotal += tiempos[nodoDondeEstoy][0];
                // Recargo cajas
                cajasRestantes = capacidadTotal;
                // Voy al nodo que me tocaba segun la permutacion
                tiempoTotal += tiempos[0][nodoAIr];
                // Ya estoy en el nuevo nodo
                nodoDondeEstoy = nodoAIr;
                // Dejo las cajas en el nodo
                cajasRestantes = cajasRestantes - demandas[nodoDondeEstoy];
                // Pongo nodo en la ruta
                ruta.get(corte).add(nodoDondeEstoy);
            }
            i++;
        }
        soluciones.add(new Solucion(ruta, tiempoTotal));
    }
    /*
     * Intercambia dos indices en la permutacion
     */
    public static List<Integer> intercambio2opt (List<Integer> permutacion, int vertice1, int vertice2) {
        List<Integer> permutacionCambiada = new ArrayList<>(permutacion);
        int valor1 = permutacionCambiada.get(vertice1);
        int valor2 = permutacionCambiada.get(vertice2);

        permutacionCambiada.set(vertice1, valor2);
        permutacionCambiada.set(vertice2, valor1);

        return permutacionCambiada;
    }

}
