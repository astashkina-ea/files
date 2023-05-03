package lesson;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

//исключение - это объект, с которым можно что то сделать
//есть 2 вида исключения проверяемые и непроверяемые
//непроверяемые - заранее не описаны - RuntimeException, Error
//проверяемые - описаны - все исключенения, которые наследованы от Exception, проверяемые
public class FileDownloadTest {

    //скачивание файлов
    @Test
    void downloadTest() throws Exception {
        Selenide.open("https://github.com/qa-guru/niffler/blob/master/README.md");
//        try catch - механизм перехвата исключения
//        в try выполняется код и если возникает исключение, то переходим в catch
//        в тестах try catch не должно быть, может быть throws
//        try {
//            File download = $("a[href*='/qa-guru/niffler/raw/master/README.md']").download();
//        } catch (FileNotFoundException e) {
//            //throw new RuntimeException(e);
//            e.printStackTrace();
//        }
        File download = $("a[href*='/qa-guru/niffler/raw/master/README.md']").download(); //скачиваем файл и складывем путь до файла в переменную (по умолчанию скачивается в build/downloads)
// download работает только с кнопками сслыками, у которых есть атрибут href, если его нет то необходимо задать в тестовом классе
//        static {
//            Configuration.fileDownload = FileDownloadMode.PROXY;
//        }

        try (InputStream is = new FileInputStream(download)) {
            byte[] bytes = is.readAllBytes(); //все содержимое файла складваем в виде массива байтов
            String fileAsString = new String(bytes, StandardCharsets.UTF_8);
            Assertions.assertTrue(fileAsString.contains("Технологии, использованные в Niffler"));
        }
    }

    //загрузка файла
    //способ который не будет работать в ci
    @Test
    void uploadFile1() {
        Configuration. pageLoadStrategy="eager";
        Selenide.open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").uploadFile(new File("src/test/resources/lesson/png-transparent-cat-animal-lovely-cat.png"));
    }

    //способ который будет работать всегде
    //корень папака ресурсы
    @Test
    void uploadFile2() {
        Configuration. pageLoadStrategy="eager";
        Selenide.open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").uploadFromClasspath("lesson/png-transparent-cat-animal-lovely-cat.png"); //выбор файла
        $("#file-submit").click();
    }

    @Test
    void uploadFile3() {
        Selenide.open("https://fineuploader.com/demos.html");
        $("input[type='file']").uploadFromClasspath("lesson/sample.txt"); //выбор файла
        $$("div.qq-dialog-message-selector")
                .find(text("sample.txt has an invalid extension. Valid extension(s): jpeg, jpg, gif, png.\n")).shouldBe(visible);
    }

    @Test
    void uploadTest() {
        Configuration.pageLoadStrategy = "eager";
        Selenide.open("https://tus.io/demo.html");
        $("input[type='file']").uploadFromClasspath("lesson/png-transparent-cat-animal-lovely-cat.png");
        $("#js-upload-container").shouldHave(text("The upload is complete!"));
    }
}