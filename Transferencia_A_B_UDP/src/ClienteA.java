import java.io.*;
import java.net.*;

public class ClienteA {
    public static void main(String args[]) {
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket(); // objeto socket para enviar e receber pacotes de dados
            InetAddress IPAddress = InetAddress.getByName("localhost"); // receber o numero da porta
            int port = 9876;
            String filename = "C:\\Users\\danta\\IdeaProjects\\Transferencia_A_B_UDP\\arquivo.txt\n"; //Definição do nome do arquivo a ser transferido como uma string e conversão para um array de bytes.
            byte[] sendData = filename.getBytes(); // enviar o arquivo para o servidor
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); // Criação de um objeto DatagramPacket com os dados a serem enviados e envio do pacote ao servidor através do método send() do DatagramSocket.
            clientSocket.send(sendPacket);
            byte[] receiveData = new byte[60000];//  armazena os dados recebidos e guarda os conteudos em um arquivo local
            FileOutputStream fos = new FileOutputStream("copia_arquivo.txt");//
            BufferedOutputStream bos = new BufferedOutputStream(fos);//
            long startTime = System.nanoTime();
            while (true) { // Criação de um loop infinito para receber pacotes de dados do servidor usando o método receive() .
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                bos.write(receivePacket.getData(), 0, receivePacket.getLength()); // Gravação dos dados recebidos no arquivo local usando o método write() do BufferedOutputStream (bos).
                if (receivePacket.getLength() < 60000) {
                    break;
                }
            }
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000000.0; // Cálculo do tempo de transferência do arquivo e da taxa de transferência com base no tempo de transferência e no tamanho do arquivo.
            double fileSize = new File("copia_arquivo.txt").length() / (60000 * 6000.0);
            double transferRate = fileSize / duration;
            System.out.println("Arquivo " + filename + " recebido em " + duration + " segundos."); // print do trempo de transferencia
            System.out.println("Taxa de transferência: " + transferRate + " MB/s");
            bos.close(); // fechamento do arquivo
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close(); // encerramento da conexão
            }
        }
    }
}
