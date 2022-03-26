package ucm.appmenus.ficheros;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import ucm.appmenus.R;

public class ManejadorFicheros {

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

    //Crea un fichero
    public static void crearFichero(File dir, String nombre, String texto) {
        File fich = new File(dir, nombre);
        try {
            FileWriter writer = new FileWriter(fich);
            writer.write(texto);
            writer.flush();
            writer.close();
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }
}
