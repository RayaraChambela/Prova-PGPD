/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa3;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Philosopher implements Runnable {

    private final int id;
    private final Fork left;
    private final Fork right;

    /**
     * Semáforo global: limita quantos podem TENTAR pegar garfos ao mesmo tempo.
     * Com N=5 e permits=4, nunca teremos 5 filósofos segurando 1 garfo cada ao mesmo tempo.
     */
    private final Semaphore gatekeeper;

    // Controle de execução (parada)
    private volatile boolean running = true;

    // Estatística thread-safe (incrementos corretos sem lock extra)
    private final AtomicInteger eatCount = new AtomicInteger(0);

    private final Random random = new Random();

    public Philosopher(int id, Fork left, Fork right, Semaphore gatekeeper) {
        this.id = id;
        this.left = left;
        this.right = right;
        this.gatekeeper = gatekeeper;
    }

    public void stop() {
        running = false;
    }

    public int getEatCount() {
        return eatCount.get();
    }

    @Override
    public void run() {
        try {
            while (running) {
                think();
                tryEat();
            }
        } finally {
            // finally garante log de encerramento mesmo se der exception/interrupção
            Log.info("Philosopher " + id + " encerrando.");
        }
    }

    private void think() {
        Log.info("Philosopher " + id + " começou a pensar.");
        sleepRandom(400, 900);
    }

    private void tryEat() {
        boolean permitAcquired = false;

        try {
            Log.info("Philosopher " + id + " tenta pegar permissão do semáforo (gatekeeper).");

            // Limita a 4 concorrentes tentando pegar garfos simultaneamente
            gatekeeper.acquire();
            permitAcquired = true;

            Log.info("Philosopher " + id + " conseguiu permissão do semáforo. Vai tentar pegar garfos.");

            /**
             * Aqui você manteve o padrão ESQUERDO -> DIREITO com synchronized nos garfos.
             * Isso *sozinho* poderia deadlockar, mas o gatekeeper impede o cenário de deadlock clássico.
             */
            Log.info("Philosopher " + id + " tenta pegar o garfo ESQUERDO (" + left + ").");
            synchronized (left) {
                Log.info("Philosopher " + id + " pegou o garfo ESQUERDO (" + left + ").");

                // micro delay só para aumentar chance de disputa real
                sleepRandom(5, 30);

                Log.info("Philosopher " + id + " tenta pegar o garfo DIREITO (" + right + ").");
                synchronized (right) {
                    Log.info("Philosopher " + id + " pegou AMBOS os garfos e começou a comer.");
                    eatCount.incrementAndGet();  // estatística
                    sleepRandom(300, 900);
                    Log.info("Philosopher " + id + " terminou de comer e vai soltar os garfos.");
                }
            }

        } catch (InterruptedException e) {
            // Interrupção pode acontecer durante acquire() ou sleep()
            Thread.currentThread().interrupt();
            running = false;

        } finally {
            /**
             * Libera a permissão do semáforo SEMPRE que tiver adquirido.
             * Esse finally é essencial: se você esquecer release(), você cria “vazamento de permissão”
             * e o programa pode parar por falta de permits.
             */
            if (permitAcquired) {
                gatekeeper.release();
                Log.info("Philosopher " + id + " liberou permissão do semáforo (gatekeeper).");
            }
        }
    }

    private void sleepRandom(int minMs, int maxMs) {
        try {
            Thread.sleep(minMs + random.nextInt(maxMs - minMs + 1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            running = false;
        }
    }
}


