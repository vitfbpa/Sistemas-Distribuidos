/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jogo;

public class ThreadJogo implements Runnable {
    private Movimenta movimenta;
    private boolean rodando = true;

    public ThreadJogo(Movimenta movimenta) {
        this.movimenta = movimenta;
    }

    public void parar() {
        rodando = false;
    }

    @Override
    public void run() {
        while (rodando) {
            movimenta.atualizarPosicao();
            try {
                java.lang.Thread.sleep(100); // evita conflito com o nome da classe
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}