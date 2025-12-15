/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa2;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger mínimo com timestamp.
 *
 * Design:
 * - Centraliza saída de logs.
 * - Timestamp milissegundos ajuda a visualizar concorrência.
 * - System.out.println suficiente pro trabalho.
 */
public class Log {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static void info(String msg) {
        System.out.println("[" + LocalTime.now().format(FMT) + "] " + msg);
    }
}
