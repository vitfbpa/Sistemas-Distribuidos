/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

/**
 *
 * @author vitim
 */
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int listas = 5;
        int tamanho = 10;

        Agregador ag = new Agregador();
        List<List<Integer>> listaDeListas = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < listas; i++) listaDeListas.add(PopulaLista.novaLista());
        for (int i = 0; i < listas; i++) {
            Thread t = new PopulaLista(listaDeListas.get(i), tamanho, ag);
            threads.add(t);
            t.start();
        }
        for (Thread t : threads) t.join();

        System.out.println("Media global: " + ag.media());
        for (int i = 0; i < listas; i++) {
            System.out.println("Lista " + (i+1) + ": " + listaDeListas.get(i));
        }
    }
}
