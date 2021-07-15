package ru.gb_dz.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.core.IsEqual.equalTo;

public abstract class BaseTest {
    static ResponseSpecification positiveResponseSpecification200;
    static ResponseSpecification positiveResponseSpecification201;
    static ResponseSpecification negativeResponseSpecification400;
    static ResponseSpecification negativeResponseSpecification401;
    static ResponseSpecification negativeResponseSpecification404;
    static ResponseSpecification negativeResponseSpecification406;
    static RequestSpecification requestSpecificationWithAuth;
    static RequestSpecification requestSpecificationWithoutAuth;
    static RequestSpecification requestSpecificationWithoutAccept;

    static Properties properties = new Properties();
    static String token;
    static String key;

    @BeforeAll
    static void beforeAll() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
        getProperties();
        token = properties.getProperty("token");
        key = properties.getProperty("key");


        positiveResponseSpecification200 = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(200)
                .build();

        positiveResponseSpecification201 = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(201)
                .build();

        negativeResponseSpecification406 = new ResponseSpecBuilder()
                .expectContentType(ContentType.HTML)
                .expectStatusCode(406)
                .build();

        negativeResponseSpecification404 = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(404)
                .build();

        negativeResponseSpecification401 = new ResponseSpecBuilder()
                .expectContentType(ContentType.TEXT)
                .expectStatusCode(401)
                .build();

        negativeResponseSpecification400 = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectStatusCode(400)
                .build();

        requestSpecificationWithAuth = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        requestSpecificationWithoutAuth = new RequestSpecBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .build();

        requestSpecificationWithoutAccept = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .build();

    }


    private static void getProperties(){
        try (InputStream output = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
