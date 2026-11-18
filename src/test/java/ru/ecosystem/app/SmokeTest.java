package ru.ecosystem.app;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SmokeTest {

    @Test
    void testMainRunsWithoutErrors() {
        // Перехватываем вывод
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));
        System.setErr(new PrintStream(capturedOut));

        try {
            // Маленькое поле и мало шагов
            String[] args = {"--width", "5", "--height", "5", "--ticks", "3", "--herbivores", "2", "--predators", "1", "--grass", "5"};
            Main.main(args);

            String output = capturedOut.toString();
            assertTrue(output.contains("=== Старт симуляции ==="), "Нет приветствия");
            assertTrue(output.contains("=== Конец симуляции ==="), "Нет завершения");
            
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
    }
}