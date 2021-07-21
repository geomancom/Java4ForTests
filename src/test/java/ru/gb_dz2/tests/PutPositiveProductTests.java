package ru.gb_dz2.tests;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.gb_dz2.db.dao.CategoriesMapper;
import ru.gb_dz2.db.dao.ProductsMapper;
import ru.gb_dz2.dto.Product;
import ru.gb_dz2.enums.CategoryType;
import ru.gb_dz2.service.CategoryService;
import ru.gb_dz2.service.ProductService;
import ru.gb_dz2.utils.DbUtils;
import ru.gb_dz2.utils.RetrofitUtils;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.gb_dz2.utils.CommonAsserts.*;
import static ru.gb_dz2.utils.CommonLog.logGetBasic;
import static ru.gb_dz2.utils.CommonLog.logGetFull;

@Slf4j
public class PutPositiveProductTests {
    int productId;
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    Faker faker = new Faker();
    Product product;
    Product productUpd;
    static ProductsMapper productsMapperGet;
    static CategoriesMapper categoriesMapperGet;
    static ProductsMapper productsMapperUpd;
    static CategoriesMapper categoriesMapperUpd;


    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        productsMapperGet = DbUtils.getProductsMapper();
        categoriesMapperGet = DbUtils.getCategoriesMapper();
        productsMapperUpd = DbUtils.getProductsMapper();
        categoriesMapperUpd = DbUtils.getCategoriesMapper();

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
        logGetFull(response);
        productUpd = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        logGetBasic(responseUpd);
        assertGetProductIdCategoryId(productId, productUpd, productsMapperUpd, categoriesMapperUpd);
        Response<Product> responseGet = productService.getProduct(productId).execute();
        logGetBasic(responseGet);
        assertGetProductIdCategoryId(productId, productUpd, productsMapperGet, categoriesMapperGet);
        assertGetProductTitle(productId, productUpd, productsMapperGet);
        assertGetProductPrice(productId, productUpd, productsMapperGet);

    }

    //2.1.1. Обновление продукта только наименование
    @Test
    void putTitleProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        logGetFull(response);
        productUpd = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice(response.body().getPrice())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        logGetBasic(responseUpd);
        assertGetProductIdCategoryId(productId, productUpd, productsMapperUpd, categoriesMapperUpd);
        Response<Product> responseGet = productService.getProduct(productId).execute();
        logGetBasic(responseGet);
        assertGetProductIdCategoryId(productId, productUpd, productsMapperGet, categoriesMapperGet);
        assertGetProductTitle(productId, productUpd, productsMapperGet);

    }
    //2.1.2. Обновление продукта только цена
    @Test
    void putPriceProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        logGetFull(response);
        productUpd = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        logGetBasic(responseUpd);
        assertGetProductIdCategoryId(productId, productUpd, productsMapperUpd, categoriesMapperUpd);
        Response<Product> responseGet = productService.getProduct(productId).execute();
        logGetBasic(responseGet);
        assertGetProductIdCategoryId(productId, productUpd, productsMapperGet, categoriesMapperGet);
        assertGetProductPrice(productId, productUpd, productsMapperGet);

    }

    @AfterEach
    void tearDown() throws IOException {
        Response<ResponseBody> responseDel = productService.deleteProduct(productId).execute();
        assertThat(responseDel.isSuccessful(), is(true));
        //Проверяем что продукт удален
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        assert responseGet.code() == 404;
    }
}
