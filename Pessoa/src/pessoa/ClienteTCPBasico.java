package pessoa;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

public class ClienteTCPBasico {
    public static void main(String[] args) {
        try {
            int porta = 50000;
            String nome = JOptionPane.showInputDialog(null, "Nome completo");
            if (nome == null || nome.isBlank()) return;

            Socket cliente = new Socket("127.0.0.1", porta);

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(nome);

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            Pessoa p = (Pessoa) entrada.readObject();

            if (p == null) {
                JOptionPane.showMessageDialog(null, "Seu nome já está na lista com um email gerado");
            } else {
                JOptionPane.showMessageDialog(null, "Pessoa criada e recebida: " + p);
            }
            entrada.close();
            cliente.close();
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
