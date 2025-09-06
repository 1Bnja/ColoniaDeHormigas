
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Uso: java Main <numHormigas> <evaporacion> <iteraciones>");
            return;
        }

        int numHormigas= Integer.parseInt(args[0]);     
        double evaporacion= Double.parseDouble(args[1]);  
        int maxIter= Integer.parseInt(args[2]);     

        double alpha= 1.0;   
        double beta = 5.0;   
        double Q= 500.0; 

        List<Ciudad> ciudades = LeerArchivo.cargarCiudades("a280.tsp");
        int[][] dist = UtilidadesTSP.matrizDistancias(ciudades);
        double[][] tau = UtilidadesTSP.inicializarFeromonas(ciudades.size(), 1.0);

        Random rand = new Random();

        int mejorLongitud = Integer.MAX_VALUE;

        for (int it = 0; it < maxIter; it++) {

            List<int[]> tours = new ArrayList<>();
            List<Integer> longitudes = new ArrayList<>();

            for (int k = 0; k < numHormigas; k++) {
                int[] tour = UtilidadesTSP.construirTour(ciudades.size(), tau, dist, alpha, beta, rand);
                int longitud = UtilidadesTSP.distanciaRecorrida(tour, dist);

                tours.add(tour);
                longitudes.add(longitud);

                if (longitud < mejorLongitud) {
                    mejorLongitud = longitud;
                    mejorTour = tour.clone();
                }
            }

            for (int i = 0; i < tau.length; i++) {
                for (int j = 0; j < tau.length; j++) {
                    tau[i][j] *= (1 - evaporacion);
                }
            }

            for (int a = 0; a < tours.size(); a++) {
                int[] tour = tours.get(a);
                int longitud = longitudes.get(a);
                double delta = Q / longitud;

                for (int paso = 0; paso < tour.length - 1; paso++) {
                    int i = tour[paso];
                    int j = tour[paso + 1];
                    tau[i][j] += delta;
                    tau[j][i] = tau[i][j];
                }

                int u = tour[tour.length - 1];
                int v = tour[0];
                tau[u][v] += delta;
                tau[v][u] = tau[u][v];
            }

            System.out.println("Iteración " + it + " mejor hasta ahora: " + mejorLongitud);
        }

        System.out.println("\nMejor longitud encontrada: " + mejorLongitud);

        System.out.println();
    }
}
