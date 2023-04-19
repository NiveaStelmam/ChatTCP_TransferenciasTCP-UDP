import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            // Estabelecendo conexão com o servidor
            socket = new Socket("localhost", 8000);
            System.out.println("Conexão estabelecida");

            // Definindo arquivo a ser enviado
            File file = new File("C:\\Users\\Adriano\\Documents\\TrabalhoSD\\msg\\SESSENTA.txt");

            // Enviando arquivo para o servidor
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = socket.getOutputStream();
            byte[] buffer = new byte[60000];
            int bytesRead;
            long start = System.currentTimeMillis();
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            long end = System.currentTimeMillis();
            System.out.println("Arquivo enviado em " + (end - start) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
