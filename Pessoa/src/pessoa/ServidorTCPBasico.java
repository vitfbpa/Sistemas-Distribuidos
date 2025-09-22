package pessoa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServidorTCPBasico {

    public static void main(String[] args) {
        try {
            int portaServidor = 50000;
            ServerSocket servidor = new ServerSocket(portaServidor);
            System.out.println("Servidor ouvindo a porta: " + portaServidor);
            Socket cliente;
            ArrayList<Pessoa> lista = new ArrayList<>();
            boolean encontrado;

            while (true) {
                encontrado = false;
                cliente = servidor.accept();

                String enderecoIP = cliente.getInetAddress().getHostAddress();
                int portaCliente = cliente.getPort();
                System.out.println("Cliente conectado no IP: " + enderecoIP);
                System.out.println("Cliente conectado via porta: " + portaCliente);
                DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                String nomePessoa = entrada.readUTF().trim();
                String[] vetorNome = nomePessoa.split("\\s+");
                String email = vetorNome[0] + "." + vetorNome[vetorNome.length - 1] + "@ufn.edu.br";
                nomePessoa = nomePessoa.toUpperCase();
                email = email.toLowerCase();

                Pessoa p = new Pessoa(nomePessoa, email);

                if (lista.contains(p)) {
                    encontrado = true;
                } else {
                    lista.add(p);
                }

                DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());
                if (!encontrado) {
                    saida.writeUTF(email);
                } else {
                    saida.writeUTF("Servidor respondeu: nome já está na lista (email já gerado).");
                }

                saida.close();
                cliente.close();

                System.out.println("Clientes na base....");
                for (Pessoa pessoa : lista) {
                    System.out.println(pessoa);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}