package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExportadorEstadisticasCSV {
    private final List<String[]> filasDetalle = new ArrayList<>();
    private final List<Double> costos = new ArrayList<>();
    private String nombreArchivo;

    public void registrarDetalle(String permutacionInicial, String rutaFinal, double costo) {
        filasDetalle.add(new String[]{permutacionInicial, rutaFinal});
        costos.add(costo);
    }

    public void registrarSoloRuta(String rutaFinal, double costo) {
        filasDetalle.add(new String[]{"", rutaFinal});
        costos.add(costo);
    }

    public void setNombreArchivo(String nombre) {
        this.nombreArchivo = nombre;
    }

    public void exportar(String operador, String instancia, boolean conSplit, boolean conPermutacionInicial, boolean esMultiStart, double tiempoTotalMs) {
        if (filasDetalle.isEmpty()) {
            System.out.println("ExportadorEstadisticasCSV: no hay registros para exportar.");
            return;
        }

        double mejorCosto = Double.MAX_VALUE;
        double peorCosto = -Double.MAX_VALUE;
        String mejorRuta = "";
        String peorRuta = "";
        double sumaCostos = 0;
        int count = costos.size();

        for (int i = 0; i < count; i++) {
            double c = costos.get(i);
            String r = filasDetalle.get(i)[1];
            if (c < mejorCosto) { mejorCosto = c; mejorRuta = r; }
            if (c > peorCosto) { peorCosto = c; peorRuta = r; }
            sumaCostos += c;
        }

        double promedio = sumaCostos / count;
        double sumaCuadrados = 0;
        for (double c : costos) {
            double diff = c - promedio;
            sumaCuadrados += diff * diff;
        }
        double desviacionTipica = Math.sqrt(sumaCuadrados / count);

        if (nombreArchivo == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String opLimpio = operador.replace(" ", "_").replace(",", "");
            String instLimpia = instancia.replace(".vrp", "").replace(".txt", "");
            String modo = esMultiStart ? "MultiStart" : "Simple";
            nombreArchivo = "estadisticas_" + instLimpia + "_" + opLimpio + "_" + modo + "_" + (conSplit ? "conSplit" : "sinSplit") + "_" + timestamp + ".csv";
        }

        Path dir = Paths.get("src", "main", "resources", "resultados", "CMT5");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("Error al crear directorio de resultados: " + e.getMessage());
            return;
        }

        Path filePath = dir.resolve(nombreArchivo);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            if (conPermutacionInicial) {
                writer.write("permutacion_inicial,ruta_final");
            } else {
                writer.write("ruta_final");
            }
            writer.newLine();

            for (String[] fila : filasDetalle) {
                String rutaEscapada = "\"" + fila[1] + "\"";
                if (conPermutacionInicial) {
                    String permEscapada = "\"" + fila[0] + "\"";
                    writer.write(permEscapada + "," + rutaEscapada);
                } else {
                    writer.write(rutaEscapada);
                }
                writer.newLine();
            }

            writer.newLine();
            writer.write("ESTADÍSTICAS");
            writer.newLine();
            writer.write(String.format(Locale.US, "operador,%s", operador));
            writer.newLine();
            writer.write(String.format(Locale.US, "instancia,%s", instancia));
            writer.newLine();
            writer.write(String.format(Locale.US, "conSplit,%b", conSplit));
            writer.newLine();
            writer.write(String.format(Locale.US, "num_ejecuciones,%d", count));
            writer.newLine();
            writer.write(String.format(Locale.US, "tiempo_total_ms,%.4f", tiempoTotalMs));
            writer.newLine();
            writer.write(String.format(Locale.US, "mejor_costo,%.2f", mejorCosto));
            writer.newLine();
            writer.write(String.format(Locale.US, "mejor_ruta,\"%s\"", mejorRuta));
            writer.newLine();
            writer.write(String.format(Locale.US, "peor_costo,%.2f", peorCosto));
            writer.newLine();
            writer.write(String.format(Locale.US, "peor_ruta,\"%s\"", peorRuta));
            writer.newLine();
            writer.write(String.format(Locale.US, "promedio,%.2f", promedio));
            writer.newLine();
            writer.write(String.format(Locale.US, "desviacion_tipica,%.2f", desviacionTipica));
            writer.newLine();

            System.out.println("CSV estadísticas exportado: " + filePath.toAbsolutePath() + " (" + count + " ejecuciones)");
        } catch (IOException e) {
            System.err.println("Error al escribir CSV: " + e.getMessage());
        }
    }
}
