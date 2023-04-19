import java.io.*;
import java.net.*;

public class ClienteA {
    public static void main(String args[]) {
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            int port = 9876;
            String filename = "C:\\Users\\danta\\IdeaProjects\\Transferencia_A_B_UDP\\arquivo.txt\n";
            byte[] sendData = filename.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
            byte[] receiveData = new byte[60000];
            FileOutputStream fos = new FileOutputStream("copia_arquivo.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            long startTime = System.nanoTime();
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                bos.write(receivePacket.getData(), 0, receivePacket.getLength());
                if (receivePacket.getLength() < 60000) {
                    break;
                }
            }
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000000.0;
            double fileSize = new File("copia_arquivo.txt").length() / (60000 * 6000.0);
            double transferRate = fileSize / duration;
            System.out.println("Arquivo " + filename + " recebido em " + duration + " segundos.");
            System.out.println("Taxa de transferência: " + transferRate + " MB/s");
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}
