package utils;

import model.Solucion;

import java.util.List;

public class Estadisticas {
    List<Solucion> datos;

    public Estadisticas(List<Solucion> datos) {
        this.datos = datos;
    }

    public void calcularBasico() {
        double promedio, diferencia, desviacionTipica;

        Solucion mejor = datos.get(0);
        Solucion peor = datos.get(0);
        int totalTiempo = 0;
        double sumaCuadrados = 0.0;

        for (Solucion dato: datos) {
            if (dato.getCosto() < mejor.getCosto()) {
                mejor = dato;
            }
            if (dato.getCosto() > peor.getCosto()) {
                peor = dato;
            }
            totalTiempo += dato.getCosto();
        }
        promedio = (double) totalTiempo / datos.size();

        sumaCuadrados = 0.0;

        for (Solucion dato : datos) {
            diferencia = dato.getCosto() - promedio;
            sumaCuadrados += diferencia * diferencia;
        }

        desviacionTipica = Math.sqrt(sumaCuadrados / datos.size());

        System.out.println("==================================================");
        System.out.println("            ESTADÍSTICAS           ");
        System.out.println("Mejor: " + mejor);
        System.out.println("Peor: " + peor);
        System.out.println("Promedio: " + promedio);
        System.out.println("Desviacion tipica: " + desviacionTipica);
        System.out.println("==================================================");

    }




}
