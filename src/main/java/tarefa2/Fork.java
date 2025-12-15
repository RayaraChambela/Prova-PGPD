/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa2;

/**
 * Representa um garfo (recurso compartilhado).
 *
 * Design:
 * - Simples e imutável: só tem id, usado para debug/log.
 * - O próprio objeto Fork vira o "monitor" do synchronized.
 *   Ou seja: synchronized(fork) = lock desse garfo.
 */
public class Fork {
    private final int id;

    public Fork(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        // Facilita leitura dos logs
        return "Fork-" + id;
    }
}

