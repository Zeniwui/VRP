package genetic;

import model.Solucion;
import operators.*;
import utils.EvaluadorGenerico;
import utils.GeneradorPermutacion;

import java.util.*;

public class AlgoritmoGenetico {
    /*
     * Necesitamos seguir unos pasos:
     * 1. Poblacion inicial -> Generar una poblacion de permutaciones aleatorias. Por ejemplo, 100 permutaciones
     * 2. Evaluamos esas permutaciones. Podemos utilizar split + combinacion de los operadores que ya tengo. Podemos crear una nueva clase que sea un individuo
     * 3. Entramos en un bucle que tendremos que gestionar si no encontramos mejora en un numero de iteraciones determinadas
     * 4. Seleccionamos pares de soluciones aleatorias
     * 5. Hacemos cruces entre cada par
     * 6. Hacemos mutaciones
     * 7. Evaluamos las nuevas permutaciones. Con split y busqueda local
     * 8. Reemplazamos mediante torneo 2/4. Por ejemplo, cogemos dos padres y dos hijos y nos quedamos con los mejores
     * 9. Volvemos a ejecutar el bucle del punto 3
     */
    private List<Individuo> padres;
    private List<Individuo> hijos;
    private GeneradorPermutacion generador;
    private Split split;
    private Operador2Opt operador2Opt;
    private OperadorOrOpt operadorOrOpt;
    private OperadorSwap operadorSwap;
    private Combinacion operadorCombo;
    private EvaluadorGenerico evaluador;

    private int numPoblacion = 100;
    private int semilla = 2533;
    private double probCruce = 1;
    private double probMutacion = 0.05;
    private Individuo mejorSolucionGlobal;

    private Random random = new Random(semilla);

    public AlgoritmoGenetico(GeneradorPermutacion generador, Split operadorSplit, Combinacion operadorCombo, Operador2Opt operador2Opt, OperadorSwap swap) {
        this.generador = generador;
        this.split = operadorSplit;
        this.operadorCombo = operadorCombo;
        this.operador2Opt = operador2Opt;
        this.operadorSwap = swap;
    }

    public Individuo ejecutar() {
        generarPoblacionInicial(numPoblacion);
        hijos = new ArrayList<>(List.copyOf(padres));

        // Hallamos cual es el mejor individuo del array de padres
        mejorSolucionGlobal = Collections.min(padres, Comparator.comparingDouble(Individuo::getFuncionObjetivo));

        int contadorSinMejora = 0;

        // Comenzamos bucle
        while (contadorSinMejora < 15) {
            System.out.println("Principio bucle");

            // Elegimos pares
            elegirPares();

            // Hacemos los cruces entre los pares y los guardamos en el array de hijos
            for (int i = 0; i < hijos.size(); i += 2) {
                // Usamos la probabilidad marcada de antemano
                if (random.nextDouble() < probCruce) {
                    cruceGOX(hijos.get(i), hijos.get(i + 1), i);
                }
            }

            // Hacemos mutaciones de los hijos
            for (int i = 0; i < hijos.size(); i++) {
                // Solo hacemos mutacion de acuerdo a la probabilidad de mutacion
                if (random.nextDouble() < probMutacion) {
                    mutacionSwap(hijos.get(i), i);
                }
            }

            // Evaluamos las permutaciones de los hijos que han salido
            // Utilizamos split + busqueda local (el que combina varios operadores)
            for (Individuo hijo: hijos) {
                Solucion solucion = split.generarCortes(hijo.getPermutacion());
                solucion = operadorSwap.generarMinimoTodosSegmentos(solucion);
                hijo.setFuncionObjetivo(solucion.getCosto());
                hijo.setRutas(solucion.getRuta());
            }

            // Hacemos reemplazo mediante torneo 2/4. Comparamos 2 padres y 2 hijos y nos quedamos con los 2 mejores
            for (int i = 0; i < padres.size(); i += 2) {
                List<Individuo> candidatos = new ArrayList<>();
                candidatos.add(padres.get(i));
                candidatos.add(padres.get(i+1));
                candidatos.add(hijos.get(i));
                candidatos.add(hijos.get(i+1));
                // Ordenar ascendente por costo (el mejor tiene menor costo)
                candidatos.sort(Comparator.comparingDouble(Individuo::getFuncionObjetivo));
                // Los dos mejores van a padres
                padres.set(i, candidatos.get(0));
                padres.set(i+1, candidatos.get(1));
            }

            // Ahora tenemos que ver si ha habido mejora
            // Obtenemos el mejor individuo de la nueva ronda de padres
            Individuo mejorActual = Collections.min(padres, Comparator.comparingDouble(Individuo::getFuncionObjetivo));
            if (mejorActual.getFuncionObjetivo() < mejorSolucionGlobal.getFuncionObjetivo()) {
                mejorSolucionGlobal = mejorActual;
                contadorSinMejora = 0;
            } else {
                contadorSinMejora++;    // No ha habido mejora
            }

            System.out.println("Final bucle");

        }

        return mejorSolucionGlobal;
    }

