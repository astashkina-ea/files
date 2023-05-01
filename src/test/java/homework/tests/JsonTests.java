package homework.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import homework.pojos.SmartHomePojo;


import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonTests {

    private ClassLoader classLoader = ZipTests.class.getClassLoader();

    @DisplayName("Проверка содержимого json файла")
    @Test
    void jsonTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("homework/sh.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            SmartHomePojo smartHomePojo = new ObjectMapper().readValue(isr, SmartHomePojo.class);
            Assertions.assertEquals(12345, smartHomePojo.getHome().getId());
            Assertions.assertEquals("Мой умный дом", smartHomePojo.getHome().getName());
            Assertions.assertEquals(4321, smartHomePojo.getHome().getDevices().get(0).getId());
            Assertions.assertEquals("Розетка", smartHomePojo.getHome().getDevices().get(0).getName());
            Assertions.assertTrue(smartHomePojo.getHome().getDevices().get(0).isOn());
        }
    }
}
