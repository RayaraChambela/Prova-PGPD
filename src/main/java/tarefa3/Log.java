/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa3;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger com timestamp e synchronized.
 *
 * Design:
 * - synchronized no método evita logs "misturados" entre threads.
 * - Timestamp ajuda a evidenciar concorrência.
 */
public class Log {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static synchronized void info(String msg) {
        System.out.println("[" + LocalTime.now().format(FMT) + "] " + msg);
    }
}