    /*
     * Generamos la poblacion inicial con el numero de individuos que le pasamos como parametro
     */
    private void generarPoblacionInicial(int numIndividuos) {
        // Generamos lista de permutaciones aleatoria
        List<List<Integer>> listaPermutaciones =  generador.listaDePermutaciones(numIndividuos);
        padres = new ArrayList<>(numIndividuos);

        // Evaluamos cada permutacion utilizando primero split y TODO: despues generando su minimo local mediante combinacion
        for (List<Integer> permutacion: listaPermutaciones) {
            Solucion solucion = split.generarCortes(permutacion);
            padres.add(new Individuo(permutacion, solucion.getCosto(), solucion.getRuta()));
        }
    }

    /*
     * Para elegir los pares aleatoriamente, voy a mezclar la lista de hijos
     * Los pares resultantes seran los hijos en ese orden que me ha dado el shuffle
     * Es decir: un par será hijos[0] y hijos[1]
     * Otro par será hijos[2] y hijos[3]
     */
    private void elegirPares() {

        Collections.shuffle(hijos, random);
    }

    /*
     * Padre 1: [7, 4, 8, 1, 5, 3, 6, 2]
     * Padre 2: [8, 5, 7, 3, 2, 6, 4, 1]
     * Se elije la posición 4 y 6 del padre 1
     * Hijo 1: [8, 5, 3, 6, 7, 2, 4, 1]
     * Se elije la posición 4 y 6 del padre 2
     * Hijo 2: [7, 8, 1, 5, 3, 2, 6, 4]
     *
     * Funcion que dados un par de padres, nos devuelven un par de hijos
     */
    private void cruceGOX(Individuo individuo1, Individuo individuo2, int indice) {
        List<Integer> padre1 = individuo1.getPermutacion();
        List<Integer> padre2 = individuo2.getPermutacion();

        // Se eligen dos posiciones aleatorias
        int posicion1 = random.nextInt(individuo1.getPermutacion().size());
        int posicion2 = random.nextInt(individuo1.getPermutacion().size());
        while (posicion1 == posicion2) {
            posicion1 = random.nextInt(individuo1.getPermutacion().size());
        }

        // Si posicion1 > posicion2, se le da la vuelta
        if (posicion1 > posicion2) {
            int aux = posicion1;
            posicion1 = posicion2;
            posicion2 = aux;
        }


        // Tachamos las posiciones de padre2 donde estan los clientes entre padre1[posicion1] y padre1[posicion2]
        // Podemos guardar las posiciones en un array
        // Hacemos lo mismo con el padre1
        List<Integer> tachadosPadre2 = new ArrayList<>(posicion2-posicion1+1);
        List<Integer> tachadosPadre1 = new ArrayList<>(posicion2-posicion1 + 1);
        for (int i = posicion1; i <= posicion2; i++) {
            // En la primera posicion estará el primer cliente del gen
            tachadosPadre2.add(padre2.indexOf(padre1.get(i)));

            tachadosPadre1.add(padre1.indexOf(padre2.get(i)));
        }

        // Desde padre2[0] hasta padre2[tachadosPadre2.get(0)] se copia al primer hijo menos los que estén tachados
        List<Integer> hijo1 = new ArrayList<>();
        for (int i = 0; i < tachadosPadre2.get(0); i++) {
            if (!tachadosPadre2.contains(i)) {
                hijo1.add(padre2.get(i));
            }
        }
        // Copiamos el gen del padre1 seleccionado
        for (int i = posicion1; i <= posicion2; i++) {
            hijo1.add(padre1.get(i));
        }

        // Copiamos del padre2 todos los demas clientes sin tachar
        for (int i = tachadosPadre2.get(0) + 1; i < padre2.size(); i++) {
            if (!tachadosPadre2.contains(i)) {
                hijo1.add(padre2.get(i));
            }
        }

        // Ya tenemos al hijo1, ahora hay que hacer lo mismo con el hijo2 pero intercambiando los papeles de los padres
        List<Integer> hijo2 = new ArrayList<>();
        for (int i = 0; i < tachadosPadre1.get(0); i++) {
            if (!tachadosPadre1.contains(i)) {
                hijo2.add(padre1.get(i));
            }
        }
        for (int i = posicion1; i <= posicion2; i++) {
            hijo2.add(padre2.get(i));
        }

        for (int i = tachadosPadre1.get(0) + 1; i < padre1.size(); i++) {
            if (!tachadosPadre1.contains(i)) {
                hijo2.add(padre1.get(i));
            }
        }

        // Cambiamos los nuevos hijos que salieron del par de padres
        hijos.set(indice, new Individuo(hijo1));
        hijos.set(indice + 1, new Individuo(hijo2));

    }

    // Mutacion por intercambio. Se eligen dos posiciones al azar y se intercambia el cliente entre ellos
    private void mutacionSwap(Individuo hijo, int indice) {
        List<Integer> permHijo = hijo.getPermutacion();

        // Se eligen dos posiciones aleatorias
        int posicion1 = random.nextInt(permHijo.size());
        int posicion2 = random.nextInt(permHijo.size());
        while (posicion1 == posicion2) {
            posicion1 = random.nextInt(permHijo.size());
        }

        // Cambiamos el cliente de posicion1 por cliente de posicion2 y viceversa
        int aux = permHijo.get(posicion1);
        permHijo.set(posicion1, permHijo.get(posicion2));
        permHijo.set(posicion2, aux);

        hijos.set(indice, new Individuo(permHijo));

    }


}
