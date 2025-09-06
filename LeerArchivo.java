import java.io.*;
import java.util.*;

public class LeerArchivo {
    public static List<Ciudad> cargarCiudades(String ruta) throws IOException {
        List<Ciudad> ciudades = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(ruta));
        String linea;
        boolean leyendo = false;

        while ((linea = br.readLine()) != null) {
            linea = linea.trim();

            if (linea.startsWith("NODE_COORD_SECTION")) {
                leyendo = true;
                continue;
            }
            if (linea.startsWith("EOF")) break;

            if (leyendo && !linea.isEmpty()) {
                String[] partes = linea.split("\\s+");
                int id = Integer.parseInt(partes[0]);
                double x = Double.parseDouble(partes[1]);
                double y = Double.parseDouble(partes[2]);
                ciudades.add(new Ciudad(id, x, y));
            }
        }
        br.close();
        return ciudades;
    }
}
