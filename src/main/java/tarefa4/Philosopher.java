/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa4;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

public class Philosopher implements Runnable {

    private final int id;
    private final Mesa mesa;
    private final Random random = new Random();
    private final CyclicBarrier startBarrier;
    private final long endTimeMs;

    public Philosopher(int id, Mesa mesa, CyclicBarrier startBarrier, long endTimeMs) {
        this.id = id;
        this.mesa = mesa;
        this.startBarrier = startBarrier;
        this.endTimeMs = endTimeMs;
    }

    @Override
    public void run() {
        try {
            startBarrier.await(); // Todos os filósofos começam ao mesmo tempo
        } catch (Exception e) {
            return;
        }

        // Loop de pensamento e alimentação enquanto o tempo permitir
        while (System.currentTimeMillis() < endTimeMs && !Thread.currentThread().isInterrupted()) {
            think();
            eat();
        }

        Log.info("Philosopher " + id + " encerrando.");
    }

    private void think() {
        Log.info("Philosopher " + id + " começou a pensar.");
        sleepRandom(200, 600); // 1 a 3 segundos de pensamento
    }

    private void eat() {
        try {
            Log.info("Philosopher " + id + " pede para comer (Mesa/Monitor).");
            mesa.pegarGarfos(id); // Solicita garfos ao monitor

            // Come
            sleepRandom(250, 800); // 1 a 3 segundos comendo

            mesa.soltarGarfos(id); // Libera os garfos para outros filósofos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void sleepRandom(int minMs, int maxMs) {
        try {
            Thread.sleep(minMs + random.nextInt(maxMs - minMs + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

