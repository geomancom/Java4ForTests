package ru.gb_dz.tests;


import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;
import ru.gb_dz.dto.PositiveJiraResponse;


import static io.restassured.RestAssured.given;
import static ru.gb_dz.Main.*;


public class AuthorizationsTests extends BaseTest{
    static String newKey;



    //3.1 create a task with Basic Auth - Basic-авторизация пройдена успешно
    @Test
    void createTaskWithBasicAuthTest() {
        given(requestSpecificationWithAuth)
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(positiveResponseSpecification201)
        ;


    }

    //3.2.1. create a task without Basic Auth - POST-запрос на создание задачи без авторизации
    @Test
    void createTaskWithoutBasicAuthTest() {
        given(requestSpecificationWithoutAuth)
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(negativeResponseSpecification400)
        ;
    }

    //3.2.2. read the task without Basic Auth - GET-запрос проверка созданной задачи  без авторизации
    @Test
    void readTaskWithoutBasicAuthTest() {
        given(requestSpecificationWithoutAuth)
                .body(GET_BODY)
                .when()
                .get(GET_URL_KEY, key)
                .then()
                .spec(negativeResponseSpecification404)
        ;
    }

    //3.2.2.1 read the task without Basic Auth - GET-запрос проверка созданной задачи  c авторизации по полученному ключу
    @Test
    void readTaskWithBasicAuthTest() {
        newKey = given(requestSpecificationWithAuth)
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(positiveResponseSpecification201)
                .extract()
                .body()
                .as(PositiveJiraResponse.class).getKey()
        ;
        given(requestSpecificationWithAuth)
                .when()
                .get(GET_URL_KEY, newKey)
                .then()
                .spec(positiveResponseSpecification200)
        ;
    }

    //3.3.1 create a task with Basic Auth without pass - Неверные параметры авторизации - не ввести пароль
    // без пароля токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Og==
    // с  паролем токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithoutPassTest() {
        given(requestSpecificationWithoutAuth)
                .header("Authorization", "Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Og==")
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(negativeResponseSpecification401)
        ;

    }

    //3.3.2 create a task with Basic Auth with bad pass - Неверные параметры авторизации - ввести неправильный пароль
    // ошибочный пароль токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg1==
    // верный пароль    токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithBadPassTest() {
        given(requestSpecificationWithoutAuth)
                .header("Authorization", "Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg1==")
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(negativeResponseSpecification401)
        ;
    }

    //3.3.3 create a task with Basic Auth without email - Неверные параметры авторизации - не ввести e-mail
    // без email токен = Basic Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    // с   email токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithoutEmailTest() {
        given(requestSpecificationWithoutAuth)
                .header("Authorization", "Basic Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==")
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(negativeResponseSpecification400)
        ;
    }

    //3.3.4 create a task with Basic Auth with bad Email - Неверные параметры авторизации - ввести неправильный пароль
    // ошибочный Email токен = Basic MW1haWwuZm9yLnRlc3RiYXNlQHlhbmRleC5ydTp3ODNEOFZjYkZBSTFDbmZzVE15SzM0REY=
    // верный Email    токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithBadEmailTest() {
        given(requestSpecificationWithoutAuth)
                .header("Authorization", "Basic MW1haWwuZm9yLnRlc3RiYXNlQHlhbmRleC5ydTp3ODNEOFZjYkZBSTFDbmZzVE15SzM0REY=")
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(negativeResponseSpecification400)
        ;
    }

    //3.4 create a task with Basic Auth with email  but in a different register - Проверка регистронезависимости логина в basic-авторизации
    // ошибочный Email токен = Basic TWFJbC5Gb1IuVGVTdEJhU2VAeUFuRGVYLnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    // верный Email    токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithDifferentRegisterTest() {
        given(requestSpecificationWithoutAuth)
                .header("Authorization", "Basic TWFJbC5Gb1IuVGVTdEJhU2VAeUFuRGVYLnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==")
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(positiveResponseSpecification201);
    }

    //4.1. create a task without "Accept" - Проверка заголовка «Accept»	- Не передаём заголовок
    @Test
    void createTaskWithoutAcceptTest() {
        given(requestSpecificationWithoutAccept)
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(positiveResponseSpecification201);
    }

    //4.3. create a task with «Accept : application/xml» - Проверка заголовка «Accept»	- Передаём заголовок с типом не json
    @Test
    void createTaskWithoutAcceptDifferentXmlTest() {
        given(requestSpecificationWithoutAccept)
                .header("Accept", "application/xml")
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(negativeResponseSpecification406)
        ;
    }
    //4.4. create a task with «Accept : aPPlicATion/JsOn» - Проверка заголовка «Accept»	- Проверка регистронезависимости заголовка Accept aPPlicATion/JsOn
    @Test
    void createTaskWithoutAcceptDifferentRegisterTest() {
        given(requestSpecificationWithoutAccept)
                .header("Accept", "aPPlicATion/JsOn")
                .body(GET_BODY)
                .when()
                .post(GET_URL)
                .then()
                .spec(positiveResponseSpecification201);
    }
}
