/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa4;

import java.util.concurrent.CyclicBarrier;

public class Main {

    public static void main(String[] args) throws Exception {
        final int N = 5; // Número de filósofos (e garfos)
        final long durationMs = 5 * 60 * 1000L; // 2 minutos
        final long endTime = System.currentTimeMillis() + durationMs;

        // Instancia a mesa (monitor que controla os garfos e a fila)
        Mesa mesa = new Mesa(N);

        // Barreira para iniciar todos os filósofos ao mesmo tempo
        CyclicBarrier barrier = new CyclicBarrier(N, () ->
                Log.info("=== Todos prontos. Iniciando execução por 2 minutos ===")
        );

        // Threads para os filósofos
        Thread[] threads = new Thread[N];

        Log.info("Execução iniciada. Rodando por 2 minutos...");

        // Cria e inicia as threads para os filósofos
        for (int i = 0; i < N; i++) {
            Philosopher p = new Philosopher(i, mesa, barrier, endTime);
            threads[i] = new Thread(p, "Philosopher-" + i);
            threads[i].start();
        }

        // Espera até o deadline
        while (System.currentTimeMillis() < endTime) {
            Thread.sleep(250);
        }

        // Interrompe todas as threads
        for (Thread t : threads) t.interrupt();
        for (Thread t : threads) t.join(2000);

        // Imprime as estatísticas de alimentação
        Log.info("Execução concluída. Estatísticas de alimentação:");
        for (int i = 0; i < N; i++) {
            Log.info("Philosopher " + i + " comeu " + mesa.getVezesComeu(i) + " vez(es).");
        }
    }
}

