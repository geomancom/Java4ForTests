package ru.gb_dz2.utils;

import ru.gb_dz2.db.dao.CategoriesMapper;
import ru.gb_dz2.db.dao.ProductsMapper;
import ru.gb_dz2.dto.Product;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CommonAsserts {
    public static void assertGetProductIdCategoryId(Integer productId, Product product, ProductsMapper productsMapperGet, CategoriesMapper categoriesMapperGet ) {
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getId().intValue(), equalTo(productId));
        assertThat(categoriesMapperGet.selectByPrimaryKey(productsMapperGet.selectByPrimaryKey(Long.valueOf(productId)).getCategory_id().intValue()).getTitle(), equalTo(product.getCategoryTitle()));
    }

    public static void assertGetProductTitle(Integer productId, Product product, ProductsMapper productsMapperGet) {
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getTitle(), equalTo(product.getTitle()));
    }

    public static void assertGetProductPrice(Integer productId, Product product, ProductsMapper productsMapperGet) {
        assertThat(productsMapperGet.selectByPrimaryKey((long) productId).getPrice(), equalTo(product.getPrice()));
    }





}
