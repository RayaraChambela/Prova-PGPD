/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Main da Tarefa 2.
 *
 * Objetivo:
 * - Rodar por 2 minutos sem deadlock.
 * - Coletar estatística: quantas vezes cada filósofo comeu.
 * - Encerrar de forma controlada (sem System.exit no meio por deadlock).
 */
public class Main {

    public static void main(String[] args) {
        int n = 5;

        // Cria os garfos (monitores compartilhados)
        Fork[] forks = new Fork[n];
        for (int i = 0; i < n; i++) forks[i] = new Fork(i);

        // Flag de execução:
        // - AtomicBoolean garante visibilidade entre threads (sem precisar synchronized/volatile manual).
        AtomicBoolean running = new AtomicBoolean(true);

        // Contador por filósofo:
        // - AtomicIntegerArray permite incrementos thread-safe sem lock extra.
        // - índice 0..4 corresponde a filósofos 1..5.
        AtomicIntegerArray eatCounts = new AtomicIntegerArray(n);

        // Guardar threads para dar join depois
        List<Thread> threads = new ArrayList<>();

        // Cria e inicia os 5 filósofos
        for (int i = 0; i < n; i++) {
            int id = i + 1;
            Fork left = forks[i];
            Fork right = forks[(i + 1) % n];

            Philosopher p = new Philosopher(id, left, right, running, eatCounts);

            Thread t = new Thread(p, "Philosopher-" + id);
            threads.add(t);
            t.start();
        }

        Log.info("Execução iniciada (Tarefa 2). Rodando por 2 minutos sem deadlock...");

        // Tempo de execução do teste (2 minutos)
        try {
            Thread.sleep(300_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Sinaliza parada para todas as threads
        running.set(false);

        // Espera um pouco cada thread encerrar (evita travar o main eternamente)
        for (Thread t : threads) {
            try {
                t.join(2_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Imprime estatísticas pedidas no enunciado
        Log.info("Execução concluída (2 min). Estatísticas de alimentação:");
        for (int i = 0; i < n; i++) {
            Log.info("Philosopher " + (i + 1) + " comeu " + eatCounts.get(i) + " vez(es).");
        }

        System.exit(0);
    }
}


