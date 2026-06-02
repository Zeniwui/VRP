package io;

import io.parsers.LectorFormato;
import io.parsers.LectorFormatoMio;
import io.parsers.LectorFormatoSolomon;
import io.parsers.LectorFormatoTSPLIB;
import model.Input;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CargadorArchivos {
    private static final List<LectorFormato> lectores = Arrays.asList(
            new LectorFormatoMio(),
            new LectorFormatoSolomon(),
            new LectorFormatoTSPLIB()
    );

    public static void cargarDatos(String nombreFichero) {
        try {
            List<String> lineas;
            Path path = Paths.get(nombreFichero);
            if (Files.exists(path)) {
                lineas = Files.readAllLines(path);
            } else {
                try (InputStream is = CargadorArchivos.class.getClassLoader().getResourceAsStream(nombreFichero)) {
                    if (is == null) {
                        System.err.println("ERROR: No se encontró el archivo: " + nombreFichero);
                        return;
                    }
                    lineas = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.toList());
                }
            }

            Input input = Input.getInstancia();

            // Averiguamos el formato del archivo
            boolean formatoEncontrado = false;
            for (LectorFormato lector : lectores) {
                if (lector.soportaFormato(lineas)) {
                    lector.parsear(lineas, input);
                    formatoEncontrado = true;
                    System.out.println("Archivo cargado exitosamente usando: " + lector.getClass().getSimpleName());
                    break;
                }
            }

            if (!formatoEncontrado) {
                System.err.println("ERROR: No se encontró ningún lector compatible con la estructura de este archivo.");
            }

        } catch (Exception e) {
            System.err.println("ERROR al leer el fichero: " + e.getMessage());
        }
    }
}