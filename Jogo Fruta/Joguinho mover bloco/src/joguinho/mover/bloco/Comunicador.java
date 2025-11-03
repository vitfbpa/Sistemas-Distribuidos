package joguinho.mover.bloco;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JOptionPane;

/**
 * @author vitin
 */
public class Comunicador {

    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public boolean conectarCliente(String ip, int porta) {
        try {
            this.socket = new Socket(ip, porta);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Cliente conectado ao servidor!");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao servidor:\n" + e.getMessage());
            return false;
        }
    }

    public boolean aguardarCliente(int porta) {
        try {
            this.serverSocket = new ServerSocket(porta);
            System.out.println("Servidor aguardando cliente na porta " + porta + "...");
            this.socket = serverSocket.accept(); 
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Cliente conectado!");
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Erro ao iniciar o servidor:\n" + e.getMessage());
            return false;
        }
    }

    
    public void enviarObjeto(Object obj) {
        try {
            if (out != null) {
                out.writeObject(obj);
                out.reset(); // Limpa o cache para garantir envio de estado atualizado
            }
        } catch (IOException e) {
             System.err.println("Erro ao enviar objeto (conexão pode ter caído): " + e.getMessage());
        }
    }

    
    public Object receberObjeto() {
        try {
            if (in != null) {
                return in.readObject(); // Trava aqui até receber um objeto
            }
        } catch (SocketException | java.io.EOFException e) {
             System.err.println("Conexão perdida: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro ao receber dados (IOException): " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Erro: Classe não encontrada ao desserializar: " + e.getMessage());
        }
        return null; // Retorna null se houver erro ou a conexão fechar
    }

    public void fechar() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            if (serverSocket != null) serverSocket.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }

}
