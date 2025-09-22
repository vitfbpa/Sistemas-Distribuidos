/**
 * A classe Comunicador fornece métodos estáticos para facilitar a comunicação
 * de rede TCP, abstraindo a complexidade de lidar com Sockets, Streams e
 * Readers/Writers.
 *
 * Ela oferece suporte para envio e recebimento de texto (String) e de objetos
 * Java serializáveis.
 */
package pessoa;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Comunicador {

    /**
     * Envia uma única linha de texto para um servidor e aguarda o recebimento
     * de uma única linha de texto como resposta.
     *
     * @param host O endereço do servidor (ex: "127.0.0.1").
     * @param porta A porta na qual o servidor está ouvindo.
     * @param linha A String de texto a ser enviada.
     * @return A String de texto recebida como resposta do servidor.
     * @throws IOException Se ocorrer um erro de I/O durante a comunicação.
     */
    public static String enviarEReceber(String host, int porta, String linha) throws IOException {
        // Abre um socket e garante que ele seja fechado ao final (try-with-resources)
        try (Socket socket = new Socket(host, porta)) {
            // Define um tempo máximo de 5 segundos para a leitura (evita bloqueio infinito)
            socket.setSoTimeout(5000);
            
            // Cria um canal de escrita (PrintWriter) e um de leitura (BufferedReader)
            try (PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
                
                // Envia a linha de texto para o servidor
                out.println(linha == null ? "" : linha);
                
                // Lê e retorna a linha de resposta do servidor
                return in.readLine();
            }
        }
    }

    /**
     * Envia um objeto serializável para um servidor e aguarda o recebimento
     * de um objeto serializável como resposta.
     *
     * @param host O endereço do servidor (ex: "127.0.0.1").
     * @param porta A porta na qual o servidor está ouvindo.
     * @param objeto O objeto a ser enviado (deve implementar a interface Serializable).
     * @return O objeto recebido como resposta do servidor.
     * @throws IOException Se ocorrer um erro de I/O durante a comunicação.
     * @throws ClassNotFoundException Se a classe do objeto recebido não for encontrada.
     */
    public static Object enviarObjetoReceberObjeto(String host, int porta, Object objeto)
            throws IOException, ClassNotFoundException {
        // Abre um socket e garante que ele seja fechado ao final (try-with-resources)
        try (Socket socket = new Socket(host, porta)) {
            // Define um tempo máximo de 5 segundos para a leitura (evita bloqueio infinito)
            socket.setSoTimeout(5000);

            // Para comunicação com objetos, a ordem de criação dos streams é importante.
            // O cliente deve criar primeiro o ObjectOutputStream e depois o ObjectInputStream,
            // e o servidor deve fazer o oposto, para evitar um deadlock.
            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            saida.flush(); // Garante que o cabeçalho do stream seja enviado
            saida.writeObject(objeto); // Envia o objeto

            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());
            Object resposta = entrada.readObject(); // Lê o objeto da resposta

            // Fecha os streams e o socket (o try-with-resources já faz isso, mas chamadas explícitas não prejudicam)
            entrada.close();
            saida.close();
            return resposta;
        }
    }
}