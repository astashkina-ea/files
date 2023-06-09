package lesson.parsing;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import lesson.Human;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;

public class FileParsingTest {
    private ClassLoader cl = FileParsingTest.class.getClassLoader(); //classLoader - механизм кот позволяет читать файлы которые находятся в ресурса

    @Test
    void pdfParseTest() throws Exception {
        Configuration. pageLoadStrategy="eager";
        Selenide.open("https://junit.org/junit5/docs/current/user-guide/");
        File download = $("a[href*='junit-user-guide-5.9.3.pdf']").download();
        PDF pdf = new PDF(download);
        Assertions.assertEquals(
                "Stefan Bechtold, Sam Brannen, Johannes Link, Matthias Merdes, Marc Philipp, Juliette de Rancourt, Christian Stein",
                pdf.author
        );
    }

    @Test
    void xlsParseTest() throws Exception {
        Selenide.open("https://excelvba.ru/programmes/Teachers?ysclid=lfcu77j9j9951587711");
        File download = $("a[href*='teachers.xls']").download();
        XLS xls = new XLS(download);

        Assertions.assertTrue(
                xls.excel.getSheetAt(0)
                        .getRow(3)//строка
                        .getCell(2).getStringCellValue()
                        .startsWith("1. Суммарное количество часов планируемое на штатную по всем разделам плана")
        );
    }

    @Test
    void csvParseTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("lesson/expectedFiles/teachers.csv");
             InputStreamReader isr = new InputStreamReader(is)) { // CSVReader чтение посимвольно
            CSVReader csvReader = new CSVReader(isr); //считывает все
            List<String[]> content = csvReader.readAll(); //лист массивов стрингов
            Assertions.assertArrayEquals(new String[]{"Иванов", "Геометрия"}, content.get(1));
        }
    }

    //сравнение файлов
    @Test
    void filesEqualsTest() throws Exception {
        Selenide.open("https://github.com/astashkina-ea/files/blob/master/src/test/resources/teachers.csv");
        File download = $("#raw-url").download();
        try (InputStream isExpected = cl.getResourceAsStream("lesson/expectedFiles/teachers.csv");
             InputStream downloaded = new FileInputStream(download)) {
            Assertions.assertEquals(
                    new String(isExpected.readAllBytes(), StandardCharsets.UTF_8),
                    new String(downloaded.readAllBytes(), StandardCharsets.UTF_8)
            );
        }
    }

    @Test
    void zipTest() throws Exception {
        try (InputStream is = cl.getResourceAsStream("lesson/sample.zip"); //достаем класслоадером зип файл
             ZipInputStream zs = new ZipInputStream(is)) {
            ZipEntry entry;
            while ((entry = zs.getNextEntry()) != null) {
                //Assertions.assertEquals("1.txt", entry.getName());
                Assertions.assertTrue(entry.getName().contains("1.txt"));
            }
        }
    }

    @Test
    void jsonTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = cl.getResourceAsStream("lesson/human.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            JsonObject jsonObject = gson.fromJson(isr, JsonObject.class);

            Assertions.assertTrue(jsonObject.get("isClever").getAsBoolean());
            Assertions.assertEquals(33, jsonObject.get("age").getAsInt());
        }
    }

    @Test
    void jsonCleverTest() throws Exception {
        Gson gson = new Gson();
        try (InputStream is = cl.getResourceAsStream("lesson/human.json");
             InputStreamReader isr = new InputStreamReader(is)) {
            Human human = gson.fromJson(isr, Human.class);

            Assertions.assertTrue(human.isClever);
            Assertions.assertEquals(33, human.age);
        }
    }
}