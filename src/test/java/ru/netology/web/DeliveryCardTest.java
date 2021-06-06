package ru.netology.web;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryCardTest {

    public static String getDate() {
        LocalDate date = LocalDate.now().plusDays(3);
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
    public String date = getDate();
    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999");
    }

    @Test
    void shouldFillCorrectRegiste() {

        $("[data-test-id=city] input").setValue("Воронеж");
        $(".menu-item_type_block").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id='date'] .input__control").setValue(date);
        $("[data-test-id=name] input").setValue("Дмитрий Петров-Водкин");
        $("[data-test-id=phone] input").setValue("+79865432098");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofMillis(11000));
        $(".notification__content").shouldBe(text("Встреча успешно забронирована на "+"09.06.2021"), visible);


    }

    @Test
    void shouldSendEmptyForm() {

        $(byText("Забронировать")).click();
        $(byText("Поле обязательно для заполнения")).shouldBe(visible);
    }

    @Test
    void shouldWrongCity() {

        $("[data-test-id='city'] .input__control").setValue("Париж");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id='date'] .input__control").setValue(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Николай");
        $("[data-test-id='phone'] .input__control").setValue("+79998887766");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byText("Доставка в выбранный город недоступна")).shouldBe(visible);
    }

    @Test
    void shouldNoDate() {

        $("[data-test-id='city'] .input__control").setValue("Абакан");
        $(".menu-item_type_block").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id='name'] .input__control").setValue("Петров Николай");
        $("[data-test-id='phone'] .input__control").setValue("+79998887766");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byText("Неверно введена дата")).shouldBe(visible);
    }

    @Test
    void shouldWrongName() {

        $("[data-test-id='city'] .input__control").setValue("Барнаул");
        $(".menu-item_type_block").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id='date'] .input__control").setValue(date);
        $("[data-test-id='name'] .input__control").setValue("456211");
        $("[data-test-id='phone'] .input__control").setValue("+79998887766");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.")).shouldBe(visible);
    }

    @Test
    void shouldwrongPhone() {

        $("[data-test-id='city'] .input__control").setValue("Липецк");
        $(".menu-item_type_block").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id='date'] .input__control").setValue(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Николай");
        $("[data-test-id='phone'] .input__control").setValue("89998887766");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.")).shouldBe(visible);
    }

    @Test
    void shouldNotmarkCheckbox() {

        $("[data-test-id='city'] .input__control").setValue("Краснодар");
        $(".menu-item_type_block").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id='date'] .input__control").setValue(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Николай");
        $("[data-test-id='phone'] .input__control").setValue("+79998887766");
        $(byText("Забронировать")).click();
        $(".input_invalid").shouldHave(text("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }

    @Test
    void shouldClosePopup() {

        $("[data-test-id='city'] .input__control").setValue("Севастополь");
        $(".menu-item_type_block").click();
        $("[data-test-id='date'] .input__control").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
        $("[data-test-id='date'] .input__control").setValue(date);
        $("[data-test-id='name'] .input__control").setValue("Петров Николай");
        $("[data-test-id='phone'] .input__control").setValue("+79998887766");
        $("[data-test-id='agreement']").click();
        $(byText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofMillis(11000));
        $(".notification__closer").click();
        $("[data-test-id='notification']").shouldBe(not(visible));
    }
}

