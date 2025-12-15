/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa1;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger simples para imprimir mensagens com timestamp.
 *
 * Design:
 * - Centraliza a saída de log em um único lugar.
 * - Formato HH:mm:ss.SSS ajuda a enxergar concorrência e interleaving.
 * - System.out.println já é suficiente pro exercício (sem dependências externas).
 */
public class Log {
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void info(String msg) {
        // Timestamp + mensagem. Simples e direto.
        System.out.println("[" + LocalTime.now().format(FMT) + "] " + msg);
    }
}

