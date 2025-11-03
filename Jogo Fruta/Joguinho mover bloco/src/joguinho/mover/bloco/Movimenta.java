package joguinho.mover.bloco;

import java.awt.Rectangle;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author vitin
 */
public class Movimenta {
    
    private static final Random gerador = new Random();
    
    public static void cima(JButton botao, JFrame frame) {
        int x = botao.getX();
        int y = botao.getY() - GlobalJogo.PASSO;
        Rectangle novaPos = new Rectangle(x, y, botao.getWidth(), botao.getHeight());

        if (!GlobalJogo.verificarColisao(novaPos, frame)) {
            botao.setLocation(x, y);
        }
    }

    public static void baixo(JButton botao, JFrame frame) {
        int x = botao.getX();
        int y = botao.getY() + GlobalJogo.PASSO;
        Rectangle novaPos = new Rectangle(x, y, botao.getWidth(), botao.getHeight());

        if (!GlobalJogo.verificarColisao(novaPos, frame)) {
            botao.setLocation(x, y);
        }
    }

    public static void esquerda(JButton botao, JFrame frame) {
        int x = botao.getX() - GlobalJogo.PASSO;
        int y = botao.getY();
        Rectangle novaPos = new Rectangle(x, y, botao.getWidth(), botao.getHeight());

        if (!GlobalJogo.verificarColisao(novaPos, frame)) {
            botao.setLocation(x, y);
        }
    }

    public static void direita(JButton botao, JFrame frame) {
        int x = botao.getX() + GlobalJogo.PASSO;
        int y = botao.getY();
        Rectangle novaPos = new Rectangle(x, y, botao.getWidth(), botao.getHeight());

        if (!GlobalJogo.verificarColisao(novaPos, frame)) {
            botao.setLocation(x, y);
        }
    }

    public static void sorteiaPosicao(JButton botao, JFrame frame) {
        Rectangle limites = frame.getContentPane().getBounds();
        Rectangle posFruta = new Rectangle(botao.getWidth(), botao.getHeight());

        do {
            posFruta.x = gerador.nextInt(limites.width - botao.getWidth());
            posFruta.y = gerador.nextInt(limites.height - botao.getHeight() - 80) + 80; 
            
        } while (GlobalJogo.verificarColisao(posFruta, frame)); 

        botao.setLocation(posFruta.x, posFruta.y);
    }
}