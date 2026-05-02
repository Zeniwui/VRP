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
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), semilla);
        Experimentador experimentador = new Experimentador();

/*        // Generamos 30 permutaciones distintas
        List<List<Integer>> listaPermutaciones = generadorPermutaciones.listaDePermutaciones(numPermutaciones);
        List<Solucion> listaSoluciones = evaluadorSoluciones.evaluarListaPermutaciones(listaPermutaciones);

        // Creamos un Experimentador
        experimentador.ejecutarExperimentoSimple(operador2Opt, listaSoluciones);
        experimentador.ejecutarExperimentoSimple(operadorOrOpt, listaSoluciones);
        experimentador.ejecutarExperimentoSimple(operadorSwap, listaSoluciones);*/

        experimentador.ejecutarMultiStart(operador2Opt, 30, evaluadorSoluciones, generadorPermutaciones);
        experimentador.ejecutarMultiStart(operadorOrOpt, 30, evaluadorSoluciones, generadorPermutaciones);
        experimentador.ejecutarMultiStart(operadorSwap, 30, evaluadorSoluciones, generadorPermutaciones);
    }
}
