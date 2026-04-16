package implementations;

import model.Input;
import model.Solucion;
import operators.Operador2Opt;
import operators.OperadorOrOpt;
import operators.OperadorSwap;
import utils.EvaluadorTiempos;
import utils.Experimentador;
import utils.GeneradorPermutacion;

import java.util.List;

public class Implementation1 {

    public void implementar(int semilla, int numPermutaciones) {

        // Cargamos los datos de entrada iniciales con los que trabajaremos
        Input input = Input.getInstancia();
        input.cargarDatosMios("input.txt");
        input.mostrarDatosCargados();

        // Generamos una permutacion aleatoria
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), semilla);
        List<Integer> permutacionInicial = generadorPermutaciones.aleatoria();
        System.out.println("Permutación aleatoria inicial: " + permutacionInicial);

        // Evaluamos la permutacion aleatoria generada
        EvaluadorTiempos evaluadorSoluciones = new EvaluadorTiempos(input);
        Solucion solucionInicial = evaluadorSoluciones.evaluarCompleto(permutacionInicial);
        System.out.println("Solucion de la permutacion aleatoria inicial: " + solucionInicial);

        // Generamos la solucion optima aplicando el operador 2-opt
        System.out.println("---------------------------------------------- OPERADOR 2-OPT -----------------------------------------------");
        Operador2Opt operador2Opt = new Operador2Opt(evaluadorSoluciones);
        Solucion minimo2Opt = operador2Opt.generarMinimoLocal(solucionInicial);
        System.out.println("Solucion minimo 2-opt partiendo de la permutacion aleatoria inicial: " + minimo2Opt);

        System.out.println("---------------------------------------------- OPERADOR OR-OPT -----------------------------------------------");
        OperadorOrOpt operadorOrOpt = new OperadorOrOpt(evaluadorSoluciones);
        Solucion minimoOrOpt = operadorOrOpt.generarMinimoLocal(solucionInicial);
        System.out.println("Solucion minimo OR-opt partiendo de la permutacion aleatoria inicial: " + minimoOrOpt);

        //Generamos la solucion optima aplicando el operador swap
        System.out.println("---------------------------------------------- OPERADOR SWAP -----------------------------------------------");
        OperadorSwap operadorSwap = new OperadorSwap(evaluadorSoluciones);
        Solucion minimoSwap = operadorSwap.generarMinimoLocal(solucionInicial);
        System.out.println("Solucion minimo swap partiendo de la permutacion aleatoria inicial: " + minimoSwap);

        // Generamos 30 permutaciones distintas
        List<List<Integer>> listaPermutaciones = generadorPermutaciones.listaDePermutaciones(numPermutaciones);
        List<Solucion> listaSoluciones = evaluadorSoluciones.evaluarListaPermutaciones(listaPermutaciones);

        // Creamos un Experimentador
        Experimentador experimentador = new Experimentador();
        experimentador.ejecutarExperimento(operador2Opt, listaSoluciones);
        experimentador.ejecutarExperimento(operadorOrOpt, listaSoluciones);
        experimentador.ejecutarExperimento(operadorSwap, listaSoluciones);
    }
}
