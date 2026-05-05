package implementations;

import io.CargadorArchivos;
import model.Input;
import model.Solucion;
import operators.Operador2Opt;
import operators.OperadorOrOpt;
import operators.OperadorSwap;
import utils.*;

import java.util.List;

public class ImplementacionSolomon {
    public void implementar(int semilla, int numPermutaciones) {
        CargadorArchivos.cargarDatos("c101.txt");

        Input input = Input.getInstancia();
        input.mostrarDatosCargados();

        Evaluador evaluadorSoluciones = new EvaluadorGenerico(input.getDistancias(), input);
        Operador2Opt operador2Opt = new Operador2Opt(evaluadorSoluciones);
        OperadorOrOpt operadorOrOpt = new OperadorOrOpt(evaluadorSoluciones);
        OperadorSwap operadorSwap = new OperadorSwap(evaluadorSoluciones);

        // Generamos 30 permutaciones distintas
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), semilla);
        List<List<Integer>> listaPermutaciones = generadorPermutaciones.listaDePermutaciones(numPermutaciones);
        List<Solucion> listaSoluciones = ((EvaluadorGenerico) evaluadorSoluciones).evaluarListaPermutaciones(listaPermutaciones);


        // Creamos un Experimentador
        Experimentador experimentador = new Experimentador();
        experimentador.ejecutarExperimentoSimple(operador2Opt, listaSoluciones);
        experimentador.ejecutarExperimentoSimple(operadorOrOpt, listaSoluciones);
        experimentador.ejecutarExperimentoSimple(operadorSwap, listaSoluciones);

    }

}
