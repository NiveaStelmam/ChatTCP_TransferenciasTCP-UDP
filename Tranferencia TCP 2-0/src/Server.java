import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null; // será usada para aguardar conexões de entrada de clientes.
        Socket socket = null; // será usada para lidar com a conexão de um cliente específico após uma conexão ser estabelecida com sucesso.
        try {
            // Criando socket do servidor e aguardando conexão
            serverSocket = new ServerSocket(8000); // cria um objeto ServerSocket para o servidor na porta 8000
            System.out.println("Servidor aguardando conexão...");
            socket = serverSocket.accept(); // Quando o cliente se conecta, o metodo accept retorna um objeto socket representando a conexão do cliente
            System.out.println("Conexão estabelecida");

            // Recebendo arquivo enviado pelo cliente
            InputStream is = socket.getInputStream(); // obtém um objeto InputStream para receber dados do cliente.
            File file = new File("arquivo_recebido.txt"); // cria um objeto File para representar o arquivo recebido do cliente
            FileOutputStream fos = new FileOutputStream(file); // cria um objeto FileOutputStream para escrever os dados recebidos no arquivo.
            byte[] buffer = new byte[60000];  // Essa variável será usada para armazenar os dados recebidos de um cliente em uma conexão de rede.
            int bytesRead; //  Essa variável será usada para armazenar o número de bytes lidos da conexão de rede.
            long start = System.currentTimeMillis(); // Este método retorna o tempo atual em milissegundos

            while ((bytesRead = is.read(buffer)) != -1) {// lê os dados enviados pelo cliente e armazena no buffer até que não haja mais dados a serem lidos.
                fos.write(buffer, 0, bytesRead);
            }
            long end = System.currentTimeMillis();
            System.out.println("Arquivo recebido em " + (end - start) + "ms"); // calcula o incio de leitura menos o fim
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) { // encerrar a conexão socket cliente
                    socket.close();
                }
                if (serverSocket != null) { // encerra a conexão socket do servidor
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

