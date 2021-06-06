package ru.netology.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class AutoComleteTest {

    public static String getDate() {
        LocalDate dateMeeting = LocalDate.now().plusDays(10);
        return dateMeeting.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String dateMeeting = getDate();

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999");
    }

    @Test
    void shouldFillCorrectRegisterBySearching() {

        $("[data-test-id='city'] .input__control").setValue("ба");
        $(".input__menu").sendKeys(Keys.chord(Keys.DOWN, Keys.DOWN, Keys.ENTER));
        $$(".input__box").find(exactText("Барнаул"));

        $("[data-test-id='date'] .input__control").click();

        LocalDate date = LocalDate.now();
        int yearNow = date.getYear();
        int monthNow = date.getMonthValue();

        LocalDate dateSearch = LocalDate.now().plusDays(10);
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
        $("[data-test-id=name] input").setValue("Дмитрий Петров-Водкин");
        $("[data-test-id=phone] input").setValue("+79865432098");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id='notification']").shouldBe(visible, Duration.ofMillis(11000));
        String firstDate = $(".notification__content").getText();
        $(".notification__title").shouldBe(visible);
        assertEquals("Встреча успешно забронирована на " + dateMeeting, firstDate);
        $(".notification__content").shouldBe(text("Встреча успешно забронирована на " + dateMeeting), visible);
     //TODO : Оставил два способа проверки, просто попробовать будет работать или нет.

    }

}
