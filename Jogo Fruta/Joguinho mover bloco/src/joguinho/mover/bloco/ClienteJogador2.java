package joguinho.mover.bloco;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * @author vitin
 */
public class ClienteJogador2 extends javax.swing.JFrame {

private Comunicador comunicador;

    public ClienteJogador2() {
        initComponents();
        configurarJanela();
        configurarRedeEAutenticar();
        configurarTeclado();
    }
    
    private void configurarJanela() {
        getContentPane().setLayout(null);
        this.setFocusable(true);
        this.requestFocusInWindow();
        GlobalJogo.criarObstaculos(this);
    }
    
    private void configurarRedeEAutenticar() {
        String ip = JOptionPane.showInputDialog(this, "Digite o IP do Servidor:", "localhost");
        if (ip == null || ip.isEmpty()) {
            System.exit(0);
        }
        
        String frutaDigitada = JOptionPane.showInputDialog(this, 
                "Digite a fruta secreta do lobby:", 
                "Autenticação", 
                JOptionPane.QUESTION_MESSAGE);
        
        if (frutaDigitada == null || frutaDigitada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A fruta secreta é obrigatória.");
            System.exit(0);
        }
        
        comunicador = new Comunicador();
        if (comunicador.conectarCliente(ip, GlobalJogo.PORTA)) {
            
            // --- ETAPA DE AUTENTICAÇÃO ---
            
            // Envia a fruta (String) como primeiro objeto
            comunicador.enviarObjeto(frutaDigitada);
            
            Object respostaObj = comunicador.receberObjeto();
            
            if (respostaObj instanceof String respostaAuth && respostaAuth.equals("AUTENTICADO_OK")) {
                JOptionPane.showMessageDialog(this, "Autenticado com sucesso!");
                iniciarListenerServidor();
                
            } else {
                JOptionPane.showMessageDialog(this, "Autenticação falhou! Fruta errada ou servidor rejeitou.");
                comunicador.fechar();
                System.exit(1);
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao conectar ao servidor.");
            System.exit(1);
        }
    }
    
    private void iniciarListenerServidor() {
        new Thread(() -> {
            while (true) {
                // Recebe um Objeto 
                Object estadoObj = comunicador.receberObjeto(); 
                if (estadoObj == null) {
                    JOptionPane.showMessageDialog(this, "Servidor desconectado.");
                    comunicador.fechar();
                    System.exit(0); 
                    break;
                }
                
                // Verifica se o objeto é do tipo 'EstadoJogo'
                if (estadoObj instanceof EstadoJogo estadoJogo) {
                    processarEstado(estadoJogo);
                }
            }
        }).start();
    }
    
    /**
     * Atualiza a tela com base no Objeto EstadoJogo
     */
    private void processarEstado(EstadoJogo estado) {
        try {
            // Atualizações de tela devem rodar na thread do Swing
            SwingUtilities.invokeLater(() -> {
                
                lbl_pontosBranco.setText("Branco: " + estado.pontosBranco);
                lbl_pontosVermelho.setText("Vermelho: " + estado.pontosVermelho);

                // Limpa os obstáculos antigos
                GlobalJogo.clearObstaculos(this);
                
                // Processa todos os componentes (Players, Fruta, Obstáculos)
                for (GlobalJogo comp : estado.componentes) {
                    switch (comp.tipo) {
                        case GlobalJogo.JOGADOR1:
                            btn_ghost1.setLocation(comp.x, comp.y);
                            break;
                        case GlobalJogo.JOGADOR2:
                            btn_ghost2.setLocation(comp.x, comp.y);
                            break;
                        case GlobalJogo.FRUTA:
                            btn_frutinha.setLocation(comp.x, comp.y);
                            break;
                        case GlobalJogo.OBSTACULO:
                            // Cria e adiciona o novo obstáculo
                            JButton newObs = new JButton();
                            newObs.setBackground(java.awt.Color.DARK_GRAY);
                            newObs.setBounds(comp.x, comp.y, comp.largura, comp.altura);
                            newObs.setFocusable(false);
                            GlobalJogo.addObstaculo(newObs, this);
                            break;
                    }
                }
                
                this.repaint();
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarTeclado() {
        this.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                // Envia a String de input como um Objeto
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_W -> comunicador.enviarObjeto("W_PRESS");
                    case java.awt.event.KeyEvent.VK_S -> comunicador.enviarObjeto("S_PRESS");
                    case java.awt.event.KeyEvent.VK_A -> comunicador.enviarObjeto("A_PRESS");
                    case java.awt.event.KeyEvent.VK_D -> comunicador.enviarObjeto("D_PRESS");
                }
            }
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_W -> comunicador.enviarObjeto("W_RELEASE");
                    case java.awt.event.KeyEvent.VK_S -> comunicador.enviarObjeto("S_RELEASE");
                    case java.awt.event.KeyEvent.VK_A -> comunicador.enviarObjeto("A_RELEASE");
                    case java.awt.event.KeyEvent.VK_D -> comunicador.enviarObjeto("D_RELEASE");
                }
            }
        });
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btn_ghost1 = new javax.swing.JButton();
        btn_ghost2 = new javax.swing.JButton();
        btn_frutinha = new javax.swing.JButton();
        lbl_pontosBranco = new javax.swing.JLabel();
        lbl_pontosVermelho = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_ghost1.setBackground(new java.awt.Color(102, 102, 102));
        btn_ghost1.setForeground(new java.awt.Color(255, 51, 51));
        btn_ghost1.setText("👻");
        btn_ghost1.setFocusable(false);

        btn_ghost2.setBackground(new java.awt.Color(102, 102, 102));
        btn_ghost2.setForeground(new java.awt.Color(0, 153, 255));
        btn_ghost2.setText("👻");
        btn_ghost2.setFocusable(false);

        btn_frutinha.setBackground(new java.awt.Color(204, 0, 204));
        btn_frutinha.setForeground(new java.awt.Color(255, 255, 255));
        btn_frutinha.setText("🍇");
        btn_frutinha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_frutinhaKeyPressed(evt);
            }
        });

        lbl_pontosBranco.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_pontosBranco.setForeground(new java.awt.Color(255, 51, 51));
        lbl_pontosBranco.setText("Time 01");

        lbl_pontosVermelho.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lbl_pontosVermelho.setForeground(new java.awt.Color(0, 153, 255));
        lbl_pontosVermelho.setText("Time 02");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(btn_frutinha, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(lbl_pontosBranco, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_pontosVermelho, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btn_ghost1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_ghost2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(95, 95, 95))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_pontosBranco, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_pontosVermelho, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ghost2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_ghost1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 282, Short.MAX_VALUE)
                .addComponent(btn_frutinha, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_frutinhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_frutinhaKeyPressed

    }//GEN-LAST:event_btn_frutinhaKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClienteJogador2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClienteJogador2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClienteJogador2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClienteJogador2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClienteJogador2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_frutinha;
    private javax.swing.JButton btn_ghost1;
    private javax.swing.JButton btn_ghost2;
    private javax.swing.JLabel lbl_pontosBranco;
    private javax.swing.JLabel lbl_pontosVermelho;
    // End of variables declaration//GEN-END:variables
}
