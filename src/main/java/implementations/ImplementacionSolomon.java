package implementations;

import model.Input;
import model.Solucion;
import utils.EvaluadorTiempos;
import utils.GeneradorPermutacion;

import java.util.List;

public class ImplementacionSolomon {
    public void implementar(int semilla, int numPermutaciones) {
        Input input = Input.getInstancia();
        input.cargarDatosSolomon("c101.txt");
        input.mostrarDatosCargadosSolomon();

        // Generamos una permutacion aleatoria
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), semilla);
        List<Integer> permutacionInicial = generadorPermutaciones.aleatoria();
        System.out.println("Permutación aleatoria inicial: " + permutacionInicial);

    }
}
