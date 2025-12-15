/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa4;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * A classe Mesa atua como o monitor que gerencia os garfos e controla o acesso.
 * Também implementa a fila para garantir fairness.
 */
public class Mesa {

    private final boolean[] garfoLivre;  // Marca se o garfo está livre ou ocupado
    private final Queue<Integer> fila = new ArrayDeque<>();  // Fila de espera (garante fairness FIFO)
    private final int[] vezesComeu;  // Contador de refeições de cada filósofo

    public Mesa(int nFilosofos) {
        this.garfoLivre = new boolean[nFilosofos];
        this.vezesComeu = new int[nFilosofos];
        for (int i = 0; i < nFilosofos; i++) garfoLivre[i] = true;
    }

    private int leftFork(int id) {
        return id; // garfo esquerdo
    }

    private int rightFork(int id) {
        return (id + 1) % garfoLivre.length; // garfo direito
    }

    private boolean forksAvailable(int id) {
        // Verifica se ambos os garfos (esquerdo e direito) estão livres
        int l = leftFork(id);
        int r = rightFork(id);
        return garfoLivre[l] && garfoLivre[r];
    }

    public synchronized void pegarGarfos(int id) throws InterruptedException {
        // Adiciona o filósofo na fila para garantir a ordem de acesso
        if (!fila.contains(id)) {
            fila.add(id);
            Log.info("Philosopher " + id + " entrou na FILA.");
        }

        Log.info("Philosopher " + id + " aguardando os garfos livres.");

        // Espera até que o filósofo seja o primeiro na fila e tenha os garfos livres
        while (fila.peek() != id || !forksAvailable(id)) {
            wait();
        }

        // Reserva os garfos para o filósofo
        int l = leftFork(id);
        int r = rightFork(id);
        garfoLivre[l] = false;
        garfoLivre[r] = false;

        fila.poll(); // Retira o filósofo da fila após pegar os garfos

        Log.info("Philosopher " + id + " pegou os garfos e vai comer.");
    }

    public synchronized void soltarGarfos(int id) {
        int l = leftFork(id);
        int r = rightFork(id);

        garfoLivre[l] = true;
        garfoLivre[r] = true;

        vezesComeu[id]++; // Incrementa o contador de refeições

        Log.info("Philosopher " + id + " soltou os garfos.");

        // Notifica todos os filósofos esperando para verificar se estão prontos para comer
        notifyAll();
    }

    public synchronized int getVezesComeu(int id) {
        return vezesComeu[id];
    }
}


