import csv
import os
import glob
import matplotlib.pyplot as plt
import matplotlib.ticker as ticker

# Carpeta de la instancia que quiera sacar las gráficas
INSTANCIA = "CMT5"

# Valor de la línea coste optimo
LINEA_REFERENCIA = 1291.289144

def obtener_csvs(directorio):
    patron = os.path.join(directorio, "resultados_*.csv")
    archivos = glob.glob(patron)
    if not archivos:
        print(f"No se encontraron CSVs en {directorio}")
    return sorted(archivos)


def parsear_csv(ruta_csv):
    datos_sin_split = {"tiempo": [], "coste": []}
    datos_con_split = {"tiempo": [], "coste": []}
    operador = ""
    instancia = ""

    with open(ruta_csv, "r", encoding="utf-8") as f:
        lector = csv.reader(f)
        cabecera = next(lector)

        for fila in lector:
            if not fila or len(fila) < 7:
                continue

            try:
                iteracion = int(fila[0])
                tiempo_us = float(fila[1])
                coste = float(fila[2])
                operador = fila[3]
                instancia = fila[4]
                con_split = fila[6].strip().lower() == "true"
            except (ValueError, IndexError):
                continue

            if con_split:
                datos_con_split["tiempo"].append(tiempo_us)
                datos_con_split["coste"].append(coste)
            else:
                datos_sin_split["tiempo"].append(tiempo_us)
                datos_sin_split["coste"].append(coste)

    return datos_sin_split, datos_con_split, operador, instancia


def generar_grafica(ruta_csv, ruta_salida=None):
    datos_sin, datos_con, operador, instancia = parsear_csv(ruta_csv)

    if not datos_sin["tiempo"] and not datos_con["tiempo"]:
        print(f"  No hay datos validos en {os.path.basename(ruta_csv)}")
        return

    fig, ax = plt.subplots(figsize=(12, 7))

    if datos_sin["tiempo"]:
        ax.plot(
            datos_sin["tiempo"],
            datos_sin["coste"],
            marker="o",
            markersize=3,
            linewidth=1.2,
            label="Sin Split",
            color="#2196F3",
            alpha=0.85,
        )

    if datos_con["tiempo"]:
        ax.plot(
            datos_con["tiempo"],
            datos_con["coste"],
            marker="s",
            markersize=3,
            linewidth=1.2,
            label="Con Split",
            color="#F44336",
            alpha=0.85,
        )

    if LINEA_REFERENCIA:
        ax.axhline(
            y=LINEA_REFERENCIA,
            color="#4CAF50",
            linestyle="--",
            linewidth=1.5,
            label=f"Referencia ({LINEA_REFERENCIA:,.0f})",
            alpha=0.8,
        )

    titulo = f"{instancia.replace('.vrp', '').replace('.txt', '')} - {operador}"
    ax.set_title(titulo, fontsize=16, fontweight="bold", pad=15)
    ax.set_xlabel("Tiempo (microsegundos)", fontsize=12)
    ax.set_ylabel("Coste", fontsize=12)
    ax.legend(fontsize=11, framealpha=0.9)
    ax.grid(True, alpha=0.3)

    ax.yaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{x:,.0f}"))
    ax.xaxis.set_major_formatter(ticker.FuncFormatter(lambda x, _: f"{x:,.0f}"))

    plt.tight_layout()

    if ruta_salida is None:
        nombre_base = os.path.splitext(os.path.basename(ruta_csv))[0]
        ruta_salida = os.path.join(os.path.dirname(ruta_csv), f"{nombre_base}.png")

    plt.savefig(ruta_salida, dpi=150, bbox_inches="tight")
    print(f"  Grafica guardada: {ruta_salida}")
    plt.close()


if __name__ == "__main__":
    directorio_base = os.path.join("src", "main", "resources", "resultados")
    directorio_instancia = os.path.join(directorio_base, INSTANCIA)

    if not os.path.isdir(directorio_instancia):
        print(f"La carpeta de instancia no existe: {directorio_instancia}")
        print(f"Carpetas disponibles: {os.listdir(directorio_base) if os.path.isdir(directorio_base) else 'Ninguna'}")
        exit(1)

    print(f"Procesando CSVs de: {directorio_instancia}")
    csvs = obtener_csvs(directorio_instancia)

    for csv_path in csvs:
        print(f"\nProcesando: {os.path.basename(csv_path)}")
        generar_grafica(csv_path)

    print(f"\nProceso completado. {len(csvs)} graficas generadas.")
