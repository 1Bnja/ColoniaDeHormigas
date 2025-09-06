import java.util.List;
import java.util.Random;

public class UtilidadesTSP {
    public static int distancia(Ciudad a, Ciudad b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return (int) Math.round(Math.sqrt(dx*dx + dy*dy));
    }

    public static int[][] matrizDistancias(List<Ciudad> ciudades) {
        int n = ciudades.size();
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else {
                    dist[i][j] = distancia(ciudades.get(i), ciudades.get(j));
                }
            }
        }
        return dist;
    }

    public static int distanciaRecorrida(int[] camino, int[][] dist) {
        int total = 0;
        for (int i = 0; i < camino.length - 1; i++) {
            int a = camino[i];
            int b = camino[i+1];
            total += dist[a][b];
        }
        total += dist[camino[camino.length - 1]][camino[0]];
        return total;
    }
    
    public static double[][] inicializarFeromonas(int numCiudades, double valorInicial) {
        double[][] tau = new double[numCiudades][numCiudades];
        for (int i = 0; i < numCiudades; i++) {
            for (int j = 0; j < numCiudades; j++) {
                if (i == j) {
                    tau[i][j] = 0.0; 
                } else {
                    tau[i][j] = valorInicial;
                }
            }
        }
        return tau;
    }

    public static int elegirSiguienteCiudad(int ciudadActual, boolean[] visitadas, double[][] tau,int[][] dist,double alpha,double beta,Random rand) {

        int n = visitadas.length;
        double[] probabilidades = new double[n];
        double suma = 0.0;

        
        for (int j = 0; j < n; j++) {
            if (!visitadas[j]) {
                double feromona = Math.pow(tau[ciudadActual][j], alpha);
                double heuristica = Math.pow(1.0 / (dist[ciudadActual][j] + 1e-6), beta);
                probabilidades[j] = feromona * heuristica;
                suma += probabilidades[j];
            } else {
                probabilidades[j] = 0.0;
            }
        }

        double r = rand.nextDouble() * suma;
        double acumulado = 0.0;
        for (int j = 0; j < n; j++) {
            acumulado += probabilidades[j];
            if (acumulado >= r) {
                return j;
            }
        }

        for (int j = 0; j < n; j++) {
            if (!visitadas[j]) return j;
        }
        return -1; 
    }

    public static int[] construirTour(int numCiudades,double[][] tau,int[][] dist,double alpha,double beta,Random rand) {

        int[] tour = new int[numCiudades];
        boolean[] visitadas = new boolean[numCiudades];

        int inicio = rand.nextInt(numCiudades);
        tour[0] = inicio;
        visitadas[inicio] = true;
        int actual = inicio;

        for (int paso = 1; paso < numCiudades; paso++) {
            int siguiente = elegirSiguienteCiudad(actual, visitadas, tau, dist, alpha, beta, rand);
            tour[paso] = siguiente;
            visitadas[siguiente] = true;
            actual = siguiente;
        }

        return tour;
    }   

}
    

