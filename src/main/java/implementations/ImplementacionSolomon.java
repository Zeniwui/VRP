package implementations;

import model.Input;
import model.Solucion;
import operators.Operador2Opt;
import operators.OperadorOrOpt;
import operators.OperadorSwap;
import utils.*;

import java.util.List;

public class ImplementacionSolomon {
    public void implementar(int semilla, int numPermutaciones) {
        Input input = Input.getInstancia();
        input.cargarDatosSolomon("c101.txt");
        input.mostrarDatosCargadosSolomon();

        EvaluadorDistancias evaluadorSoluciones = new EvaluadorDistancias(input);
        Operador2Opt operador2Opt = new Operador2Opt(evaluadorSoluciones);
        OperadorOrOpt operadorOrOpt = new OperadorOrOpt(evaluadorSoluciones);
        OperadorSwap operadorSwap = new OperadorSwap(evaluadorSoluciones);

        // Generamos 30 permutaciones distintas
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), semilla);
        List<List<Integer>> listaPermutaciones = generadorPermutaciones.listaDePermutaciones(numPermutaciones);
        List<Solucion> listaSoluciones = evaluadorSoluciones.evaluarListaPermutaciones(listaPermutaciones);

        // Creamos un Experimentador
        Experimentador experimentador = new Experimentador();
        experimentador.ejecutarExperimento(operador2Opt, listaSoluciones);
        experimentador.ejecutarExperimento(operadorOrOpt, listaSoluciones);
        experimentador.ejecutarExperimento(operadorSwap, listaSoluciones);

    }
}
