import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

class Partition{
    public int partitionSize;
    public HashMap<Integer, Integer> parents;
    public HashMap<Integer, Integer> sizes;
    public Set<Set<Integer>> partition;

    public Partition(int size){
        this.parents = new HashMap<Integer, Integer>();
        this.sizes = new HashMap<Integer, Integer>();
        this.partition = new HashSet<Set<Integer>>();
        this.partitionSize = size;
        for (int i = 1; i <= size; i++) {
            parents.put(i, i);
            sizes.put(i,1);
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
        if (sizes.get(root1) > sizes.get(root2)) {
            parents.put(root2, root1);
        } else if (sizes.get(root2) > sizes.get(root1)) {
            parents.put(root1, root2);
        } else {
            parents.put(root2, root1);
            sizes.put(root1, sizes.get(root1) + 1);
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

class Orchestrator {
    private Partition coaxial;
    private Partition fibra;
    private ArrayList<Integer> respuesta;

    public Orchestrator(Integer numeroComputadores) {
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
                if (respuesta.isEmpty()) return 0;
                else return respuesta.getLast();
            }
        }
        else {
            if (coaxial.find(e1) != coaxial.find(e2)){
                coaxial.union(e1, e2);
                return compararParticion(fibra, coaxial);
            }
            else {
                if (respuesta.isEmpty()) return 0;
                else return respuesta.getLast();
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
            if (fibra.partition.equals(coaxial.partition)) {
                respuesta.add(1);
                return 1;
            } else {
                respuesta.add(0);
                return 0;
            }
        }
        return 0;
    }

    public class Main {

        public static void main(String[] args) {
            try {
                File archivo = new File("estructurada30conexiones.txt");
                Scanner sc = new Scanner(archivo);

                int T = sc.nextInt(); 

                for (int t = 0; t < T; t++) {
                    int N = sc.nextInt(); 
                    int M = sc.nextInt(); 
                    Orchestrator orch = new Orchestrator(N);
                    ArrayList<Integer> resultados = new ArrayList<>();

                    for (int m = 0; m < M; m++) {
                        int i = sc.nextInt();
                        int j = sc.nextInt();
                        int k = sc.nextInt();

                        Integer r = orch.esRedundante(i, j, k);
                        resultados.add(r);
                    }

                    for (int i = 0; i < resultados.size(); i++) {
                        System.out.print(resultados.get(i));
                        if (i < resultados.size() - 1) System.out.print(" ");
                    }
                    System.out.println(); 
                }

                sc.close();

            } catch (FileNotFoundException e) {
                System.out.println("No se encontró el archivo de entrada.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

