import csv
import os
import re
import sys
from collections import defaultdict
from pathlib import Path


def parse_estadisticas_csv(filepath):
    """Parsea la sección ESTADÍSTICAS de un CSV generado por ExportadorEstadisticasCSV."""
    stats = {}
    in_stats = False
    with open(filepath, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if line == "ESTADÍSTICAS":
                in_stats = True
                continue
            if not in_stats:
                continue
            if "," in line:
                key, value = line.split(",", 1)
                key = key.strip()
                value = value.strip().strip('"')
                stats[key] = value
    return stats


def parse_filename(filename):
    """
    Extrae operador, modo (Simple/MultiStart) y split (conSplit/sinSplit) del nombre del archivo.
    Patrón: estadisticas_{instancia}_{operador}_{modo}_{split}_{timestamp}.csv
    """
    basename = os.path.basename(filename)
    match = re.match(
        r"estadisticas_([^_]+)_(.+)_(Simple|MultiStart)_(conSplit|sinSplit)_\d{8}_\d{6}\.csv",
        basename,
    )
    if not match:
        return None
    instancia, operador, modo, split = match.groups()
    return {
        "instancia": instancia,
        "operador": operador,
        "modo": modo,
        "split": split,
    }


def collect_stats_for_instance(instance_dir):
    """
    Lee todos los CSV de estadísticas en un directorio de instancia
    y retorna un dict: operador -> {modo}_{split} -> stats
    """
    operadores = defaultdict(dict)

    for filename in os.listdir(instance_dir):
        if not filename.startswith("estadisticas_") or not filename.endswith(".csv"):
            continue

        filepath = os.path.join(instance_dir, filename)
        parsed = parse_filename(filename)
        if parsed is None:
            continue

        stats = parse_estadisticas_csv(filepath)
        if not stats:
            continue

        operador = parsed["operador"]
        modo = parsed["modo"]
        split = parsed["split"]
        key = f"{modo}_{split}"

        operadores[operador][key] = stats

    return operadores


def build_consolidated_row(operador, stats_dict):
    """
    Construye una fila para el CSV consolidado.
    Columnas: Operador, Simple_Tiempo, Simple_Promedio, Simple_Desviacion, Simple_Mejor, Simple_Peor,
              Simple_Tiempo_conSplit, Simple_Promedio_conSplit, Simple_Desviacion_conSplit, Simple_Mejor_conSplit, Simple_Peor_conSplit,
              MultiStart_Tiempo, MultiStart_Promedio, MultiStart_Desviacion, MultiStart_Mejor, MultiStart_Peor,
              MultiStart_Tiempo_conSplit, MultiStart_Promedio_conSplit, MultiStart_Desviacion_conSplit, MultiStart_Mejor_conSplit, MultiStart_Peor_conSplit
    """
    row = {"Operador": operador}

    for modo in ["Simple", "MultiStart"]:
        for split in ["sinSplit", "conSplit"]:
            key = f"{modo}_{split}"
            stats = stats_dict.get(key, {})

            suffix = f"_{modo}"
            if split == "conSplit":
                suffix += "_conSplit"

            row[f"Tiempo{suffix}"] = stats.get("tiempo_total_ms", "")
            row[f"Promedio{suffix}"] = stats.get("promedio", "")
            row[f"Desviacion{suffix}"] = stats.get("desviacion_tipica", "")
            row[f"Mejor{suffix}"] = stats.get("mejor_costo", "")
            row[f"Peor{suffix}"] = stats.get("peor_costo", "")

    return row


def get_column_order():
    """Retorna el orden de columnas para el CSV consolidado."""
    columns = ["Operador"]

    for modo in ["Simple", "MultiStart"]:
        for split in ["sinSplit", "conSplit"]:
            suffix = f"_{modo}"
            if split == "conSplit":
                suffix += "_conSplit"

            columns.extend(
                [
                    f"Tiempo{suffix}",
                    f"Promedio{suffix}",
                    f"Desviacion{suffix}",
                    f"Mejor{suffix}",
                    f"Peor{suffix}",
                ]
            )

    return columns


OPERATOR_ORDER = ["2-opt", "OR-opt", "Swap", "Combinacion_2-opt_OR-opt_y_Swap"]


def _normalize_operator(op):
    return op.replace(" ", "_").replace(",", "")


def _sort_operators(operadores):
    normalized = { _normalize_operator(k): v for k, v in operadores.items() }
    result = []
    for target in OPERATOR_ORDER:
        for key, value in normalized.items():
            if key == target:
                result.append((key, value))
                break
    for key, value in normalized.items():
        if key not in OPERATOR_ORDER:
            result.append((key, value))
    return result


def process_instance(instance_name, resultados_dir, output_dir):
    """Procesa una instancia y genera el CSV consolidado."""
    instance_dir = os.path.join(resultados_dir, instance_name)
    if not os.path.isdir(instance_dir):
        print(f"  [SKIP] Directorio no encontrado: {instance_dir}")
        return None

    operadores = collect_stats_for_instance(instance_dir)
    if not operadores:
        print(f"  [SKIP] No se encontraron CSV de estadísticas en: {instance_dir}")
        return None

    column_order = get_column_order()
    output_file = os.path.join(output_dir, f"consolidado_{instance_name}.csv")

    with open(output_file, "w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=column_order)
        writer.writeheader()

        for operador, stats_dict in _sort_operators(operadores):
            row = build_consolidated_row(operador, stats_dict)
            writer.writerow(row)

    print(f"  [OK] {output_file} ({len(operadores)} operadores)")
    return output_file


def main():
    base_dir = Path(__file__).resolve().parent.parent
    resultados_dir = base_dir / "src" / "main" / "resources" / "resultados"
    output_dir = base_dir / "scripts" / "output"
    output_dir.mkdir(parents=True, exist_ok=True)

    if len(sys.argv) > 1:
        instances = [sys.argv[1]]
    else:
        if not resultados_dir.is_dir():
            print(f"Error: Directorio no encontrado: {resultados_dir}")
            sys.exit(1)
        instances = sorted(
            [
                d.name
                for d in resultados_dir.iterdir()
                if d.is_dir()
            ]
        )

    if not instances:
        print("No se encontraron instancias para procesar.")
        sys.exit(0)

    print(f"Procesando {len(instances)} instancia(s): {', '.join(instances)}")
    print()

    for instance in instances:
        process_instance(instance, str(resultados_dir), str(output_dir))

    print()
    print("Listo. Archivos generados en:", output_dir)


if __name__ == "__main__":
    main()
