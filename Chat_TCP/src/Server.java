import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private static final int PORTA = 54321;

    private static List<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        // Imprime uma mensagem na tela informando que o servidor está rodando na porta especificada

        System.out.println("O servidor está rodando na porta " + PORTA);

        // Cria um objeto ServerSocket que ficará escutando na porta especificada
        ServerSocket listener = new ServerSocket(PORTA);
        try {
            // Cria um loop infinito para receber conexões de clientes
            while (true) {

                //Cada vez que o servidor aceita uma conexão de um cliente, ele cria um objeto TratadorDeCliente que estende a classe Thread.
                //Esse objeto é responsável por lidar com as solicitações do cliente de forma assíncrona, ou seja, sem bloquear o servidor para
                //outros clientes que possam estar conectados simultaneamente.
                new TratadorDeCliente(listener.accept()).start();
            }
        } finally {
            // Fecha o objeto ServerSocket
            listener.close();
        }
    }

    // Classe interna que lida com as conexões dos clientes
    private static class TratadorDeCliente extends Thread {
        private Socket socket;
        private BufferedReader input;
        private PrintWriter output;

        public TratadorDeCliente(Socket socket) {
            // Armazena o socket da conexão
            this.socket = socket;
        }

        public void run() {
            try {
                // Cria um BufferedReader para ler as mensagens enviadas pelo cliente
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // Cria um PrintWriter para enviar mensagens para o cliente
                output = new PrintWriter(socket.getOutputStream(), true);

                // Adiciona o PrintWriter do cliente na lista de clientes conectados
                clientes.add(output);

                // Percorre a lista PrintWriter e envia uma mensagem para todos os clientes informando que um novo usuário entrou na conversa
                for (PrintWriter cliente : clientes) {
                    cliente.println("Um novo usuario entrou na conversa.");
                }

                // Loop infinito para ler e enviar mensagens para todos os clientes conectados
                while (true) {
                    // Lê uma mensagem enviada pelo cliente
                    String mensagem = input.readLine();

                    // Se a mensagem for nula, significa que o cliente desconectou
                    if (mensagem == null) {
                        return;
                    }

                    // Envia a mensagem para todos os clientes conectados
                    for (PrintWriter cliente : clientes) {
                        cliente.println(mensagem);
                    }
                }
            } catch (IOException e) {
                // Exibe a mensagem de erro no console
                System.out.println(e);
            } finally {
                // Remove o PrintWriter do cliente da lista de clientes conectados
                clientes.remove(output);

                // Envia uma mensagem para todos os clientes informando que um usuário saiu da conversa
                for (PrintWriter cliente : clientes) {
                    cliente.println("Um usuário saiu da conversa.");
                }

                try {
                    // Fecha o socket da conexão
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
