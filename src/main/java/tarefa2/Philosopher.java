/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa2;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Filósofo = thread que alterna pensar/comer.
 *
 * Diferença crítica da Tarefa 1:
 * - 1 filósofo (id == 4) inverte a ordem de pegar garfos:
 *   pega DIREITO depois ESQUERDO.
 * - Os outros continuam ESQUERDO depois DIREITO.
 *
 * Isso quebra a espera circular (condição necessária do deadlock).
 */
public class Philosopher implements Runnable {

    private final int id;
    private final Fork left;
    private final Fork right;

    private final Random random = new Random();

    // Flag de execução compartilhada
    private final AtomicBoolean running;

    // Vetor de contadores compartilhado entre threads
    private final AtomicIntegerArray eatCounts;

    public Philosopher(int id, Fork left, Fork right, AtomicBoolean running, AtomicIntegerArray eatCounts) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.running = running;
        this.eatCounts = eatCounts;
    }

    @Override
    public void run() {
        // Loop controlado pelo AtomicBoolean (rodar por 2 min)
        while (running.get()) {
            think();
            eat();
        }
        Log.info("Philosopher " + id + " encerrando.");
    }

    private void think() {
        Log.info("Philosopher " + id + " começou a pensar.");
        sleepRandom(1000, 3000); // 1 a 3 segundos conforme enunciado
    }

    private void eat() {
        // Regra da tarefa:
        // - filósofo 4 pega DIREITO -> ESQUERDO (inverso)
        // - os outros pegam ESQUERDO -> DIREITO
        if (id == 4) {
            pickAndEat(right, "DIREITO", left, "ESQUERDO");
        } else {
            pickAndEat(left, "ESQUERDO", right, "DIREITO");
        }
    }

    /**
     * Método genérico para pegar 2 garfos em uma ordem definida.
     *
     * Design:
     * - Evita duplicar código (um caso para cada ordem).
     * - Mantém o logging completo (tentativa, sucesso, comer, soltar).
     */
    private void pickAndEat(Fork first, String firstLabel, Fork second, String secondLabel) {

        Log.info("Philosopher " + id + " tenta pegar o garfo " + firstLabel + " (" + first + ").");
        synchronized (first) {
            Log.info("Philosopher " + id + " pegou o garfo " + firstLabel + " (" + first + ").");

            Log.info("Philosopher " + id + " tenta pegar o garfo " + secondLabel + " (" + second + ").");
            synchronized (second) {
                Log.info("Philosopher " + id + " pegou AMBOS os garfos e começou a comer.");

                // Estatística exigida: conta refeições.
                // id-1 porque array é 0..4 e filósofos são 1..5.
                eatCounts.incrementAndGet(id - 1);

                sleepRandom(1000, 3000);
                Log.info("Philosopher " + id + " terminou de comer e vai soltar os garfos.");
            } // libera second
        } // libera first
    }

    private void sleepRandom(int minMs, int maxMs) {
        try {
            Thread.sleep(minMs + random.nextInt(maxMs - minMs + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}


