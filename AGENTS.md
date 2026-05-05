# VRP Project Architecture Map

## Problem
Capacitated Vehicle Routing Problem (CVRP) solver using local search algorithms.

## Tech Stack
- Java 17, Maven
- Java 17, Maven

## Directory Structure

```
src/main/java/
├── Main.java                          -- Entry point (seed=2533, permutations=30)
├── model/
│   ├── Input.java                     -- Singleton: holds dimension, capacity, demands, coords, time/distance matrices
│   └── Solucion.java                  -- Solution: List<List<Integer>> routes + double cost
├── io/
│   ├── CargadorArchivos.java          -- Auto-detects file format, delegates to appropriate parser
│   └── parsers/
│       ├── LectorFormato.java         -- Interface: soportaFormato(), parsear()
│       ├── LectorFormatoMio.java      -- Custom format (DIMENSION/CAPACIDAD headers)
│       ├── LectorFormatoSolomon.java  -- Solomon benchmark format (CUST NO. section)
│       └── LectorFormatoTSPLIB.java   -- TSPLIB format (NODE_COORD_SECTION, DEMAND_SECTION)
├── operators/
│   ├── OperadorLocal.java             -- Interface: generarMinimoLocal(), generarMinimoTodosSegmentos(), getNombre()
│   ├── Operador2Opt.java              -- Reverses route segments to remove crossings
│   ├── OperadorOrOpt.java             -- Relocates chains of 2 consecutive nodes
│   ├── OperadorSwap.java              -- Swaps nodes between different routes (inter-route)
│   └── OperadorSplit.java             -- INCOMPLETE: does not compile
├── utils/
│   ├── Evaluador.java                 -- Interface: evaluarRutaCompleta(), evaluarSegmento(), suficienteCapacidadParaCubrirSegmento(), evaluarCompleto()
│   ├── EvaluadorDistancias.java       -- Evaluates based on distance matrix
│   ├── EvaluadorTiempos.java          -- Evaluates based on time matrix (structurally identical to EvaluadorDistancias)
│   ├── GeneradorPermutacion.java      -- Generates random unique permutations of customer nodes
│   ├── Experimentador.java            -- Runs experiments: simple operator pass or Multi-Start loop
│   └── Estadisticas.java             -- Computes best/worst/mean/stddev from a list of solutions
├── metaheuristics/
│   └── MultiStart.java                -- Multi-Start: generates random solutions, applies local search, keeps best
└── implementations/
    ├── Implementation1.java           -- Test runner for custom format
    ├── ImplementacionSolomon.java     -- Test runner for Solomon format
    └── ImplementacionTSPLIB.java      -- Test runner for TSPLIB format (experiment code commented out)
```

## Design Patterns
- **Singleton**: `Input` (single global instance of problem data)
- **Strategy**: `LectorFormato`, `Evaluador`, `OperadorLocal` (interchangeable implementations)
- **Chain of Responsibility**: `CargadorArchivos` iterates parsers until one accepts
- **Facade**: Implementation classes orchestrate the full pipeline

## Typical Execution Flow
1. `CargadorArchivos.cargarDatos(filename)` → detects format, populates `Input` singleton
2. `GeneradorPermutacion.listaDePermutaciones(n)` → generates n random permutations
3. `Evaluador.evaluarCompleto(permutation)` → splits permutation into feasible routes respecting capacity, returns `Solucion`
4. `OperadorLocal.generarMinimoTodosSegmentos(solution)` → applies local search, returns improved `Solucion`
5. `Estadisticas.calcularBasico()` → prints best/worst/mean/stddev

## Known Issues
- `OperadorSplit.java` is incomplete and does not compile (syntax errors, missing return)
- `OperadorSwap.generarMinimoLocal()` is a stub (returns `new Solucion(null, 3)`)
- `ImplementacionTSPLIB.implementar()` has all experiment code commented out
- `EvaluadorDistancias` and `EvaluadorTiempos` are near-duplicates
- `Main.java` has unused import `java.awt.image.BaseMultiResolutionImage`
- No test files in `src/test`

## Planned Refactorings
- Unify `EvaluadorDistancias`/`EvaluadorTiempos` into a generic evaluator
- Add `costoEntre(int i, int j)` to `Evaluador` interface to expose per-node-pair cost without leaking matrix implementation
- Complete `OperadorSplit` implementation

## Resource Files
- `resources/c101.txt` through `c105.txt`, `c201.txt` (Solomon instances)
- `resources/CMT1.vrp` through `CMT5.vrp` (TSPLIB instances)
- `resources/input.txt` (custom format)
