
/*
 * classe responsavel por movimentar componentes e botoes para cima, baixo, esquerda e direita
 * 
 */
package jogo;

import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;

public class Movimenta {
    private JButton botao;
    private int dx, dy;
    private int velocidade = 10;

    public Movimenta(JButton botao) {
        this.botao = botao;
    }

    public void definirDirecao(int keyCode) {
        switch (keyCode) {
            case java.awt.event.KeyEvent.VK_UP:
                dx = 0; dy = -velocidade;
                break;
            case java.awt.event.KeyEvent.VK_DOWN:
                dx = 0; dy = velocidade;
                break;
            case java.awt.event.KeyEvent.VK_LEFT:
                dx = -velocidade; dy = 0;
                break;
            case java.awt.event.KeyEvent.VK_RIGHT:
                dx = velocidade; dy = 0;
                break;
        }
    }

    public void atualizarPosicao() {
        int novoX = botao.getX() + dx;
        int novoY = botao.getY() + dy;
        if (novoX >= 0 && novoX <= 780 && novoY >= 0 && novoY <= 580) {
            botao.setBounds(novoX, novoY, botao.getWidth(), botao.getHeight());
        }
    }

    public static void cima(JButton botao) {
        if (botao.getY() > 0) {
            botao.setBounds(botao.getX(), botao.getY() - 10, botao.getWidth(), botao.getHeight());
        }
    }

    public static void baixo(JButton botao) {
        if (botao.getY() < 580) {
            botao.setBounds(botao.getX(), botao.getY() + 10, botao.getWidth(), botao.getHeight());
        }
    }

    public static void baixo(JButton botao, JFrame frame) {
        if (botao.getY() + botao.getHeight() < frame.getHeight()) {
            botao.setBounds(botao.getX(), botao.getY() + 10, botao.getWidth(), botao.getHeight());
        }
    }

    public static void esquerda(JButton botao) {
        if (botao.getX() > 0) {
            botao.setBounds(botao.getX() - 10, botao.getY(), botao.getWidth(), botao.getHeight());
        }
    }

    public static void direita(JButton botao, JFrame frame) {
        if (botao.getX() + botao.getWidth() < frame.getWidth()) {
            botao.setBounds(botao.getX() + 10, botao.getY(), botao.getWidth(), botao.getHeight());
        }
    }

    // ===== Sorteia nova posição para a fruta =====
    public static void sorteiaPosicao(JButton botao, JFrame frame) {
        Random random = new Random();
        int maxX = frame.getWidth() - botao.getWidth();
        int maxY = frame.getHeight() - botao.getHeight();

        int novoX = random.nextInt(Math.max(maxX, 1));
        int novoY = random.nextInt(Math.max(maxY, 1));

        botao.setBounds(novoX, novoY, botao.getWidth(), botao.getHeight());
    }
}