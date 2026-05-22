package implementations;

import io.CargadorArchivos;
import model.Input;
import operators.*;
import utils.Evaluador;
import utils.EvaluadorGenerico;
import utils.Experimentador;
import utils.GeneradorPermutacion;

import java.util.List;

public class ImplementacionTSPLIB {
    public void implementar(int semilla, int numPermutaciones) {
        String nombreInstancia = "CMT5.vrp";
        CargadorArchivos.cargarDatos(nombreInstancia);

        Input input = Input.getInstancia();
        input.mostrarDatosCargados();

        Evaluador evaluadorSoluciones = new EvaluadorGenerico(input.getDistancias(), input);
        Operador2Opt operador2Opt = new Operador2Opt(evaluadorSoluciones);
        OperadorOrOpt operadorOrOpt = new OperadorOrOpt(evaluadorSoluciones);
        OperadorSwap operadorSwap = new OperadorSwap(evaluadorSoluciones);
        OperadorSplit split = new OperadorSplit(evaluadorSoluciones);
        Combinacion combinacion = new Combinacion(operador2Opt, operadorOrOpt, operadorSwap, evaluadorSoluciones);
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), semilla);
        Experimentador experimentador = new Experimentador();


/*        List<List<Integer>> listaDeRutas = Arrays.asList(
                Arrays.asList(92, 37, 98, 100, 91, 16, 86, 38, 44, 14, 42, 43, 15, 57, 2, 58),
                Arrays.asList(6, 96, 99, 59, 93, 85, 61, 17, 45, 84, 5, 60, 89),
                Arrays.asList(27, 69, 1, 70, 30, 20, 66, 32, 90, 63, 10, 62, 88, 31),
                Arrays.asList(21, 72, 75, 56, 39, 67, 23, 41, 22, 74, 73, 40),
                Arrays.asList(18, 83, 8, 46, 47, 36, 49, 64, 11, 19, 48, 82, 7, 52),
                Arrays.asList(94, 95, 97, 87, 13),
                Arrays.asList(28, 12, 80, 68, 29, 24, 54, 55, 25, 4, 26, 53),
                Arrays.asList(76, 77, 3, 79, 78, 34, 35, 65, 71, 9, 51, 81, 33, 50)
        );

        Solucion solucionInicial = new Solucion(listaDeRutas, evaluadorSoluciones.evaluarRutaCompleta(listaDeRutas));
        System.out.println("Solucion de la permutacion aleatoria inicial: " + solucionInicial);

        System.out.println("---------------------------------------------- OPERADOR 2-OPT -----------------------------------------------");
        Solucion minimo2Opt = operador2Opt.generarMinimoTodosSegmentos(solucionInicial);
        System.out.println("Solucion minimo 2-opt partiendo de la permutacion aleatoria inicial: " + minimo2Opt);

        System.out.println("---------------------------------------------- OPERADOR OR-OPT -----------------------------------------------");
        Solucion minimoOrOpt = operadorOrOpt.generarMinimoLocal(solucionInicial);
        System.out.println("Solucion minimo OR-opt partiendo de la permutacion aleatoria inicial: " + minimoOrOpt);

        //Generamos la solucion optima aplicando el operador swap
        System.out.println("---------------------------------------------- OPERADOR SWAP -----------------------------------------------");
        Solucion minimoSwap = operadorSwap.generarMinimoTodosSegmentos(solucionInicial);
        System.out.println("Solucion minimo swap partiendo de la permutacion aleatoria inicial: " + minimoSwap);*/



        // Generamos 30 permutaciones distintas
        List<List<Integer>> listaPermutaciones = generadorPermutaciones.listaDePermutaciones(numPermutaciones);

        boolean conSplit = true;

        // Experimentos con estadísticas CSV para lista de permutaciones
/*        experimentador.ejecutarExperimentoSimpleConCSV(operador2Opt, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarExperimentoSimpleConCSV(operadorOrOpt, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarExperimentoSimpleConCSV(operadorSwap, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarExperimentoSimpleConCSV(combinacion, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);

        // Multi-Start con estadísticas CSV
        experimentador.ejecutarMultiStartConCSV(operador2Opt, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarMultiStartConCSV(operadorOrOpt, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarMultiStartConCSV(operadorSwap, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarMultiStartConCSV(combinacion, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);

        conSplit = false;
        // Experimentos con estadísticas CSV para lista de permutaciones
        experimentador.ejecutarExperimentoSimpleConCSV(operador2Opt, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarExperimentoSimpleConCSV(operadorOrOpt, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarExperimentoSimpleConCSV(operadorSwap, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarExperimentoSimpleConCSV(combinacion, split, evaluadorSoluciones, listaPermutaciones, conSplit, nombreInstancia);

        // Multi-Start con estadísticas CSV
        experimentador.ejecutarMultiStartConCSV(operador2Opt, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarMultiStartConCSV(operadorOrOpt, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarMultiStartConCSV(operadorSwap, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);
        experimentador.ejecutarMultiStartConCSV(combinacion, split, 30, evaluadorSoluciones, generadorPermutaciones, conSplit, nombreInstancia);*/

        // Experimento con CSV para una sola permutación (evolución iteración a iteración)
        List<Integer> permutacionIndividual = generadorPermutaciones.aleatoria();
        experimentador.ejecutarExperimentoConCSV(operador2Opt, split, evaluadorSoluciones, permutacionIndividual, nombreInstancia);
        experimentador.ejecutarExperimentoConCSV(operadorOrOpt, split, evaluadorSoluciones, permutacionIndividual, nombreInstancia);
        experimentador.ejecutarExperimentoConCSV(operadorSwap, split, evaluadorSoluciones, permutacionIndividual, nombreInstancia);
        experimentador.ejecutarExperimentoConCSV(combinacion, split, evaluadorSoluciones, permutacionIndividual, nombreInstancia);

    }
}