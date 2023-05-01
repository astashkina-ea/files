package homework.tests;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@DisplayName("Чтение и проверка сожержимого файлов")
public class ZipTests {

    private static final String FILE_PDF = "Предварительные выводы неутешительны: повышение уровня гражданского \n" +
            "сознания играет определяющее значение для переосмысления \n" +
            "внешнеэкономических политик.";

    private static final String FILE_XLSX = "Тестовый 3";

    private static final String[][] FILE_CSV = new String[][]{
            {"Name", "Age", "City"},
            {"Pasha", "23", "London"},
            {"Masha", "24", "Moscow"}
    };

    private ClassLoader classLoader = ZipTests.class.getClassLoader();

    @DisplayName("Чтение и проверка сожержимого pdf файла")
    @Test
    void zipPdfTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("homework/test123.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("pdf") && !entry.getName().contains("MACOSX")) {
                    PDF pdf = new PDF(zis);
                    Assertions.assertTrue(pdf.text.contains(FILE_PDF));
                }
            }
        }
    }

    @DisplayName("Чтение и проверка сожержимого xlsx файла")
    @Test
    void zipXlsTest() throws Exception {

        try (InputStream is = classLoader.getResourceAsStream("homework/test123.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("xlsx") && !entry.getName().contains("MACOSX")) {
                    XLS xls = new XLS(zis);
                    Assertions.assertEquals(FILE_XLSX,
                            xls.excel.getSheetAt(0)
                                    .getRow(2)
                                    .getCell(1)
                                    .getStringCellValue());
                }
            }
        }
    }

    @DisplayName("Чтение и проверка сожержимого csv файла")
    @Test
    void zipCsvTest() throws Exception {
        try (InputStream is = classLoader.getResourceAsStream("homework/test123.zip")) {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().contains("csv") && !entry.getName().contains("MACOSX")) {
                    CSVReader csvReader = new CSVReader(new InputStreamReader(zis));
                    List<String[]> csv = csvReader.readAll();
                    Assertions.assertArrayEquals(FILE_CSV[1], csv.get(1));
                }
            }
        }
    }
}
