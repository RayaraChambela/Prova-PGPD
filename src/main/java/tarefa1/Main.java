/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package tarefa1;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CyclicBarrier;

/**
 * Ponto de entrada do programa.
 *
 * Objetivo do design aqui:
 * - Criar 5 garfos e 5 filósofos.
 * - Forçar o início simultâneo (barreira) para aumentar chance de deadlock rápido.
 * - Rodar um detector de deadlock (ThreadMXBean) para evidenciar o problema.
 * - Encerrar após 30s (ou antes se detectar deadlock), conforme enunciado.
 */
public class Main {

    public static void main(String[] args) {
        int n = 5; // 5 filósofos e 5 garfos (fixo no enunciado)

        // Cria os 5 garfos (recursos compartilhados).
        Fork[] forks = new Fork[n];
        for (int i = 0; i < n; i++) forks[i] = new Fork(i);

        // Barreira: faz as 5 threads esperarem e começarem juntas.
        // Isso aumenta MUITO a chance de todos pegarem o garfo esquerdo ao mesmo tempo.
        CyclicBarrier startBarrier = new CyclicBarrier(n, () ->
                Log.info("=== Todos os filósofos prontos. Iniciando disputa pelos garfos ao mesmo tempo ===")
        );

        // Cria e inicia as threads dos filósofos.
        // Convenção aqui: filósofo i usa left=forks[i] e right=forks[(i+1)%n].
        for (int i = 0; i < n; i++) {
            Fork left = forks[i];
            Fork right = forks[(i + 1) % n];

            Thread t = new Thread(
                    new Philosopher(i + 1, left, right, startBarrier),
                    "Philosopher-" + (i + 1)
            );
            t.start();
        }

        // Começa um "watcher" em thread daemon para detectar deadlock e imprimir evidência.
        startDeadlockWatcher();

        // Rodar por 30s e encerrar para cumprir requisito do enunciado.
        // Mesmo que não deadlocke, você entrega a evidência de execução e logs.
        try {
            Thread.sleep(30_000);
            Log.info("Execução concluída (30s). Encerrando o programa.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Encerramento forçado: simplifica (sem join/flags) e garante término.
        System.exit(0);
    }

    /**
     * Thread daemon que checa deadlock periodicamente usando ThreadMXBean.
     * Se encontrar deadlock, imprime ThreadInfo detalhado e encerra.
     */
    private static void startDeadlockWatcher() {
        Thread watcher = new Thread(() -> {
            ThreadMXBean mx = ManagementFactory.getThreadMXBean();

            while (true) {
                // Detecta deadlocks envolvendo monitores (synchronized) e ownable synchronizers.
                long[] ids = mx.findDeadlockedThreads();

                if (ids != null && ids.length > 0) {
                    Log.info("!!! DEADLOCK DETECTADO !!! Threads envolvidas:");

                    // Pede infos completas (locks e stack traces)
                    ThreadInfo[] infos = mx.getThreadInfo(ids, true, true);
                    for (ThreadInfo info : infos) {
                        Log.info(info.toString());
                    }

                    Log.info("Encerrando após evidência de deadlock.");
                    System.exit(0);
                }

                // Intervalo pequeno para detectar rápido e evitar spam.
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    return; // se interromper, só encerra o watcher
                }
            }
        }, "Deadlock-Watcher");

        // Daemon: não impede o encerramento do programa.
        watcher.setDaemon(true);
        watcher.start();
    }
}

