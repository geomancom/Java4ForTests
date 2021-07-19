package ru.gb_dz2.tests;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.gb_dz2.db.dao.CategoriesMapper;
import ru.gb_dz2.db.dao.ProductsMapper;
import ru.gb_dz2.dto.ErrorBody;
import ru.gb_dz2.dto.Product;
import ru.gb_dz2.enums.CategoryType;
import ru.gb_dz2.service.CategoryService;
import ru.gb_dz2.service.ProductService;
import ru.gb_dz2.utils.DbUtils;
import ru.gb_dz2.utils.RetrofitUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class PutNegativeProductTests {
    int productId;
    static Retrofit client;
    static ProductService productService;
    static Retrofit client2;
    static ProductService productService2;
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


    //2.2.0. Обновление продукта с заполнением полей наименование и цена пустыми(нулевыми) данными - Обновляется, нужно заводить баг / или уточнить в документации
    @Test
    void putZeroProductNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        log.info(responseUpd.body().toString());
        assertThat(productsMapperUpd.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperUpd.selectByPrimaryKey(productsMapperUpd.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperGet.selectByPrimaryKey(productsMapperGet.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getTitle(), equalTo(null));
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getPrice(), equalTo(0));

    }

    //2.2.0.1. Обновление продукта с заполнением полей наименование и цена пустыми(нулевыми) данными и без указания категории - не обновляется
    @Test
    void putZeroWithoutCategoryProductNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId);
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        if (responseUpd != null && !responseUpd.isSuccessful() && responseUpd.errorBody() != null) {
            ResponseBody body = responseUpd.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getError(), equalTo("Internal Server Error"));
            log.info(String.valueOf(errorBody.getError()));
        }


    }
    //2.2.1. Обновление продукта только наименование с заполнением пустыми(нулевыми) данными - Обновляется, нужно заводить баг / или уточнить в документации
    @Test
    void putZeroTitleProductNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        log.info(responseUpd.body().toString());
        assertThat(productsMapperUpd.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperUpd.selectByPrimaryKey(productsMapperUpd.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperGet.selectByPrimaryKey(productsMapperGet.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getTitle(), equalTo(null));
    }

    //2.2.2. Обновление продукта только цена с заполнением пустыми(нулевыми) данными - Обновляется, нужно заводить баг / или уточнить в документации
    @Test
    void putZeroPriceProductNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        log.info(responseUpd.body().toString());
        assertThat(productsMapperUpd.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperUpd.selectByPrimaryKey(productsMapperUpd.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperGet.selectByPrimaryKey(productsMapperGet.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getPrice(), equalTo(0));
    }

    //2.2.2.1. Обновление продукта только цена с заполнением отрицательными данными - Обновляется, нужно заводить баг / или уточнить в документации
    @Test
    void putNegativePriceProductNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withTitle(faker.food().dish())
                .withPrice((int) (-1111))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        log.info(responseUpd.body().toString());
        assertThat(productsMapperUpd.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperUpd.selectByPrimaryKey(productsMapperUpd.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperGet.selectByPrimaryKey(productsMapperGet.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        assert productsMapperGet.selectByPrimaryKey((long) productId).getPrice() < 0;
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
        assertThat(productsMapperUpd.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperUpd.selectByPrimaryKey(productsMapperUpd.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperGet.selectByPrimaryKey(productsMapperGet.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getTitle(), equalTo(productUpd.getTitle()));
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
        assertThat(productsMapperUpd.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperUpd.selectByPrimaryKey(productsMapperUpd.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        log.info(responseGet.body().toString());
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperGet.selectByPrimaryKey(productsMapperGet.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getPrice(), equalTo(productUpd.getPrice()));
    }
    //2.3.0. Обновление продукта,  присвоение несуществующей категории
    @Test
    void putNegativeCategoryProductNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId)
                .withTitle(response.body().getTitle())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle("CategoryType");
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        if (responseUpd != null && !responseUpd.isSuccessful() && responseUpd.errorBody() != null) {
            ResponseBody body = responseUpd.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getError(), equalTo("Internal Server Error"));
            log.info(String.valueOf(errorBody.getError()));
        }


    }
    //2.3.1. Обновление по не существующему айди - не обновляется
    @Test
    void putNegativeIdProductNegativeTest() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        productId = response.body().getId();
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        log.info(String.valueOf(response.body().getId()));
        productUpd = new Product()
                .withId(productId+100)
                .withTitle(response.body().getTitle())
                .withPrice((int) ((Math.random() + 1) * 1000))
                .withCategoryTitle(CategoryType.FOOD.getTitle());
        Response<Product> responseUpd = productService.updateProduct(productUpd).execute();
        log.info(String.valueOf(responseUpd.code()));
        if (responseUpd != null && !responseUpd.isSuccessful() && responseUpd.errorBody() != null) {
            ResponseBody body = responseUpd.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getError(), equalTo(null));
            log.info(String.valueOf(errorBody.getError()));
        }


    }

    @AfterEach
    void tearDown() throws IOException {
        Response<ResponseBody> responseDel = productService.deleteProduct(productId).execute();
        //Проверяем что продукт удален
        Response<Product> responseGet = productService.getProduct(productId).execute();
        log.info(String.valueOf(responseGet.code()));
        assert responseGet.code() == 404;
    }
}
