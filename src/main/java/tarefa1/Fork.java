/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa1;

/**
 * Representa um garfo (recurso compartilhado).
 *
 * Design:
 * - A classe é propositalmente simples e imutável (id final).
 * - O objeto Fork é usado como "monitor" no synchronized (lock).
 *   Ou seja: synchronized(left) trava esse garfo.
 */
public class Fork {
    private final int id; // identificador do garfo (apenas para log e rastreio)

    public Fork(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        // Ajuda a deixar os logs legíveis: "Fork-0", "Fork-1", etc.
        return "Fork-" + id;
    }
}


