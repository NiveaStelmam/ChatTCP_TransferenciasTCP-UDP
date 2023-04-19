import java.io.*;
import java.net.*;

public class Server {
    public static void main(String args[]) {
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(9876);
            byte[] receiveData = new byte[60000];
            byte[] sendData = new byte[60000];
            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);
                String sentence = new String(receivePacket.getData());
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String filename = sentence.trim();
                File file = new File(filename);
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                int bytesRead;
                long startTime = System.nanoTime();
                while ((bytesRead = bis.read(sendData, 0, sendData.length)) != -1) {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, bytesRead, IPAddress, port);
                    serverSocket.send(sendPacket);
                }
                long endTime = System.nanoTime();
                double duration = (endTime - startTime) / 1000000000.0;
                double fileSize = file.length() / (60000 * 60000);
                double transferRate = fileSize / duration;
                System.out.println("Arquivo " + filename + " transferido em " + duration + " segundos.");
                System.out.println("Taxa de transferência: " + transferRate + " MB/s");
                bis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}
