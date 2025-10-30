import random

def generar_prueba_azar(N, M, nombre_archivo):
    
    conexiones = set()
    resultado = []

    while len(resultado) < M:
        i = random.randint(1, N)
        j = random.randint(1, N)
        if i == j:
            continue
        if i > j:
            i, j = j, i

        k = random.choice([1, 2])

        # no ponemos la misma conexion de nuevo
        if (i, j, k) in conexiones:
            continue
        
        conexiones.add((i, j, k))
        resultado.append((i, j, k))

    with open(nombre_archivo, "w") as f:
        f.write("1\n")
        f.write(f"{N} {M}\n")
        for i, j, k in resultado:
            f.write(f"{i} {j} {k}\n")

    print(f"Archivo '{nombre_archivo}' generado con {N} vértices y {M} conexiones.")

def generar_prueba_estructurada(N, M, nombre_archivo):
    conexiones = []
    tipo = 1

    for i in range(1, N + 1):
        for j in range(i + 1, N + 1):
            if len(conexiones) >= M:
                break
            conexiones.append((i, j, tipo))
            tipo = 2 if tipo == 1 else 1  # alternar tipo
        if len(conexiones) >= M:
            break

    # Si todavia faltan conexiones (M > N*(N-1)/2) se vuelve a recorrer con el otro tipo de cable.
    i, j = 1, 2
    while len(conexiones) < M:
        if j > N:
            i += 1
            j = i + 1
        if j > N:
            break
        conexiones.append((i, j, tipo))
        tipo = 2 if tipo == 1 else 1
        j += 1

    with open(nombre_archivo, "w") as f:
        f.write("1\n")
        f.write(f"{N} {M}\n")
        for a, b, k in conexiones:
            f.write(f"{a} {b} {k}\n")

    print(f"Archivo 'input.txt' generado con {len(conexiones)} conexiones.")
    
N = 10   # num computadores
M = 30  # num conexiones
generar_prueba_estructurada(N, M, "estructurada30conexiones.txt")
