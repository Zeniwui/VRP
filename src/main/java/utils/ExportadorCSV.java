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

public class ExportadorCSV {
    private final List<RegistroCSV> registros = new ArrayList<>();
    private String nombreArchivo;

    public void registrar(int iteracion, double tiempoMs, double coste, String operador, String instancia, String rutaActual, boolean conSplit) {
        registros.add(new RegistroCSV(iteracion, tiempoMs, coste, 0.0, "", 0.0, "", 0.0, 0, "", 0.0, operador, instancia, rutaActual, conSplit));
    }

    public void registrar(int iteracion, double tiempoMs, double coste, double promedioPadres, String operador, String instancia, String rutaActual, boolean conSplit) {
        registros.add(new RegistroCSV(iteracion, tiempoMs, coste, promedioPadres, "", 0.0, "", 0.0, 0, "", 0.0, operador, instancia, rutaActual, conSplit));
    }

    public void registrar(int iteracion, double tiempoMs, double coste, double promedioPadres,
                          String tipoCruce, double probCruce, String tipoMutacion, double probMutacion,
                          int iteracionesSinMejora, String estrategiaBL, double porcentajeBL,
                          String operador, String instancia, String rutaActual, boolean conSplit) {
        registros.add(new RegistroCSV(iteracion, tiempoMs, coste, promedioPadres,
                tipoCruce, probCruce, tipoMutacion, probMutacion,
                iteracionesSinMejora, estrategiaBL, porcentajeBL,
                operador, instancia, rutaActual, conSplit));
    }

    public void setNombreArchivo(String nombre) {
        this.nombreArchivo = nombre;
    }

    public void exportar() {
        if (registros.isEmpty()) {
            System.out.println("ExportadorCSV: no hay registros para exportar.");
            return;
        }

        if (nombreArchivo == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String operador = registros.get(0).getOperador().replace(" ", "_");
            String instancia = registros.get(0).getInstancia().replace(".vrp", "").replace(".txt", "");
            nombreArchivo = "resultados_" + instancia + "_" + operador + "_" + timestamp + ".csv";
        }

        Path dir = Paths.get("resultados");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            System.err.println("Error al crear directorio de resultados: " + e.getMessage());
            return;
        }

        Path filePath = dir.resolve(nombreArchivo);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("iteracion,tiempo_us,coste,promedio_padres,cruce,prob_cruce,mutacion,prob_mutacion,iteraciones_sin_mejora,estrategia_bl,porcentaje_bl,operador,instancia,ruta_actual,conSplit");
            writer.newLine();
            for (RegistroCSV r : registros) {
                String rutaEscapada = "\"" + r.getRutaActual() + "\"";
                writer.write(String.format(Locale.US, "%d,%.0f,%.0f,%.0f,%s,%.2f,%s,%.2f,%d,%s,%.2f,%s,%s,%s,%b",
                    r.getIteracion(),
                    r.getTiempoUs(),
                    r.getCoste(),
                    r.getPromedioPadres(),
                    r.getTipoCruce(),
                    r.getProbCruce(),
                    r.getTipoMutacion(),
                    r.getProbMutacion(),
                    r.getIteracionesSinMejora(),
                    r.getEstrategiaBL(),
                    r.getPorcentajeBL(),
                    r.getOperador(),
                    r.getInstancia(),
                    rutaEscapada,
                    r.isConSplit()));
                writer.newLine();
            }
            writer.newLine();
            System.out.println("CSV exportado: " + filePath.toAbsolutePath() + " (" + registros.size() + " registros)");
        } catch (IOException e) {
            System.err.println("Error al escribir CSV: " + e.getMessage());
        }
    }
}
