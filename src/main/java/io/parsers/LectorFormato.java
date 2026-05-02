package io.parsers;

import model.Input;

import java.util.List;

public interface LectorFormato {
    // Devuelve 'true' si el lector reconoce el formato del archivo mirando las primeras líneas
    boolean soportaFormato(List<String> lineas);

    // Ejecuta la lógica específica de parseo y guarda los datos en la instancia 'Input'
    void parsear(List<String> lineas, Input input);
}