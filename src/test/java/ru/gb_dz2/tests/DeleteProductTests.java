package ru.gb_dz2.tests;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.gb_dz2.dto.ErrorBody;
import ru.gb_dz2.dto.Product;
import ru.gb_dz2.enums.CategoryType;
import ru.gb_dz2.service.CategoryService;
import ru.gb_dz2.service.ProductService;
import ru.gb_dz2.utils.RetrofitUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class DeleteProductTests {
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


    //3.1.0. Удаление по существующему ID
    @Test
    void deleteProductTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        Response<Product> responseGet1 = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet1.code()));
        log.info(responseGet1.body().toString());
        productService.deleteProduct(productId).execute();
        Response<Product> responseGet2 = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet2.code()));
        if (responseGet2 != null && !responseGet2.isSuccessful() && responseGet2.errorBody() != null) {
            ResponseBody body = responseGet2.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getError(), equalTo(null));
            log.info(String.valueOf(errorBody.getError()));

        }

    }

    //3.2.0. Удаление по несуществующему ID
    @Test
    void deleteProductNegativeTest() throws IOException {
        //Создаем
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        //Проверяем что продукт создан
        Response<Product> responseGet1 = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet1.code()));
        log.info(responseGet1.body().toString());
        //Проверяем что продукт ИД+100 не существует
        Response<Product> responseGet2 = productService.getProduct(productId+100).execute();
        log.info(String.valueOf(responseGet2.code()));
        if (responseGet2 != null && !responseGet2.isSuccessful() && responseGet2.errorBody() != null) {
            ResponseBody body = responseGet2.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getError(), equalTo(null));
            log.info(String.valueOf(errorBody.getError()));
        }
        //Удаляем продукт ИД+100 который не существует
        productService.deleteProduct(productId+100).execute();
        Response<Product> responseGet3 = productService.getProduct(productId+100).execute();
        log.info(String.valueOf(responseGet3.code()));
            if (responseGet3 != null && !responseGet3.isSuccessful() && responseGet3.errorBody() != null) {
                ResponseBody body = responseGet3.errorBody();
                Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
                ErrorBody errorBody = converter.convert(body);
                assertThat(errorBody.getError(), equalTo(null));
                log.info(String.valueOf(errorBody.getError()));

            }

    }


}
