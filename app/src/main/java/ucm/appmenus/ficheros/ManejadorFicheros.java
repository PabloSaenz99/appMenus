package ucm.appmenus.ficheros;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManejadorFicheros {

    private String nombreFicheroEntrada;
    private String nombreFicheroSalida;
    private BufferedReader br;
    private BufferedWriter bw;

    //Convierte un fichero a string
    public static String fileToString(Context context, String inFile) throws IOException {
        InputStreamReader inputStreamReader =
                new InputStreamReader(context.openFileInput(inFile), StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (line != null) {
            stringBuilder.append(line).append('\n');
            line = reader.readLine();
        }
        inputStreamReader.close();
        return stringBuilder.toString();
    }

    public ManejadorFicheros(){}

    public ManejadorFicheros(String nombreEntrada, String nombreSalida) throws FileNotFoundException, IOException {
        nombreFicheroEntrada = nombreEntrada;
        nombreFicheroSalida = nombreSalida;
        bw = new BufferedWriter(new FileWriter(nombreFicheroSalida));
        br = new BufferedReader(new FileReader(nombreFicheroEntrada));
    }

    public void abrirFicheroEntrada(String nombre) throws FileNotFoundException{
        nombreFicheroEntrada = nombre;
        br = new BufferedReader(new FileReader(nombreFicheroEntrada));
    }

    public void abrirFicheroSalida(String nombre) throws FileNotFoundException, IOException {
        nombreFicheroSalida = nombre;
        bw = new BufferedWriter(new FileWriter(nombreFicheroSalida));
    }

    public String leerLinea() {
        try {
            String linea = br.readLine();
            if(linea == null){
                return null;
            }
            else{
                return linea;
            }
        } catch (IOException ex) {
            Logger.getLogger(ManejadorFicheros.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void cerrarFicheroEntrada(){
        try {
            br.close();
        } catch (IOException ex) {
            Logger.getLogger(ManejadorFicheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cerrarFicheroSalida(){
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(ManejadorFicheros.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void escribirResultado(String s) throws IOException {
        bw.write(s);
        bw.write('\n');
    }
}
