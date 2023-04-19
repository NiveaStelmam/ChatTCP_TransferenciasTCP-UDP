import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client2 {

    private static final String IP_SERVIDOR = "localhost";
    private static final int PORTA_SERVIDOR = 12345;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nome = reader.readLine();

        System.out.println("Bem-vindo(a) " + nome + "!");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String mensagem = input.readLine();
                        System.out.println(mensagem);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão com o servidor perdida.");
                    System.exit(0);
                }
            }
        }).start();

        while (true) {
            String mensagem = reader.readLine();

            if (mensagem.equalsIgnoreCase("/exit")) {
                socket.close();
                System.exit(0);
            }

            output.println(nome + ": " + mensagem);
        }
    }
}
