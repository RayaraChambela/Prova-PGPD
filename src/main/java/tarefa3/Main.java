/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
        int n = 5;

        // Garfos: monitores usados com synchronized (cumpre requisito 3)
        Fork[] forks = new Fork[n];
        for (int i = 0; i < n; i++) forks[i] = new Fork(i);

        /**
         * Semáforo global (gatekeeper):
         * - Permite no máximo N-1 filósofos competindo pelos garfos ao mesmo tempo.
         * - Para N=5, permite 4.
         *
         * O "true" ativa fairness FIFO no semáforo:
         * - Threads adquirem permissão em ordem aproximada de chegada,
         * - reduz chance de starvation no gatekeeper (não zera starvation total do sistema).
         */
        Semaphore gatekeeper = new Semaphore(n - 1, true);

        List<Thread> threads = new ArrayList<>();
        List<Philosopher> philosophers = new ArrayList<>();

        // Cria e inicia os filósofos
        for (int i = 0; i < n; i++) {
            Fork left = forks[i];
            Fork right = forks[(i + 1) % n];

            Philosopher p = new Philosopher(i + 1, left, right, gatekeeper);
            Thread t = new Thread(p, "Philosopher-" + (i + 1));

            philosophers.add(p);
            threads.add(t);
            t.start();
        }

        Log.info("Execução iniciada (Tarefa 3 - Semáforos). Rodando por 2 minutos...");
        sleep(120_000);

        // Parada controlada:
        // 1) sinaliza stop() no filósofo (flag running)
        // 2) interrompe threads (para sair de sleep/acquire mais rápido)
        for (Philosopher p : philosophers) p.stop();
        for (Thread t : threads) t.interrupt();

        // Aguarda finalização (com timeout no join)
        for (Thread t : threads) join(t);

        // Estatística exigida: vezes que cada filósofo comeu
        Log.info("Execução concluída (2 min). Estatísticas de alimentação:");
        for (int i = 0; i < philosophers.size(); i++) {
            Log.info("Philosopher " + (i + 1) + " comeu " + philosophers.get(i).getEatCount() + " vez(es).");
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private static void join(Thread t) {
        try { t.join(2000); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}


