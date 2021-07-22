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
import static org.hamcrest.Matchers.*;
import static ru.gb_dz2.utils.CommonAsserts.*;
import static ru.gb_dz2.utils.CommonLog.logGetBasic;

@Slf4j
public class PostPositiveProductTests {
    int productId;
    static Retrofit client;
    static ProductService productService;
    static CategoryService categoryService;
    Faker faker = new Faker();
    Product product;
    static ProductsMapper productsMapper;
    static CategoriesMapper categoriesMapper;

    @BeforeAll
    static void beforeAll() {
        client = RetrofitUtils.getRetrofit();
        productService = client.create(ProductService.class);
        categoryService = client.create(CategoryService.class);
        productsMapper = DbUtils.getProductsMapper();
        categoriesMapper = DbUtils.getCategoriesMapper();
    }

    @BeforeEach
    void setUp() {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
    }


    @Test
    void postProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        logGetBasic(response);
        productId = response.body().getId();
        assertGetProductTitle(productId, product, productsMapper);
        assertGetProductPrice(productId, product, productsMapper);
        assertGetProductIdCategoryId(productId, product, productsMapper, categoriesMapper);

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
