import csv
import os
import glob
import re
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

OPTIMOS = {
    "CMT1": 524.61,
    "CMT2": 835.26,
    "CMT3": 826.14,
    "CMT4": 1028.42,
    "CMT5": 1291.29,
    "CMT6": 555.43,
    "CMT7": 909.675
}

ALIAS_ALGORITMOS = {
    "GA_Combinacion": "GA + Combinacion",
    "GA_split": "GA + Split",
    "MultiStart_Combinacion": "MultiStart",
}

COLORES = {
    "GA_Combinacion": "#2196F3",
    "GA_split": "#F44336",
    "MultiStart_Combinacion": "#FF9800",
}

MARCADORES = {
    "GA_Combinacion": "o",
    "GA_split": "s",
    "MultiStart_Combinacion": "^",
}


def agrupar_csvs(directorio):
    patron = os.path.join(directorio, "convergencia_CMT*.csv")
    archivos = sorted(glob.glob(patron))
    if not archivos:
        print(f"No se encontraron CSVs en {directorio}")
        return {}

    grupos = {}
    for ruta in archivos:
        nombre = os.path.basename(ruta)
        partes = nombre.split("_")
        instancia = partes[1]
        algoritmo = "_".join(partes[2:-2])
        grupos.setdefault(instancia, {})[algoritmo] = ruta

    return grupos


def parsear_csv(ruta_csv):
    tiempos = []
    costes = []
    promedios = []
    config = {}

    with open(ruta_csv, "r", encoding="utf-8") as f:
        lector = csv.reader(f)
        cabecera = next(lector)

        tiene_promedio = "promedio_padres" in cabecera
        tiene_config = "cruce" in cabecera

        for fila in lector:
            if not fila or len(fila) < 3:
                continue
            try:
                tiempo_s = float(fila[1]) / 1_000_000
                coste = float(fila[2])
            except (ValueError, IndexError):
                continue

            # Leer config de la primera fila de datos
            if not tiempos and tiene_config and len(fila) >= 9:
                config["cruce"] = fila[4]
                config["prob_cruce"] = fila[5]
                config["mutacion"] = fila[6]
                config["prob_mutacion"] = fila[7]
                config["iteraciones_sin_mejora"] = fila[8]
                if len(fila) > 10:
                    config["estrategia_bl"] = fila[9]
                    config["porcentaje_bl"] = fila[10]

            tiempos.append(tiempo_s)
            costes.append(coste)

            if tiene_promedio and len(fila) > 3:
                try:
                    promedios.append(float(fila[3]))
                except ValueError:
                    promedios.append(None)
            else:
                promedios.append(None)

    return tiempos, costes, promedios, config


def generar_grafica(instancia, archivos_algoritmo, ruta_salida):
    fig, ax = plt.subplots(figsize=(12, 7))

    config_acc = {}

    for algoritmo, ruta in sorted(archivos_algoritmo.items()):
        tiempos, costes, promedios, config = parsear_csv(ruta)
        if not tiempos:
            print(f"  No hay datos en {os.path.basename(ruta)}")
            continue

        if config:
            config_acc = config

        label = ALIAS_ALGORITMOS.get(algoritmo, algoritmo)
        color = COLORES.get(algoritmo, "#333333")
        marker = MARCADORES.get(algoritmo, ".")

        ax.plot(
            tiempos,
            costes,
            marker=marker,
            markersize=3,
            linewidth=1.2,
            label=label,
            color=color,
            alpha=0.85,
        )
        ax.annotate(f"{costes[-1]:,.0f}",
                    xy=(tiempos[-1], costes[-1]),
                    xytext=(5, 5), textcoords="offset points",
                    fontsize=8, color=color, fontweight="bold")

        if any(p is not None for p in promedios):
            ax.plot(
                tiempos,
                promedios,
                marker=marker,
                markersize=2,
                linewidth=0.8,
                linestyle=":",
                label=f"{label} (promedio padres)",
                color=color,
                alpha=0.5,
            )
            ax.annotate(f"{promedios[-1]:,.0f}",
                        xy=(tiempos[-1], promedios[-1]),
                        xytext=(5, 5), textcoords="offset points",
                        fontsize=8, color=color, fontweight="bold", alpha=0.5)

    optimo = OPTIMOS.get(instancia)
    if optimo:
        ax.axhline(
            y=optimo,
            color="#4CAF50",
            linestyle="--",
            linewidth=1.5,
            label=f"Optimo ({optimo:,.2f})",
            alpha=0.8,
        )

    titulo = f"{instancia} - Convergencia"
    if config_acc:
        eval_str = f"BL: {config_acc['estrategia_bl']}"
        if config_acc.get('estrategia_bl') == 'porcentaje':
            eval_str += f" ({config_acc['porcentaje_bl']})"
        titulo += (
            f"\nCruce: {config_acc['cruce']} ({config_acc['prob_cruce']}), "
            f"Mutacion: {config_acc['mutacion']} ({config_acc['prob_mutacion']}), "
            f"{eval_str}, "
            f"Iter sin mejora: {config_acc['iteraciones_sin_mejora']}"
        )
    ax.set_title(titulo, fontsize=14, fontweight="bold", pad=15)
    ax.set_xlabel("Tiempo (segundos)", fontsize=12)
    ax.set_ylabel("Coste", fontsize=12)
    ax.legend(fontsize=11, framealpha=0.9)
    ax.grid(True, alpha=0.3)

    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{x:,.0f}"))
    ax.xaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{x:,.2f}"))

    plt.tight_layout()
    plt.savefig(ruta_salida, dpi=150, bbox_inches="tight")
    print(f"  Grafica guardada: {ruta_salida}")
    plt.close()


if __name__ == "__main__":
    import sys

    if len(sys.argv) >= 2:
        ruta_csv = sys.argv[1]
        if not os.path.isfile(ruta_csv):
            print(f"El archivo no existe: {ruta_csv}")
            exit(1)

        tiempos, costes, promedios, config = parsear_csv(ruta_csv)
        if not tiempos:
            print(f"No hay datos validos en {ruta_csv}")
            exit(1)

        nombre = os.path.splitext(os.path.basename(ruta_csv))[0]
        instancia = nombre.split("_")[1]
        directorio = os.path.dirname(ruta_csv)

        ruta_salida = os.path.join(directorio, f"{nombre}.png")

        # Simular estructura que espera generar_grafica
        archivos_algoritmo = {}
        partes = nombre.split("_")
        algoritmo = "_".join(partes[2:-2])
        archivos_algoritmo[algoritmo] = ruta_csv

        generar_grafica(instancia, archivos_algoritmo, ruta_salida)
    else:
        directorio = os.path.join(os.path.dirname(__file__), "graficas")

        if not os.path.isdir(directorio):
            print(f"La carpeta no existe: {directorio}")
            exit(1)

        grupos = agrupar_csvs(directorio)
        if not grupos:
            exit(1)

        print(f"Instancias encontradas: {', '.join(sorted(grupos.keys()))}")

        for instancia in sorted(grupos.keys()):
            archivos_algoritmo = grupos[instancia]
            print(f"\n{instancia}:")
            for algo in sorted(archivos_algoritmo.keys()):
                print(f"  - {algo}: {os.path.basename(archivos_algoritmo[algo])}")

            ruta_salida = os.path.join(directorio, f"convergencia_{instancia}.png")
            generar_grafica(instancia, archivos_algoritmo, ruta_salida)

        print(f"\nProceso completado. {len(grupos)} graficas generadas.")
