/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author vitim
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulaLista extends Thread {
    private final List<Integer> lista;
    private final int quantidade;
    private final Agregador ag;
    private final Random rand = new Random();

    public PopulaLista(List<Integer> lista, int quantidade, Agregador ag) {
        this.lista = lista;
        this.quantidade = quantidade;
        this.ag = ag;
    }

    @Override
    public void run() {
        for (int i = 0; i < quantidade; i++) {
            int v = rand.nextInt(100001 - 1000) + 1000;
            lista.add(v);
            ag.add(v);
        }
    }

    public static List<Integer> novaLista() {
        return new ArrayList<>();
    }
}