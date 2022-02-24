package ru.netology;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private DataGenerator() {
    }
    private static Faker faker = new Faker(new Locale("en"));

        private static RequestSpecification requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(9999)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        static void registrationRequest(Registration registration) {
            // сам запрос
            given() // "дано"
                    .spec(requestSpec) // указываем, какую спецификацию используем
                    .body(registration) // передаём в теле объект, который будет преобразован в JSON
                    .when() // "когда"
                    .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                    .then() // "тогда ожидаем"
                    .statusCode(200); // код 200 OK
        }

    public static String generateLogin() {
        String login = faker.name().username();
        return login;
    }

    public static String generatePassword() {
        String password = faker.internet().password(false);
        return password;
    }

public static Registration generateUser(String status) {
    Registration registration = new Registration(generateLogin(), generatePassword(), status);
    registrationRequest(registration);
    return registration;
}

    public static Registration generateInvalidPassword(String status) {
        String login = generateLogin();
        registrationRequest(new Registration(login, generatePassword(), status));
        return new Registration(login, generatePassword(), status);
    }

    public static Registration generateInvalidLogin(String status) {
        String password = generatePassword();
        registrationRequest(new Registration(generateLogin(), password, status));
        return new Registration(generateLogin(), password, status);
    }
}
