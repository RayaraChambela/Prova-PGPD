package tarefa1;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * Filósofo = uma Thread (Runnable).
 *
 * Design:
 * - Loop infinito (conforme enunciado), alternando "comer" e "pensar".
 * - Ordem de lock: SEMPRE pega o ESQUERDO e depois o DIREITO.
 *   Isso é proposital porque essa simetria é a causa clássica do deadlock.
 * - Delays controlados para aumentar chance do deadlock acontecer cedo.
 */
public class Philosopher implements Runnable {

    private final int id;
    private final Fork left;
    private final Fork right;

    // Random por thread (simples). Não precisa ser compartilhado.
    private final Random random = new Random();

    // Barreira de início simultâneo.
    private final CyclicBarrier startBarrier;

    public Philosopher(int id, Fork left, Fork right, CyclicBarrier startBarrier) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.startBarrier = startBarrier;
    }

    @Override
    public void run() {
        try {
            // Todos começam exatamente ao mesmo tempo, aumentando disputa.
            startBarrier.await();
        } catch (Exception e) {
            // Se algo der errado na barreira (interrupção, broken barrier),
            // encerra esta thread para não deixar estado inconsistente.
            return;
        }

        // Loop infinito conforme enunciado.
        // Você inverteu a ordem: come primeiro, depois pensa.
        // Isso foi uma decisão consciente pra forçar disputa logo no início.
        while (true) {
            eat();     // força tentativa de pegar garfos
            think();   // depois simula pensamento
        }
    }

    /**
     * Simula o filósofo pensando.
     * Obs: seu enunciado dizia 1 a 3 segundos; aqui está ~0.8 a 1.2s.
     * Se você quiser aderência estrita ao enunciado, muda para 1000..3000.
     */
    private void think() {
        Log.info("Philosopher " + id + " começou a pensar.");
        sleepRandom(800, 1200);
    }

    /**
     * Simula o filósofo comendo:
     * - tenta pegar o garfo esquerdo (lock no objeto left)
     * - segura um tempo (pra todo mundo "prender" o esquerdo)
     * - tenta pegar o garfo direito (lock no objeto right)
     *
     * Se todo mundo pegar o esquerdo ao mesmo tempo,
     * ninguém consegue o direito => deadlock (ciclo de espera).
     */
    private void eat() {
        Log.info("Philosopher " + id + " tenta pegar o garfo ESQUERDO (" + left + ").");

        synchronized (left) {
            Log.info("Philosopher " + id + " pegou o garfo ESQUERDO (" + left + ").");

            // Delay grande proposital:
            // aumenta chance de todos segurarem o esquerdo simultaneamente.
            sleepRandom(800, 1200);

            Log.info("Philosopher " + id + " tenta pegar o garfo DIREITO (" + right + ").");

            synchronized (right) {
                Log.info("Philosopher " + id + " pegou AMBOS os garfos e começou a comer.");

                // Tempo de comer (aqui está aderente ao enunciado: 1 a 3s).
                sleepRandom(1000, 3000);

                Log.info("Philosopher " + id + " terminou de comer e vai soltar os garfos.");
            } // sai do synchronized(right) => solta o direito
        } // sai do synchronized(left) => solta o esquerdo
    }

    /**
     * Helper pra dormir com tempo aleatório.
     */
    private void sleepRandom(int minMs, int maxMs) {
        try {
            Thread.sleep(minMs + random.nextInt(maxMs - minMs + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

