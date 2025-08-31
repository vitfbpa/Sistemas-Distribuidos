/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author vitim
 */
public class Agregador {
    private long soma;
    private long total;

    public synchronized void add(int v) {
        soma += v;
        total++;
    }

    public synchronized double media() {
        return total == 0 ? 0.0 : (double) soma / total;
    }
}