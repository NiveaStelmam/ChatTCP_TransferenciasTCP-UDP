import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORTA = 12345;

    private static List<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        System.out.println("O servidor está rodando na porta " + PORTA);
        ServerSocket listener = new ServerSocket(PORTA);
        try {
            while (true) {
                new TratadorDeCliente(listener.accept()).start();
            }
        } finally {
            listener.close();
        }
    }

    private static class TratadorDeCliente extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;

        public TratadorDeCliente(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream(), true);

                // adiciona o PrintWriter do cliente na lista
                clientes.add(output);

                // notifica os clientes que um novo usuário entrou na conversa
                for (PrintWriter cliente : clientes) {
                    cliente.println("Um novo usuário entrou na conversa.");
                }

                while (true) {
                    String mensagem = input.readLine();

                    if (mensagem == null) {
                        return;
                    }

                    // envia a mensagem para todos os clientes
                    for (PrintWriter cliente : clientes) {
                        cliente.println(mensagem);
                    }
                }
            } catch (IOException e) {
                System.out.println(e);
            } finally {
                // remove o PrintWriter do cliente da lista
                clientes.remove(output);

                // notifica os clientes que um usuário saiu da conversa
                for (PrintWriter cliente : clientes) {
                    cliente.println("Um usuário saiu da conversa.");
                }

                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}