import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ClienteB {

    public static void main(String[] args) throws Exception {
        int serverPort = 9876;

        DatagramSocket clientSocket = new DatagramSocket();

        byte[] receiveData = new byte[1024];

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

        System.out.println("Digite o IP do servidor: ");
        String serverIP = System.console().readLine();

        DatagramPacket sendPacket = new DatagramPacket(new byte[1024], 1024);
        sendPacket.setAddress(InetAddress.getByName(serverIP));
        sendPacket.setPort(serverPort);
        clientSocket.send(sendPacket);

        System.out.println("Aguardando arquivo...");

        clientSocket.receive(receivePacket);

        long startTime = System.currentTimeMillis();

        byte[] fileData = receivePacket.getData();

        String fileName = "arquivo_recebido.txt";

        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        fileOutputStream.write(fileData);

        System.out.println("Arquivo recebido!");

        long endTime = System.currentTimeMillis();

        System.out.println("Tempo de transferência: " + (endTime - startTime) + " ms");

        fileOutputStream.close();
        clientSocket.close();
    }
}
