package ru.netology;

import com.codeborne.selenide.Condition;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static io.restassured.RestAssured.given;

public class AuthTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }
    @Test
    void shouldValidLoginPassword() {
        Registration validUser = DataGenerator.generateUser("active");
        $("[data-test-id=login] input").setValue(validUser.getLogin());
        $("[data-test-id=password] input").setValue(validUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Личный кабинет")).shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldSInvalidPassword() {
        Registration invalidPassword = DataGenerator.generateInvalidPassword("active");
        $("[data-test-id=login] input").setValue(invalidPassword.getLogin());
        $("[data-test-id=password] input").setValue(invalidPassword.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Неверно указан логин или пароль"));
    }

    @Test
    void shouldInvalidLogin() {
        Registration invalidLogin = DataGenerator.generateInvalidLogin("active");
        $("[data-test-id=login] input").setValue(invalidLogin.getLogin());
        $("[data-test-id=password] input").setValue(invalidLogin.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $("[data-test-id=error-notification] .notification__content")
                .shouldHave(text("Неверно указан логин или пароль"));
    }
    @Test
    void shouldBlockedUser() {
        Registration blockedUser = DataGenerator.generateUser("blocked");
        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $$("button").find(exactText("Продолжить")).click();
        $(withText("Пользователь заблокирован")).shouldBe(visible, Duration.ofSeconds(15));
    }

}
