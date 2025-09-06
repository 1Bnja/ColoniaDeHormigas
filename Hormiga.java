import java.util.*;

public class Hormiga {
    int[] tour;            
    boolean[] visitadas;   
    int longitud;          
    int posicionActual;    
    int paso;              

    public Hormiga(int numCiudades) {
        tour = new int[numCiudades];
        visitadas = new boolean[numCiudades];
        longitud = 0;
        paso = 0;
    }

    public void iniciar(int ciudadInicial) {
        Arrays.fill(visitadas, false);
        paso = 0;
        longitud = 0;
        posicionActual = ciudadInicial;
        tour[paso++] = ciudadInicial;
        visitadas[ciudadInicial] = true;
    }
}
