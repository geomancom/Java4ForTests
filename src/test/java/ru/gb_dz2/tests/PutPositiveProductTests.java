package ru.gb_dz2.tests;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.gb_dz2.dto.Category;
import ru.gb_dz2.dto.Product;
import ru.gb_dz2.enums.CategoryType;
import ru.gb_dz2.service.CategoryService;
import ru.gb_dz2.service.ProductService;
import ru.gb_dz2.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class PutPositiveProductTests {
    static Retrofit client;
    static ProductService productService;
    static Retrofit client2;
    static ProductService productService2;
    static CategoryService categoryService;
    Faker faker = new Faker();
    Product product;
    Product productUpd;
    Integer productId;


    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());

    }


    //2.1.0. Обновление продукта с заменой полей наименование и цена
    @Test
    void putPriceTitleProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        log.info(responseUpd.body().toString());
        assertThat(responseUpd.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpd.body().getId(), equalTo(productId));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));

    }

    //2.1.1. Обновление продукта только наименование
    @Test
    void putTitleProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice(response.body().getPrice())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        log.info(responseUpd.body().toString());
        assertThat(responseUpd.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpd.body().getId(), equalTo(productId));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));

    }
    //2.1.2. Обновление продукта только цена
    @Test
    void putPriceProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        log.info(responseUpd.body().toString());
        assertThat(responseUpd.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseUpd.body().getId(), equalTo(productId));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(responseGet.body().getCategoryTitle(), equalTo(product.getCategoryTitle()));
        assertThat(responseGet.body().getId(), equalTo(productId));

    }


}
