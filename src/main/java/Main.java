import model.Input;
import model.Solucion;
import operators.Operador2Opt;
import operators.OperadorSwap;
import utils.Evaluador;
import utils.GeneradorPermutacion;

import java.util.List;

public class Main {
    private static final int SEMILLA = 2533;
    public static void main(String[] args) {

        // Cargamos los datos de entrada iniciales con los que trabajaremos
        Input input = Input.getInstancia();
        input.cargarDatos("input.txt");
        input.mostrarDatosCargados();

        // Generamos una permutacion aleatoria
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), SEMILLA);
        List<Integer> permutacionInicial = generadorPermutaciones.aleatoria();
        System.out.println("Permutación aleatoria inicial: " + permutacionInicial);

        // Evaluamos la permutacion aleatoria generada
        Evaluador evaluadorSoluciones = new Evaluador(input);
        Solucion solucionInicial = evaluadorSoluciones.evaluarCompleto(permutacionInicial);
        System.out.println("Solucion de la permutacion aleatoria inicial: " + solucionInicial);

        // Generamos la solucion optima aplicando el operador 2-opt
        System.out.println("---------------------------------------------- OPERADOR 2-OPT -----------------------------------------------");
        Operador2Opt operador2Opt = new Operador2Opt(evaluadorSoluciones);
        Solucion minimo2Opt = operador2Opt.generarMinimoLocal(solucionInicial);
        System.out.println("Solucion minimo 2-opt partiendo de la permutacion aleatoria inicial: " + minimo2Opt);

        //Generamos la solucion optima aplicando el operador swap
        System.out.println("---------------------------------------------- OPERADOR SWAP -----------------------------------------------");
        OperadorSwap operadorSwap = new OperadorSwap(evaluadorSoluciones);
        Solucion minimoSwap = operadorSwap.generarMinimoLocal(solucionInicial);
        System.out.println("Solucion minimo swap partiendo de la permutacion aleatoria inicial: " + minimoSwap);
    }
}
