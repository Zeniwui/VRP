package implementations;

import model.Input;
import model.Solucion;
import operators.Operador2Opt;
import utils.Evaluador;
import utils.EvaluadorDistancias;
import utils.EvaluadorTiempos;
import utils.GeneradorPermutacion;

import java.util.List;

public class ImplementacionSolomon {
    public void implementar(int semilla, int numPermutaciones) {
        Input input = Input.getInstancia();
        input.cargarDatosSolomon("c101.txt");
        input.mostrarDatosCargadosSolomon();

        // Generamos una permutacion aleatoria
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(5, semilla);
        List<Integer> permutacionInicial = generadorPermutaciones.aleatoria();
        System.out.println("Permutación aleatoria inicial: " + permutacionInicial);

        // Evaluamos la permutacion aleatoria generada
        Evaluador evaluadorDistancias = new EvaluadorDistancias(input);
        Solucion solucionInicial = evaluadorDistancias.evaluarCompleto(permutacionInicial);
        System.out.println("Solucion de la permutacion aleatoria inicial: " + solucionInicial);

        // Generamos la solucion optima aplicando el operador 2-opt
        System.out.println("---------------------------------------------- OPERADOR 2-OPT -----------------------------------------------");
        Operador2Opt operador2Opt = new Operador2Opt(evaluadorDistancias);
        Solucion minimo2Opt = operador2Opt.generarMinimoLocal(solucionInicial);
        System.out.println("Solucion minimo 2-opt partiendo de la permutacion aleatoria inicial: " + minimo2Opt);

    }
}
