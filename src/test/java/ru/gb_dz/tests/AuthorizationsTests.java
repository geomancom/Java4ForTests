package ru.gb_dz.tests;


import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;


public class AuthorizationsTests extends BaseTest{

    //3.1 create a task with Basic Auth - Basic-авторизация пройдена успешно
    @Test
    void createTaskWithBasicAuthTest() {
        given()
                .header("Authorization", token)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(201);
    }

    //3.2.1. create a task without Basic Auth - POST-запрос на создание задачи без авторизации
    @Test
    void createTaskWithoutBasicAuthTest() {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(400);
    }

    //3.2.2. read the task without Basic Auth - GET-запрос проверка созданной задачи  без авторизации
    @Test
    void readTaskWithoutBasicAuthTest() {
        given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .get("https://testbase.atlassian.net/rest/api/3/issue/{key}", key)
                .then()
                .statusCode(404);
    }

    //3.3.1 create a task with Basic Auth without pass - Неверные параметры авторизации - не ввести пароль
    // без пароля токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Og==
    // с  паролем токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithoutPassTest() {
        given()
                .header("Authorization", "Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Og==")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(401);
    }

    //3.3.2 create a task with Basic Auth with bad pass - Неверные параметры авторизации - ввести неправильный пароль
    // ошибочный пароль токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg1==
    // верный пароль    токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithBadPassTest() {
        given()
                .header("Authorization", "Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg1==")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(401);
    }

    //3.3.3 create a task with Basic Auth without email - Неверные параметры авторизации - не ввести e-mail
    // без email токен = Basic Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    // с   email токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithoutEmailTest() {
        given()
                .header("Authorization", "Basic Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(400);
    }

    //3.3.4 create a task with Basic Auth with bad Email - Неверные параметры авторизации - ввести неправильный пароль
    // ошибочный Email токен = Basic MW1haWwuZm9yLnRlc3RiYXNlQHlhbmRleC5ydTp3ODNEOFZjYkZBSTFDbmZzVE15SzM0REY=
    // верный Email    токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithBadEmailTest() {
        given()
                .header("Authorization", "Basic MW1haWwuZm9yLnRlc3RiYXNlQHlhbmRleC5ydTp3ODNEOFZjYkZBSTFDbmZzVE15SzM0REY=")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(400);
    }

    //3.4 create a task with Basic Auth with email  but in a different register - Проверка регистронезависимости логина в basic-авторизации
    // ошибочный Email токен = Basic TWFJbC5Gb1IuVGVTdEJhU2VAeUFuRGVYLnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    // верный Email    токен = Basic bWFpbC5mb3IudGVzdGJhc2VAeWFuZGV4LnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==
    @Test
    void createTaskWithBasicAuthWithDifferentRegisterTest() {
        given()
                .header("Authorization", "Basic TWFJbC5Gb1IuVGVTdEJhU2VAeUFuRGVYLnJ1Onc4M0Q4VmNiRkFJMUNuZnNUTXlLMzRERg==")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(201);
    }

    //4.1. create a task without "Accept" - Проверка заголовка «Accept»	- Не передаём заголовок
    @Test
    void createTaskWithoutAcceptTest() {
        given()
                .header("Authorization", token)
                //.header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(201);
    }

    //4.3. create a task with «Accept : application/xml» - Проверка заголовка «Accept»	- Передаём заголовок с типом не json
    @Test
    void createTaskWithoutAcceptDifferentXmlTest() {
        given()
                .header("Authorization", token)
                .header("Accept", "application/xml")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(406);
    }
    //4.4. create a task with «Accept : aPPlicATion/JsOn» - Проверка заголовка «Accept»	- Проверка регистронезависимости заголовка Accept aPPlicATion/JsOn
    @Test
    void createTaskWithoutAcceptDifferentRegisterTest() {
        given()
                .header("Authorization", token)
                .header("Accept", "aPPlicATion/JsOn")
                .header("Content-Type", "application/json")
                .body("{\n  \"fields\": {\n    \"summary\": \"позитивный запрос из документации\",\n    \"issuetype\": {\n      \"id\": \"10103\"\n    },\n    \"project\": {\n      \"id\": \"10302\"\n    }\n    }\n}\n")
                .when()
                .post("https://testbase.atlassian.net/rest/api/3/issue/")
                .then()
                .statusCode(201);
    }
}
