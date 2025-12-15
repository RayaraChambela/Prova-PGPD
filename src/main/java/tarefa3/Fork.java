/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa3;

/**
 * Representa um garfo (recurso compartilhado).
 *
 * Design:
 * - É só um "token" que vira monitor no synchronized.
 * - Não tem estado mutável; o lock é o próprio objeto.
 */
public class Fork {
    private final int id;

    public Fork(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        // Ajuda na leitura do log
        return "Fork-" + id;
    }
}

