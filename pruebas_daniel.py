def new_graph(vertices, aristas): # O(V+E), lista adyacencia
    grafo = {}
    for v in vertices:
        grafo[v] = []
    for v1, v2 in aristas:
        grafo[v1].append(v2)
        grafo[v2].append(v1)
    return grafo
    
def intento1(entrada: list):
    vertices_procesados = set()
    grafo_fibra = {}
    grafo_coaxial = {}
    part_fibra = make_partition()
    part_coaxial = make_partition()
    vert_a_part = {} #mapa que pasa de id de computador (vertice) ingresado a su id en la particion
    es_redundante = 0
    respuesta = ""
    
    for c1, c2, tipo in entrada:
        if c1 not in vertices_procesados:
            vertices_procesados.add(c1)
            procesar_vertice(grafo_fibra, grafo_coaxial, part_fibra, part_coaxial, vert_a_part, c1)
        if c2 not in vertices_procesados:
            vertices_procesados.add(c2)
            procesar_vertice(grafo_fibra, grafo_coaxial, part_fibra, part_coaxial, vert_a_part, c2)
            
        if tipo == 1:
            grafo_fibra[c1].append(c2)
            grafo_fibra[c2].append(c1)
            union(part_fibra, vert_a_part[c1], vert_a_part[c2])
        
        if tipo == 2:
            grafo_coaxial[c1].append(c2)
            grafo_coaxial[c2].append(c1)
            union(part_coaxial, vert_a_part[c1], vert_a_part[c2])
        
        es_redundante = 1
        for vert in vertices_procesados:
            id_vert = vert_a_part[vert]
            padre_fibra = find(part_fibra, id_vert)
            padre_coaxial = find(part_coaxial, id_vert)
            if not same_subset(part_coaxial, id_vert, padre_fibra):
                es_redundante = 0
                break
            if not same_subset(part_fibra, id_vert, padre_coaxial):
                es_redundante = 0
                break
        respuesta += str(es_redundante) + " "
    print(respuesta)

def procesar_vertice(gf, gc, pf, pc, vap, v):
    gf[v] = []
    gc[v] = []
    vap[v] = partition_size(pf)
    add_element(pf)
    add_element(pc)
    


def make_partition(): # O(n)
    parent = []
    rank = []
    return parent,rank

def partition_size(partition):
    return len(partition[0])

def add_element(partition):
    parent = partition[0]
    rank = partition[1]
    prev_size = len(parent)
    parent.append(prev_size)
    rank.append(0)
    return partition

def find(partition, e): # O(ackermann(n))
    parent = partition[0]
    if parent[e] != e:
        parent[e] = find(partition, parent[e])
    return parent[e]

def union(partition, e1, e2): # O(ackermann(n))
    p1 = find(partition, e1)
    p2 = find(partition, e2)
    if p1 == p2: return
    parent = partition[0]
    rank = partition[1]
    
    if rank[p1] < rank[p2]:
        parent[p1] = p2
    elif rank[p1] > rank[p2]:
        parent[p2] = p1
    else:
        parent[p2] = p1
        rank[p1] += 1
    return partition

def same_subset(partition, e1, e2): # O(ackermann(n))
    return find(partition, e1) == find(partition, e2)

entrada1 = [
    (1, 2, 1),
    (2, 3, 1),
    (1, 3, 2),
    (2, 3, 2),
    (4, 5, 2),
    (4, 5, 1)
]

entrada2 = [
    (1, 2, 1),
    (1, 2, 2)
]

entrada3 = [
    (1, 2, 2),
    (2, 3, 2),
    (1, 3, 2),
    (1, 3, 1)
]


intento1(entrada1)
intento1(entrada2)
intento1(entrada3)