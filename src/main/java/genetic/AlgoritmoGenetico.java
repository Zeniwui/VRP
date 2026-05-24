package genetic;

import model.Solucion;
import operators.Operador2Opt;
import operators.OperadorOrOpt;
import operators.Split;
import operators.OperadorSwap;
import utils.EvaluadorGenerico;
import utils.GeneradorPermutacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    private EvaluadorGenerico evaluador;

    private int numPoblacion = 100;
    private int semillaPares = 2533;
    private double probCruce = 1;
    private double probMutacion = 0.05;

    public AlgoritmoGenetico(GeneradorPermutacion generador, Split operadorSplit, EvaluadorGenerico evaluador) {
        this.generador = generador;
        this.split = operadorSplit;
        this.evaluador = evaluador;
    }

    public void ejecutar() {
        generarPoblacionInicial(numPoblacion);
        hijos = new ArrayList<>(List.copyOf(padres));
        int contadorSinMejora = 0;

        // Comenzamos bucle
        while (contadorSinMejora < 15) {

            // Elegimos pares
            elegirPares();

            // Hacemos los cruces entre los pares

        }
    }

    /*
     * Generamos la poblacion inicial con el numero de individuos que le pasamos como parametro
     */
    private void generarPoblacionInicial(int numIndividuos) {
        // Generamos lista de permutaciones aleatoria
        List<List<Integer>> listaPermutaciones =  generador.listaDePermutaciones(numIndividuos);
        padres = new ArrayList<>(numIndividuos);

        // Evaluamos cada permutacion utilizando primero split y despues generando su minimo local mediante combinacion
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
        Random random = new Random(semillaPares);

        Collections.shuffle(hijos, random);
    }

    /*
     * Padre 1: [7, 4, 8, 1, 5, 3, 6, 2]
     * Padre 2: [8, 5, 7, 3, 2, 6, 4, 1]
     * Se elije la posición 4 del padre 1
     * Hijo 1: [8, 5, 3, 6, 7, 2, 4, 1]
     * Se elije la posición 4 del padre 2
     * Hijo 2: [7, 8, 1, 5, 3, 2, 6, 4]
     */
    private void cruceGOX() {

    }
}
