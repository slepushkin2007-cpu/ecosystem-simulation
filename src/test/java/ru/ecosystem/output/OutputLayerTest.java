package ru.ecosystem.output;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.Position;
import ru.ecosystem.model.event.GrassGrowthEvent;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OutputLayerTest {

    @Test
    void testConsolePrinterFormatsEvent() {
        // Перехватываем System.out
        PrintStream originalOut = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOut));

        try {
            ConsolePrinter printer = new ConsolePrinter();
            printer.onEvent(new GrassGrowthEvent(5, new Position(2, 3), 1));

            String output = capturedOut.toString();
            assertTrue(output.contains("[tick 5] Трава +1 в (2,3)"), "Вывод не совпадает с ожидаемым");
        } finally {
            // Обязательно возвращаем System.out обратно!
            System.setOut(originalOut);
        }
    }
}