import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            // Criando socket do servidor e aguardando conexão
            serverSocket = new ServerSocket(8000);
            System.out.println("Servidor aguardando conexão...");
            socket = serverSocket.accept();
            System.out.println("Conexão estabelecida");

            // Recebendo arquivo enviado pelo cliente
            InputStream is = socket.getInputStream();
            File file = new File("arquivo_recebido.txt");
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[60000];
            int bytesRead;
            long start = System.currentTimeMillis();
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            long end = System.currentTimeMillis();
            System.out.println("Arquivo recebido em " + (end - start) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

