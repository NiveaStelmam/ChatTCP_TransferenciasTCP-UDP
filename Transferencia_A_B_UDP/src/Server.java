import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) {
        // Criando um socket UDP e inicializando-o na porta 9876
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[60000]; // armazena os dados recebidos
            byte[] sendData = new byte[60000]; // armazena os dados enciados
            // Loop infinito para escutar por novas requisições
            while (true) {
                // Criando um pacote para receber os dados do cliente
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); // recebe o pacote de datagrama do cliente, usando o metodo receive
                serverSocket.receive(receivePacket);

                // Convertendo os bytes recebidos em uma string
                String sentence = new String(receivePacket.getData());

                // Obtendo o endereço IP e a porta do cliente que enviou a requisição
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();

                // Extraindo o nome do arquivo a ser enviado a partir da string recebida
                String filename = sentence.trim();

                // Criando um objeto File a partir do nome do arquivo recebido
                File file = new File(filename);

                // Criando um objeto FileInputStream e BufferedInputStream para ler o arquivo
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis); //

                // Definindo a variável bytesRead para armazenar o número de bytes lidos
                int bytesRead;

                // Obtendo o tempo de início da transferência em nanosegundos
                long startTime = System.nanoTime();

                // Lendo os bytes do arquivo e enviando-os ao cliente em pacotes UDP
                while ((bytesRead = bis.read(sendData, 0, sendData.length)) != -1) {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, IPAddress, port);
                    serverSocket.send(sendPacket);
                }

                // Obtendo o tempo de término da transferência em nanosegundos
                long endTime = System.nanoTime();

                // Calculando o tempo de duração da transferência em segundos
                double duration = (endTime - startTime) / 1000000000.0;
                // Calculando o tamanho do arquivo transferido em megabytes
                double fileSize = file.length() / (60000 * 60000);
                // Calculando a taxa de transferência em megabytes por segundos
                double transferRate = fileSize / duration;

                // Exibindo informações sobre a transferência na saída padrão
                System.out.println("Arquivo " + filename + " transferido em " + duration + " segundos.");
                System.out.println("Taxa de transferência: " + transferRate + " MB/s");
                bis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Fechando o socket quando terminar de ser utilizado
            if (serverSocket != null) {
                serverSocket.close(); // encerrar a conexão
            }
        }
    }
}
