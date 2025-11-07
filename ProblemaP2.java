import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class Partition{
    public int partitionSize;
    public HashMap<Integer, Integer> parents;
    public HashMap<Integer, Integer> rank;
    public Set<Set<Integer>> partition;

    public Partition(int size){
        this.parents = new HashMap<Integer, Integer>();
        this.rank = new HashMap<Integer, Integer>();
        this.partition = new HashSet<Set<Integer>>();
        this.partitionSize = size;
        for (int i = 1; i <= size; i++) {
            parents.put(i, i);
            rank.put(i,0);
            Set<Integer> singleton = new HashSet<Integer>();
            singleton.add(i);
            partition.add(singleton);
        }
    }

    public int find(int e){
        if (parents.get(e) != e){
            parents.put(e, find(parents.get(e)));
        }
        return parents.get(e);
    }

    public void union(int e1, int e2){
        int root1 = find(e1);
        int root2 = find(e2);
        if (root1 == root2) return; 
        if (rank.get(root1) > rank.get(root2)) {
            parents.put(root2, root1);
        } else if (rank.get(root2) > rank.get(root1)) {
            parents.put(root1, root2);
        } else {
            parents.put(root2, root1);
            rank.put(root1, rank.get(root1) + 1);
        }
        this.partitionSize--;
    }

    public void refrescarParticion() {
        Map<Integer, Set<Integer>> grupos = new HashMap<>();
        for (Integer i : parents.keySet()) {
            int root = find(i);
            grupos.computeIfAbsent(root, k -> new HashSet<>()).add(i);
        }
        partition.clear();
        partition.addAll(grupos.values());
    }

}

public class ProblemaP2 {
    private Partition coaxial;
    private Partition fibra;
    private ArrayList<Integer> respuesta;

    public ProblemaP2(Integer numeroComputadores) {
        this.coaxial = new Partition(numeroComputadores);
        this.fibra = new Partition(numeroComputadores);;
        this.respuesta = new ArrayList<Integer>();
    }

    public Integer esRedundante(int e1, int e2, int cable) {
        if(fibra.partitionSize == 1 && coaxial.partitionSize == 1) return 1;
        if (cable == 1){ // es firba
            if (fibra.find(e1) != fibra.find(e2)) {
                fibra.union(e1, e2);
                return compararParticion(fibra, coaxial);
            }
            else {
                if (respuesta.isEmpty()) {
                    respuesta.add(0);
                    return 0;
                }
                else {
                    int last = respuesta.getLast();
                    respuesta.add(last);  
                    return last;
                }
            }
        }
        else {
            if (coaxial.find(e1) != coaxial.find(e2)){
                coaxial.union(e1, e2);
                return compararParticion(fibra, coaxial);
            }
            else {
                if (respuesta.isEmpty()) {
                    respuesta.add(0);
                    return 0;
                }
                else {
                    int last = respuesta.getLast();
                    respuesta.add(last);  
                    return last;
                }
            }
        }
    }

    public Integer compararParticion(Partition fibra, Partition coaxial) {
        if (fibra.partitionSize == coaxial.partitionSize) {
            fibra.refrescarParticion();
            coaxial.refrescarParticion();
            if (fibra.partition.size() != coaxial.partition.size()) {
                respuesta.add(0);
                return 0;
            }
            else if (fibra.partition.equals(coaxial.partition)) {
                respuesta.add(1);
                return 1;
            } 
            else {
                respuesta.add(0);
                return 0;
            }
        }
        else {
            respuesta.add(0);
            return 0;
        }
       
    }

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int T = Integer.parseInt(br.readLine().trim());

            while (T-- > 0) {
                String[] nm = br.readLine().trim().split(" ");
                int N = Integer.parseInt(nm[0]);
                int M = Integer.parseInt(nm[1]);

                ProblemaP2 orch = new ProblemaP2(N);
                StringBuilder sb = new StringBuilder();

                for (int m = 0; m < M; m++) {
                    String[] parts = br.readLine().trim().split(" ");
                    int i = Integer.parseInt(parts[0]);
                    int j = Integer.parseInt(parts[1]);
                    int k = Integer.parseInt(parts[2]);
                    int res = orch.esRedundante(i, j, k);
                    sb.append(res);
                    if (m < M - 1) sb.append(" ");
                }

                System.out.println(sb.toString());
            }
        } catch (Exception e) {
            System.out.println(0);
        }
    }

}

