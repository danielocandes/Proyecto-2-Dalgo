


def make_partition(n): # O(n)
    parent = list(range(n))
    rank = [0]*n
    return parent,rank

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