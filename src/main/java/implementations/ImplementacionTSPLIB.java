package implementations;

import io.CargadorArchivos;
import model.Input;
import model.Solucion;
import operators.Operador2Opt;
import operators.OperadorOrOpt;
import operators.OperadorSwap;
import utils.EvaluadorDistancias;
import utils.Experimentador;
import utils.GeneradorPermutacion;

import java.util.List;

public class ImplementacionTSPLIB {
    public void implementar(int semilla, int numPermutaciones) {
        CargadorArchivos.cargarDatos("CMT1.vrp");

        Input input = Input.getInstancia();
        input.mostrarDatosCargados();

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
