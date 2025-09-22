/**
 * Esta classe representa a interface gráfica (GUI) do cliente TCP.
 * O nome da classe "ServidorTCP" é enganoso, pois sua função é de cliente.
 * 
 * A interface permite que o usuário digite um nome, envie para o servidor
 * e veja o resultado (o e-mail gerado ou uma mensagem de status).
 */
package pessoa;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.*;

public class ServidorTCP extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ServidorTCP.class.getName());
    private final String host = "127.0.0.1";
    private final int porta = 50000;

    /**
     * Construtor da classe. Apenas inicializa os componentes da GUI.
     */
    public ServidorTCP() {
        initComponents();
    }
    
    /**
     * Este método é chamado de "Generated Code" pelo NetBeans e não deve ser
     * modificado manualmente. Ele contém a inicialização e o layout de todos
     * os componentes da interface gráfica.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();
        lblMenu = new javax.swing.JLabel();
        txtResultado = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblNome.setText("Nome:");

        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        lblMenu.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblMenu.setText("MENU");

        txtResultado.setEditable(false);
        txtResultado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtResultadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(198, 198, 198)
                        .addComponent(lblMenu)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblNome)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtNome))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblMenu)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNome, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Método principal que lida com a lógica de envio de dados para o servidor.
     * Ele é executado em uma nova Thread para não travar a interface gráfica
     * durante a comunicação de rede.
     */
    private void enviar() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite um nome.", "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Limpa o campo de texto do nome para a próxima entrada
        txtNome.setText("");

        btnEnviar.setEnabled(false);
        txtResultado.setText("Conectando a " + host + ":" + porta + "...");

        // A comunicação de rede é feita em uma thread separada para não bloquear a GUI
        new Thread(() -> {
            try {
                // Usa o Comunicador para enviar o nome (String) e receber um objeto
                Object resp = Comunicador.enviarObjetoReceberObjeto(host, porta, nome);

                // Atualiza a GUI na thread de eventos do Swing
                SwingUtilities.invokeLater(() -> {
                    if (resp == null) {
                        txtResultado.setText("Servidor respondeu: nome já está na lista (email já gerado).");
                    } else if (resp instanceof Pessoa p) {
                        // Em caso de sucesso, exibe apenas o e-mail
                        txtResultado.setText(p.getEmail());
                    } else {
                        txtResultado.setText("Resposta inesperada: " + resp);
                    }
                    btnEnviar.setEnabled(true); // Reabilita o botão
                });

            } catch (Exception ex) {
                // Em caso de erro na comunicação, exibe a mensagem de erro
                SwingUtilities.invokeLater(() -> {
                    txtResultado.setText("Erro: " + ex.getMessage());
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                    btnEnviar.setEnabled(true); // Reabilita o botão
                });
            }
        }, "Cliente-GUI").start();
    }
    
    /**
     * Ação executada quando o usuário pressiona Enter no campo de nome.
     * Apenas chama o método enviar().
     * @param evt O evento de ação (não utilizado).
     */
    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        enviar();
    }//GEN-LAST:event_txtNomeActionPerformed

    /**
     * Ação executada quando o usuário clica no botão "Enviar".
     * Apenas chama o método enviar().
     * @param evt O evento de ação (não utilizado).
     */
    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        enviar();
    }//GEN-LAST:event_btnEnviarActionPerformed

    /**
     * Ação para o campo de resultado. Nenhuma ação é necessária aqui, pois
     * este campo é apenas para exibição.
     * @param evt O evento de ação (não utilizado).
     */
    private void txtResultadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtResultadoActionPerformed
        // Este campo é para exibição, nenhuma ação é necessária.
    }//GEN-LAST:event_txtResultadoActionPerformed

    /**
     * Método principal que inicia a aplicação.
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new ServidorTCP().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JLabel lblMenu;
    private javax.swing.JLabel lblNome;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtResultado;
    // End of variables declaration//GEN-END:variables
}
