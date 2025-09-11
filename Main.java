import java.util.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Uso: java Main <numHormigas> <evaporacion> <iteraciones>");
            return;
        }

        int numHormigas = Integer.parseInt(args[0]);
        double evaporacion = Double.parseDouble(args[1]);
        int maxIter = Integer.parseInt(args[2]);

        double alpha = 1.0; // impoetancia de feromonas
        double beta = 3.0; // importancia de la heuristica, un valor mas alto hace que las hormigas prefieran rutas mas cortas
        double Q = 1000.0; // cantidad de feromona depositada por hormiga

        List<Ciudad> ciudades = LeerArchivo.cargarCiudades("a280.tsp");
        int[][] dist = UtilidadesTSP.matrizDistancias(ciudades);
        
        int sumaDistancias = 0;
        int numPares = 0;

        for (int i = 0; i < dist.length; i++) {
            for (int j = i + 1; j < dist[i].length; j++) { 
                sumaDistancias += dist[i][j];
                numPares++;
            }
        }

        double distanciaPromedio = (double) sumaDistancias / numPares;
        double valorInicialFeromona = 1.0 / (ciudades.size() * distanciaPromedio);
        double[][] tau = UtilidadesTSP.inicializarFeromonas(ciudades.size(), valorInicialFeromona);

        int mejorLongitud = Integer.MAX_VALUE;
        int[] mejorTour = null;

        Random randGlobal = new Random();

        for (int it = 0; it < maxIter; it++) {
            final int iter = it;
            
            int[][] tours = new int[numHormigas][];
            int[] longitudes = new int[numHormigas];

            IntStream.range(0, numHormigas).parallel().forEach(k -> {
                Random rand = new Random(randGlobal.nextLong() + k + iter * numHormigas);
                int[] tour = UtilidadesTSP.construirTour(ciudades.size(), tau, dist, alpha, beta, rand);
                int longitud = UtilidadesTSP.distanciaRecorrida(tour, dist);
                tours[k] = tour;
                longitudes[k] = longitud;
            });

            for (int k = 0; k < numHormigas; k++) {
                if (longitudes[k] < mejorLongitud) {
                    mejorLongitud = longitudes[k];
                    mejorTour = Arrays.copyOf(tours[k], tours[k].length);
                }
            }

            double factorEvaporacion = 1 - evaporacion;
            for (int i = 0; i < tau.length; i++) {
                for (int j = 0; j < tau.length; j++) {
                    tau[i][j] *= factorEvaporacion;
                }
            }

            for (int k = 0; k < numHormigas; k++) {
                double delta = Q / longitudes[k];
                int[] tour = tours[k];
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

            int progreso = (int) ((it + 1) / (double) maxIter * 50);
            if ((it + 1) % 10 == 0 || it + 1 == maxIter) {
                System.out.print("\rProgreso: [");
                for (int i = 0; i < 50; i++) {
                    if (i < progreso) {
                        System.out.print("#");
                    } else {
                        System.out.print(".");
                    }
                }
                System.out.print("] " + (it + 1) + "/" + maxIter + " Mejor hasta ahora: " + mejorLongitud);
            }

            if (it + 1 == maxIter) {
                System.out.println("\n\nMejor longitud encontrada: " + mejorLongitud);
                System.out.print("Mejor tour encontrado: ");
                if (mejorTour != null) {
                    for (int i = 0; i < mejorTour.length; i++) {
                        System.out.print(mejorTour[i]);
                        if (i < mejorTour.length - 1) System.out.print(" -> ");
                    }
                    System.out.println();
                }
                int optimo = 2579;
                double margenError = 100.0 * (mejorLongitud - optimo) / optimo;
                System.out.printf("Óptimo conocido: %d\n", optimo);
                System.out.printf("Margen de error: %.2f%%\n", margenError);
            }
        }
    }
}