/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarefa4;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger simples, mas eficiente, para mensagens com timestamp.
 * Garante que todas as mensagens sejam registradas com a hora exata da execução.
 * A função synchronized é usada para garantir que múltiplos filósofos não
 * interfiram nas mensagens de log (evita saídas misturadas).
 */
public class Log {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    public static synchronized void info(String msg) {
        // Imprime a mensagem com timestamp (precisão até milissegundos)
        System.out.println("[" + LocalTime.now().format(FMT) + "] " + msg);
    }
}

