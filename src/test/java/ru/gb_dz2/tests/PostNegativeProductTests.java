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
import ru.gb_dz2.dto.Category;
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
import static org.hamcrest.Matchers.is;

@Slf4j
public class PostNegativeProductTests {
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

    //1.2.0. Новый продукт без названия - Создается, нужно заводить баг / или уточнить в документации
    @Test
    void postProductEmptyTitleNegativeTest() throws IOException {
        product = new Product()
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 201;
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        productId = response.body().getId();
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getTitle(), equalTo(null));
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getPrice(), equalTo(product.getPrice()));
        assertThat(categoriesMapper.selectByPrimaryKey(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
    }

    //1.2.1. Новый продукт без цены - Создается, нужно заводить баг / или уточнить в документации
    @Test
    void postProductEmptyPriceNegativeTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 201;
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        productId = response.body().getId();
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getTitle(), equalTo(product.getTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getPrice(), equalTo(0));
        assertThat(categoriesMapper.selectByPrimaryKey(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));

    }

    //1.2.2. Новый продукт с отрицательной ценой - Создается, нужно заводить баг / или уточнить в документации
    @Test
    void postProductEmptyNegativePriceNegativeTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * -100))
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 201;
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        productId = response.body().getId();
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getTitle(), equalTo(product.getTitle()));
        assert productsMapper.selectByPrimaryKey((long) productId).getPrice() < 0;
        assertThat(categoriesMapper.selectByPrimaryKey(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));

    }

    //1.2.3. Новый продукт с дробной ценой - Создается, нужно заводить баг / или уточнить в документации (округляется не  по законам математики)
    @Test
    void postProductEmptyFloatPriceNegativeTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(294/100)
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 201;
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        productId = response.body().getId();
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getTitle(), equalTo(product.getTitle()));
        assertThat(categoriesMapper.selectByPrimaryKey(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
    }

    //1.2.4. Новый продукт с предельной ценой +1 - Создается, нужно заводить баг / запредельная цена становится отрицательной
    @Test
    void postProductEmptyMaxPriceNegativeTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(2147483647+1)
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 201;
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        productId = response.body().getId();
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getTitle(), equalTo(product.getTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getPrice(), equalTo(product.getPrice()));
        assertThat(categoriesMapper.selectByPrimaryKey(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
    }

    //1.2.5. Новый продукт c ценой = 0 - Создается, нужно заводить баг / или уточнить в документации
    @Test
    void postProductZeroPriceNegativeTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice(0)
                .withCategoryTitle(CategoryType.FOOD.getTitle());

        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 201;
        log.info(String.valueOf(response.code()));
        log.info(response.body().toString());
        productId = response.body().getId();
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getTitle(), equalTo(product.getTitle()));
        assertThat(productsMapper.selectByPrimaryKey((long) productId).getPrice(), equalTo(0));
        assertThat(categoriesMapper.selectByPrimaryKey(productsMapper.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));

    }

    //1.3.0. Новый продукт без категории - не создается
    @Test
    void postProductWithoutCategoryNegativeTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100));
        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 500;
        log.info(String.valueOf(response.code()));
        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getError(), equalTo("Internal Server Error"));
            log.info(String.valueOf(errorBody.getError()));
        }
    }

    //1.3.1. Новый продукт c категории, которой не существует - не создается
    @Test
    void postProductNegativeCategoryNegativeTest() throws IOException {
        product = new Product()
                .withTitle(faker.food().dish())
                .withPrice((int) ((Math.random() + 1) * 100))
                .withCategoryTitle("CategoryType");
        Response<Product> response = productService.createProduct(product).execute();
        assert response.code() == 500;
        log.info(String.valueOf(response.code()));
        if (response != null && !response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, ErrorBody> converter = RetrofitUtils.getRetrofit().responseBodyConverter(ErrorBody.class, new Annotation[0]);
            ErrorBody errorBody = converter.convert(body);
            assertThat(errorBody.getError(), equalTo("Internal Server Error"));
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
