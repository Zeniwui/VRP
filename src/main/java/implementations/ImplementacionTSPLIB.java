package implementations;

import genetic.AlgoritmoGenetico;
import io.CargadorArchivos;
import metaheuristics.MultiStart;
import model.Input;
import operators.*;
import utils.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ImplementacionTSPLIB {
    public void implementar(String instancia, int semilla, int numPermutaciones) {

        CargadorArchivos.cargarDatos(instancia);

        Input input = Input.getInstancia();
        input.mostrarDatosCargados();

        Evaluador evaluadorSoluciones = new EvaluadorGenerico(input.getDistancias(), input);
        Operador2Opt operador2Opt = new Operador2Opt(evaluadorSoluciones);
        OperadorOrOpt operadorOrOpt = new OperadorOrOpt(evaluadorSoluciones);
        OperadorSwap operadorSwap = new OperadorSwap(evaluadorSoluciones);
        Split split = new Split(evaluadorSoluciones);
        Combinacion combinacion = new Combinacion(operador2Opt, operadorOrOpt, operadorSwap, evaluadorSoluciones);
        GeneradorPermutacion generadorPermutaciones = new GeneradorPermutacion(input.getDimension(), semilla);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

/*        // --- Experimento 1: GA solo split ---
        System.out.println("=== EXPERIMENTO 1: GA solo split ===");
        ExportadorCSV csv1 = new ExportadorCSV();
        csv1.setNombreArchivo("convergencia_" + instancia.replace(".vrp","").replace(".txt","")
                + "_GA_split_" + timestamp + ".csv");
        AlgoritmoGenetico ga1 = new AlgoritmoGenetico(generadorPermutaciones, split);
        ga1.setNumPoblacion(100);
        ga1.setIteracionesSinMejora(15);
        ga1.setSemilla(semilla);
        ga1.setBusquedaLocal(null, false);
        ga1.setExportadorCSV(csv1, instancia);
        ga1.ejecutar();*/

/*        // --- Experimento 2: GA + Combinacion ---
        System.out.println("=== EXPERIMENTO 2: GA + Combinacion ===");
        ExportadorCSV csv2 = new ExportadorCSV();
        csv2.setNombreArchivo("convergencia_" + instancia.replace(".vrp","").replace(".txt","")
                + "_GA_Combinacion_" + timestamp + ".csv");
        AlgoritmoGenetico ga2 = new AlgoritmoGenetico(generadorPermutaciones, split);
        ga2.setNumPoblacion(100);
        ga2.setIteracionesSinMejora(15);
        ga2.setSemilla(semilla);
        ga2.setBusquedaLocal(combinacion, true);
        ga2.setExportadorCSV(csv2, instancia);
        ga2.ejecutar();*/

/*        // --- Experimento 3: Multi-start (100 iteraciones) + Combinacion ---
        System.out.println("=== EXPERIMENTO 3: MultiStart + Combinacion ===");
        ExportadorCSV csv3 = new ExportadorCSV();
        csv3.setNombreArchivo("convergencia_" + instancia.replace(".vrp","").replace(".txt","")
                + "_MultiStart_Combinacion_" + timestamp + ".csv");
        MultiStart multiStart = new MultiStart(combinacion, split, evaluadorSoluciones, generadorPermutaciones);
        multiStart.setExportadorCSV(csv3, instancia);
        multiStart.generarMejorSolucion(100, true);*/

/*        // --- Experimento 4: GA + Swap ---
        System.out.println("=== EXPERIMENTO 4: GA + Swap ===");
        ExportadorCSV csv2 = new ExportadorCSV();
        csv2.setNombreArchivo("convergencia_" + instancia.replace(".vrp","").replace(".txt","")
                + "_GA_Combinacion_" + timestamp + ".csv");
        AlgoritmoGenetico ga2 = new AlgoritmoGenetico(generadorPermutaciones, split);
        ga2.setNumPoblacion(100);
        ga2.setIteracionesSinMejora(15);
        ga2.setSemilla(semilla);
        ga2.setBusquedaLocal(operadorSwap, true);
        ga2.setExportadorCSV(csv2, instancia);
        ga2.ejecutar();*/

        // --- Experimento 5: GA + Combinacion (30 repeticiones) ---
        System.out.println("=== EXPERIMENTO 5: GA + Combinacion (30 repeticiones) ===");
        Experimentador experimentador = new Experimentador();
        experimentador.ejecutarExperimentoGeneticoConCSV(
                generadorPermutaciones,
                split,
                evaluadorSoluciones,
                combinacion,
                30,
                100,
                15,
                instancia);
    }
}