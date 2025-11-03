package joguinho.mover.bloco;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * @author vitin
 */
public class GlobalJogo implements Serializable { 

    public static final String[] FRUTAS_SECRETAS = {
        "Maca", "Banana", "Uva", "Laranja", "Pera", "Morango", "Kiwi", "Abacaxi"
    };
    
    public static final int JOGADOR1 = 1;
    public static final int JOGADOR2 = 2;
    public static final int FRUTA = 3;
    public static final int OBSTACULO = 4;
    
    public int x;
    public int y;
    public int largura;
    public int altura;
    public int tipo; 

    public GlobalJogo(int x, int y, int largura, int altura, int tipo) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.tipo = tipo;
    }

    public static final int PORTA = 12345;
    public static final int PASSO = 5;
    public static final int PAUSA_LOOP = 20;

    // 'transient' nao deixa a lista de botoes ser enviada pela rede.
    private static final transient List<JButton> obstaculos = new ArrayList<>();
    private static final transient Random gerador = new Random();

    public static void clearObstaculos(JFrame frame) {
        for (JButton obs : obstaculos) {
            frame.remove(obs); 
        }
        obstaculos.clear(); 
    }

    public static void addObstaculo(JButton obs, JFrame frame) {
        obstaculos.add(obs);
        frame.add(obs);
    }
    
    public static List<JButton> getObstaculos() {
        return obstaculos;
    }

    public static void criarObstaculos(JFrame frame) {
        randomizarObstaculos(frame);
    }

    public static void randomizarObstaculos(JFrame frame) {
        clearObstaculos(frame); 
        
        Rectangle limites = frame.getContentPane().getBounds();
        int numObstaculos = gerador.nextInt(3) + 2; 

        for (int i = 0; i < numObstaculos; i++) {
            JButton parede = new JButton();
            parede.setBackground(Color.DARK_GRAY);
            parede.setFocusable(false);
            
            int w, h;
            if (gerador.nextBoolean()) {
                w = gerador.nextInt(150) + 50;
                h = 20;
            } else {
                w = 20;
                h = gerador.nextInt(150) + 50;
            }
            
            int x = gerador.nextInt(limites.width - w - 40) + 20; 
            int y = gerador.nextInt(limites.height - h - 100) + 80; 
            
            parede.setBounds(x, y, w, h);
            addObstaculo(parede, frame); 
        }
    }

    public static boolean verificarColisao(Rectangle novaPos, JFrame frame) {
        for (JButton obs : getObstaculos()) {
            if (novaPos.intersects(obs.getBounds())) {
                return true;
            }
        }

        Rectangle limitesJanela = frame.getContentPane().getBounds();
        
        if (novaPos.y < 50) { 
             return true;
        }
        if (novaPos.x < 0) {
            return true; 
        }
        
        if (novaPos.x + novaPos.width > limitesJanela.width || 
            novaPos.y + novaPos.height > limitesJanela.height) {
            return true;
        }

        return false;
    }
}