import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int SEMILLA = 2533;
    private static List<Solucion> soluciones;

    private static int dimension;
    private static int capacidadTotal;
    private static int[] demandas;
    private static int[][] tiempos;

    public static void main(String[] args) {
        soluciones = new ArrayList<>();

        Input input = Input.getInstancia();
        input.cargarDatos("input.txt");

        dimension = input.getDimension();
        capacidadTotal = input.getCapacidad();
        demandas = input.getDemandas();
        tiempos = input.getTiempos();

        input.mostrarDatosCargados();

        List<Integer> permutacionInicial = generarPermutacionAleatoria();
        System.out.println("Permutación aleatoria inicial: " + permutacionInicial);

        evaluarPermutacion(permutacionInicial);
        System.out.println(soluciones);

        // Algoritmo 2-opt
        Solucion solucionOptimaLocal = algoritmo2opt(permutacionInicial);
        System.out.println("Lista de posibles soluciones: \n" + soluciones);
        System.out.println("Solucion optima local: \n" + solucionOptimaLocal);

    }
    /*
     * Generar una permutacion aleatoria con una semilla
     */
    public static List<Integer> generarPermutacionAleatoria() {
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
    public static void evaluarPermutacion(List<Integer> permutacionAEvaluar) {
        int tiempoTotal = 0;

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
    /*
     * Utiliza el algoritmo 2-opt para encontrar el optimo local
     * PROBLEMAS:
     * - Verifica permutaciones que ya fueron probadas anteriormente (en la primera iteracion)
     */
    public static Solucion algoritmo2opt (List<Integer> permutacionInicial) {
        int numeroNodos = dimension - 1;
        int mejorTiempo = soluciones.get(0).getTiempo();
        int nuevoTiempo;
        boolean hayMejora = true;
        List<Integer> permutacionAnterior = new ArrayList<>(permutacionInicial);
        List<Integer> permutacionNueva;
        boolean romperBucle1;
        int indiceMejorTiempo = 0;
        int contador = 0;

        while (hayMejora) {
            hayMejora = false;
            for (int i = 0; i <= numeroNodos - 2; i++) {
                romperBucle1 = false;
                for (int j = i + 1; j <= numeroNodos - 1; j++) {
                    permutacionNueva = intercambio2opt(permutacionAnterior, i, j);
                    evaluarPermutacion(permutacionNueva);
                    contador++;
                    nuevoTiempo = soluciones.get(soluciones.size()-1).getTiempo();
                    if (nuevoTiempo < mejorTiempo) {
                        indiceMejorTiempo = contador;
                        permutacionAnterior = permutacionNueva;
                        mejorTiempo = nuevoTiempo;
                        hayMejora = true;
                        romperBucle1 = true;
                        break;
                    }
                }
                if (romperBucle1) {
                    break;
                }
            }
        }
        return soluciones.get(indiceMejorTiempo);
    }

}
