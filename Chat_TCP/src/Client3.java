import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client3 {

    private static final String IP_SERVIDOR = "192.168.0.5";
    private static final int PORTA_SERVIDOR = 54321;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(IP_SERVIDOR, PORTA_SERVIDOR); // O código começa criando um objeto Socket que se conecta ao servidor especificado na porta especificada
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // O código cria um objeto BufferedReader que recebe a entrada de dados do servidor através do InputStream do socket
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true); // O código cria um objeto BufferedReader que recebe a entrada de dados do servidor através do InputStream do socket

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); // O código cria um objeto BufferedReader que recebe a entrada de dados do servidor através do InputStream do socket
        String nome = reader.readLine(); // lê o nome do usuário a partir da entrada do console e armazena em uma variável nome

        System.out.println("Bem-vindo(a) " + nome + "!");

        new Thread(new Runnable() { // lê o nome do usuário a partir da entrada do console e armazena em uma variável nome
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

        while (true) { // cria um loop infinito que aguarda o usuário digitar uma mensagem. A cada iteração do loop, a mensagem é lida a partir do objeto "reader" e armazenada na variável "mensagem". Em seguida, o código verifica se a mensagem é igual a "/exit" (ignorando maiúsculas e minúsculas) usando o método "equalsIgnoreCase()". Se a mensagem é "/exit", o objeto "socket" é fechado, encerrando a conexão com o servidor, e o programa é finalizado usando o método "System.exit(0)"
            String mensagem = reader.readLine();

            if (mensagem.equalsIgnoreCase("/exit")) {
                socket.close();
                System.exit(0);
            }

            output.println(nome + ": " + mensagem);
        }
    }
}