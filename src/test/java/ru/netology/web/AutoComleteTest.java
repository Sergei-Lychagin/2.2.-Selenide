package ru.netology.web;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class AutoComleteTest {
    @Test
    void shouldFillCorrectRegisterBySearching() {
        open("http://localhost:9999");
        $("[data-test-id='city'] .input__control").setValue("ба");
        $(".input__menu").sendKeys(Keys.chord(Keys.DOWN, Keys.DOWN, Keys.ENTER));
        $$(".input__box").find(exactText("Барнаул"));

        $("[data-test-id='date'] .input__control").click();

        LocalDate date = LocalDate.now();
        int yearNow = date.getYear();
        int monthNow = date.getMonthValue();

        LocalDate dateSearch = LocalDate.of(2021, 6, 20);
        int yearSearch = dateSearch.getYear() - yearNow;
        int monthSearch = dateSearch.getMonthValue();
        int daySearch = dateSearch.getDayOfMonth();

        for (int i = 0; i < yearSearch; i++) {
            $("[data-step='12']").click();
        }
        if (monthSearch > monthNow) {
            for (int i = 0; i < monthSearch - monthNow; i++) {
                $("[data-step='1']").click();
            }
        } else {
            for (int i = 0; i < monthNow - monthSearch; i++) {
                $("[data-step='-1']").click();
            }
        }
        $$(".calendar__day").get(daySearch).click();
        $("[data-test-id='date'] .input__control").find(byText("20.06.2021"));
        $("[data-test-id=name] input").setValue("Дмитрий Петров-Водкин");
        $("[data-test-id=phone] input").setValue("+79865432098");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofMillis(11000));
        $(".notification__title").shouldBe(visible, Duration.ofMillis(11000));
        $(".notification__content").shouldBe(text("Встреча успешно забронирована на " + "20.06.2021"), visible);


    }

}
