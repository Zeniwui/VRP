# Optimizador CVRP - Algoritmos de Búsqueda Local

## Descripción
Este proyecto implementa un conjunto de algoritmos basados en Búsqueda Local para resolver el **Problema de Enrutamiento de Vehículos con Restricciones de Capacidad (CVRP)**.

El proyecto se desarrolla como parte de las investigaciones en algoritmos de optimización y planificación inteligente en la Escuela Politécnica de Ingeniería (EPI) de Gijón. Actualmente, el software es capaz de generar soluciones iniciales mediante permutaciones aleatorias y optimizarlas iterativamente utilizando diferentes operadores de vecindario, evaluando su rendimiento tanto en tiempos de ejecución como en calidad de la solución (coste/distancia).

> **Estado del Proyecto:** 🚧 En desarrollo activo.

## Características Principales

* **Lectura de Instancias:** 
  * Soporte para carga de datos personalizados (`Input.cargarDatosMios`).
  * Soporte para el formato estándar de la literatura científica: **Instancias de Solomon** (`Input.cargarDatosSolomon`), calculando automáticamente las distancias euclídeas.
* **Operadores de Búsqueda Local:**
    * **2-Opt:** Invierte segmentos de la ruta para deshacer cruces ineficientes.
    * **Or-Opt:** Reubica cadenas consecutivas de clientes en nuevas posiciones.
    * **Swap:** Intercambia pares de nodos entre diferentes segmentos verificando la factibilidad de la capacidad.
* **Módulo de Experimentación:** Ejecuta lotes de pruebas sobre múltiples permutaciones iniciales y extrae estadísticas básicas (mejor solución, peor solución, promedio y desviación típica).

## Estructura del Proyecto

La arquitectura del código está modularizada de la siguiente manera:

* `/model`: Clases de dominio que representan el problema. Incluye el manejo de las entradas (`Input.java` usando el patrón Singleton) y la estructura de la `Solucion`.
* `/operators`: Lógica de los algoritmos de optimización. Contiene los operadores de vecindario (`Operador2Opt`, `OperadorOrOpt`, `OperadorSwap`) que implementan la interfaz `OperadorLocal`.
* `/utils`: Herramientas transversales. Incluye los generadores de permutaciones, evaluadores de distancias/tiempos y el motor estadístico (`Experimentador`, `Estadisticas`).
* `/implementations`: Puntos de entrada para ejecutar las distintas pruebas (ej. `ImplementacionSolomon.java`, `Implementation1.java`).

## Requisitos

* **Java:** Versión 17.
* Instancias de prueba (archivos `.txt` como `c201.txt`) ubicadas en el directorio `resources/`.

## Uso

Para ejecutar un experimento, puedes instanciar alguna de las clases dentro del paquete `implementations` y llamar al método `implementar(semilla, numero_de_permutaciones)`.

Ejemplo de flujo de ejecución básico:
1. Carga de datos mediante la clase `Input`.
2. Generación de múltiples permutaciones iniciales usando `GeneradorPermutacion`.
3. Evaluación inicial mediante `EvaluadorTiempos` o `EvaluadorDistancias`.
4. Pase de las soluciones al `Experimentador` junto con el operador deseado (ej. `Operador2Opt`).

## Autores y Agradecimientos

* Desarrollado por Xen Panera - Estudiante de Ingeniería Informática, EPI Gijón.
* Parte del Grupo de Investigación en Optimización y Scheduling Inteligentes iScOp.